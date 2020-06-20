package architecture.error;

import architecture.constants.AppConstants;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.web.util.WebUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException e) throws IOException, ServletException {
        Cookie actualCookie = WebUtils.getCookie(request, AppConstants.LOCALE_COOKIE_NAME);
        String localeContext = actualCookie != null ? actualCookie.getValue() : "en";
        request.getSession().setAttribute("accessDeniedException", e);
        String contextPath = request.getContextPath();
        String redirectedUrl = String.format("%s/%s/%s",
                contextPath, localeContext, "unauthorized");
        response.sendRedirect(redirectedUrl);
    }
}
