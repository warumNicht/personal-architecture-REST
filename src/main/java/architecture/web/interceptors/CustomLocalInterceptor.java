package architecture.web.interceptors;

import architecture.constants.AppConstants;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomLocalInterceptor extends LocaleChangeInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {

        System.out.print("Local interceptor   ");
        System.out.println(request.getRequestURI());

        super.preHandle(request, response, handler);
        String newLocale = (String) request.getParameter(AppConstants.LOCALE_COOKIE_NAME);
        if (newLocale != null && request.getAttribute(AppConstants.LOCALE_COOKIE_NAME) != null && (boolean) request.getAttribute(AppConstants.LOCALE_COOKIE_NAME)) {
            String requestURI = request.getRequestURI();
            if (requestURI.charAt(3) == '/') {
                requestURI = "/" + newLocale + requestURI.substring(3);
            }
            try {
                request.setAttribute(AppConstants.LOCALE_COOKIE_NAME, false);
                response.sendRedirect(requestURI);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
        return true;
    }
}
