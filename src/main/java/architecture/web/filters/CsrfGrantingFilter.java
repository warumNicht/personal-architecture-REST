package architecture.web.filters;

import org.springframework.security.web.csrf.CsrfToken;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CsrfGrantingFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        if(httpServletRequest.getRequestURI().contains("/session")){
            CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
            String token = csrf.getToken();
            Cookie[] cookies = httpServletRequest.getCookies();
            Cookie cookie = new Cookie("ses", token);
            cookie.setPath("/");
            ((HttpServletResponse) response).addCookie(cookie);
        }

        chain.doFilter(request, response);
    }
}
