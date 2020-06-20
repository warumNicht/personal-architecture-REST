package architecture.config.jwt;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.DefaultCsrfToken;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.UUID;

@Component
public class JWTCsrfTokenRepository implements CsrfTokenRepository {
    private static final Logger log = LoggerFactory.getLogger(JWTCsrfTokenRepository.class);
    private byte[] secret;

    @Autowired
    public JWTCsrfTokenRepository(SecretService secretService) {
        this.secret = secretService.getHS256SecretBytes();
    }

    public CsrfToken generateLoginToken(Object userJwtToken) {
        String token = this.setBaseJwtInfo()
                .claim("user" , userJwtToken)
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", token);
    }

    @Override
    public CsrfToken generateToken(HttpServletRequest request) {
        String token = this.setBaseJwtInfo()
                .signWith(SignatureAlgorithm.HS256, secret)
                .compact();

        return new DefaultCsrfToken("X-CSRF-TOKEN", "_csrf", token);
    }

    @Override
    public void saveToken(CsrfToken token, HttpServletRequest request, HttpServletResponse response) {
        if (token == null) {
            HttpSession session = request.getSession(false);
            if (session != null) {
                session.removeAttribute("_csrf");
            }
        } else {
            HttpSession session = request.getSession();
            session.setAttribute("_csrf", token);
        }
    }

    @Override
    public CsrfToken loadToken(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("_csrf")==null) {
            return null;
        }
        return (CsrfToken) session.getAttribute("_csrf");
    }

    private JwtBuilder setBaseJwtInfo(){
        String id = UUID.randomUUID()
                .toString()
                .replace("-", "");

        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + (1000 * 60 * 60)); // 1 h

        return Jwts.builder()
                .setId(id)
                .setIssuedAt(now)
                .setNotBefore(now)
                .setExpiration(exp);
    }
}
