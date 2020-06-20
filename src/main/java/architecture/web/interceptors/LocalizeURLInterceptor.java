package architecture.web.interceptors;

import architecture.constants.AppConstants;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LocalizeURLInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.print("URL correction interceptor ");
        System.out.println(request.getRequestURI());

        String requestMethod = request.getMethod();
        if (!requestMethod.equals("GET")) {
            return true;
        }

        String requestURI = request.getRequestURI();
        boolean hasLocale = false;
        try {
            char slash = requestURI.charAt(3);
            if (slash == '/') {
                hasLocale = true;
            }
        } catch (Exception e) {
        }
        if (hasLocale) {
            return true;
        }
        String localeToAppend;
        Cookie actualCookie = WebUtils.getCookie(request, AppConstants.LOCALE_COOKIE_NAME);
        if (actualCookie != null) {
            localeToAppend = actualCookie.getValue();
        } else {
            localeToAppend = AppConstants.DEFAULT_LOCALE.getLanguage();
        }
        response.sendRedirect("/" + localeToAppend + requestURI);
        return false;
    }
}
