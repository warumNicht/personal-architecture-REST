package architecture.services;

import architecture.constants.AppConstants;
import architecture.domain.CountryCodes;
import architecture.services.interfaces.LocaleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service(value = "localeService")
public class LocaleServiceImpl implements LocaleService {
    private final String[] OLD_ANDROID = {
            "/js/lib/polyfills/polyfill.min.js",
            "/js/lib/polyfills/babel-browser-build.js",
            "/js/lib/polyfills/nodelist-polyfill.js"};

    private final String[] IE_SCRIPTS = {
            "/js/lib/polyfills/polyfill.min.js",
            "/js/lib/polyfills/babel-browser-build.js",
            "/js/lib/polyfills/browser-es-module-loader.js",
            "/js/lib/polyfills/nodelist-polyfill.js"};

    private HttpServletRequest request;

    @Autowired
    public LocaleServiceImpl(HttpServletRequest request) {
        this.request = request;
    }

    @Override
    public String getLocale() {
        return this.getCurrentCookieLocale().toString().toLowerCase();
    }

    @Override
    public boolean checkOldAndroid() {
        String userAgent = this.request.getHeader("user-agent");
        if(userAgent==null){
            return false;
        }
        System.out.println(userAgent);

        String androidPattern = "[A|a]ndroid\\s+(\\d+)";

        Pattern pattern = Pattern.compile(androidPattern);
        Matcher matcher = pattern.matcher(userAgent);
        if (matcher.find()) {
            String androidVersion = matcher.group(1);
            System.out.println(androidVersion);
            int version = Integer.parseInt(androidVersion);
            return version < AppConstants.ANDROID_VERSION_WITHOUT_POLYFILLS;
        }
        return false;
    }

    @Override
    public CountryCodes getCurrentCookieLocale() {
        Cookie actualCookie = WebUtils.getCookie(this.request, AppConstants.LOCALE_COOKIE_NAME);
        if (actualCookie != null) {
            return CountryCodes.valueOf(actualCookie.getValue().toUpperCase());
        }
        return AppConstants.DEFAULT_COUNTRY_CODE;
    }

    @Override
    public String loadScripts() {
        StringBuilder res = new StringBuilder();
        this.listNeededScripts().forEach(s -> {
            String current = String.format("<script src=\"%s\"></script>", s);
            res.append(current).append('\n');
        });
        return res.toString();
    }

    private List<String> listNeededScripts() {
        String userAgent = this.request.getHeader("user-agent");
        List<String> emptyScrips = new ArrayList<>();
        if(userAgent==null){
            return emptyScrips;
        }

        if (userAgent.contains("Trident/") || userAgent.contains("MSIE ")) {
            return Arrays.asList(IE_SCRIPTS);
        }
        if (this.checkOldAndroid()) {
            return Arrays.asList(OLD_ANDROID);
        }
        return emptyScrips;
    }
}
