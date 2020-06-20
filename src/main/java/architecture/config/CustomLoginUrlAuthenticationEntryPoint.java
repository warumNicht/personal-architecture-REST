package architecture.config;

import architecture.constants.AppConstants;
import architecture.services.interfaces.LocaleService;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLoginUrlAuthenticationEntryPoint extends LoginUrlAuthenticationEntryPoint {

    private final LocaleService localeService;

    public CustomLoginUrlAuthenticationEntryPoint(String loginFormUrl, LocaleService localeService) {
        super(loginFormUrl);
        this.localeService = localeService;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String requestURI = request.getRequestURI();
        if (requestURI.charAt(3) == '/') {
            requestURI = requestURI.substring(3);
        }
        request.getSession().setAttribute(AppConstants.LOGIN_REFERRER_SESSION_ATTRIBUTE_NAME, requestURI);
        super.commence(request, response, authException);
    }

    @Override
    public String getLoginFormUrl() {
        return "/" + this.localeService.getLocale() + super.getLoginFormUrl();
    }
}
