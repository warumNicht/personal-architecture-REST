package architecture.config;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashSet;

public class NoAntPathRequestMatcher implements RequestMatcher {
    private final HashSet<String> allowedMethods = new HashSet<>(
            Arrays.asList("GET", "HEAD", "TRACE", "OPTIONS"));

    // Disable CSFR protection on the following urls:
    private AntPathRequestMatcher[] requestMatchers = {
            new AntPathRequestMatcher("/users/rest-authentication")
    };

    @Override
    public boolean matches(HttpServletRequest request) {
        System.out.println(request.getRequestURI());
        // If the request match one url the CSFR protection will be disabled
        for (AntPathRequestMatcher rm : requestMatchers) {
            if (rm.matches(request)) {
                return false;
            }
        }
        // Disable CSFR protection for allowed methods:
        return !this.allowedMethods.contains(request.getMethod());
    }
}
