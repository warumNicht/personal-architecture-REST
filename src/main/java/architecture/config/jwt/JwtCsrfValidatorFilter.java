package architecture.config.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.json.JSONObject;
import org.springframework.http.MediaType;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

public class JwtCsrfValidatorFilter extends OncePerRequestFilter {
    // must be ascending
    private String[] ignoreCsrfAntMatchers = {  "/logout", "/users/authentication" ,
            "/users/custom-logout", "/users/rest-authentication" };

    private SecretService secretService;

    public JwtCsrfValidatorFilter(SecretService secretService) {
        this.secretService = secretService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // NOTE: A real implementation should have a nonce cache so the token cannot be reused

        CsrfToken token = (CsrfToken) request.getAttribute("_csrf");
        System.out.println(request.getServletPath());
        if (
            // only care if it's not GET
                ! "GET".equals(request.getMethod()) &&
                        // ignore if the request path is in our list
                        Arrays.binarySearch(ignoreCsrfAntMatchers, request.getServletPath()) < 0 &&
                        // make sure we have a token
                        token != null) {
            // CsrfFilter already made sure the token matched. Here, we'll make sure it's not expired
            try {
                Jws<Claims> claimsJws = Jwts.parser()
                        .setSigningKeyResolver(secretService.getSigningKeyResolver())
                        .parseClaimsJws(token.getToken());
                System.out.println();
            } catch (JwtException e) {
                // most likely an ExpiredJwtException, but this will handle any
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                JSONObject errorObj = new JSONObject();
                errorObj.put("message", e.getMessage());
                response.getWriter().write(errorObj.toString());
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
