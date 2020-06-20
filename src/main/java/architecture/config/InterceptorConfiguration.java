package architecture.config;

import architecture.web.interceptors.CustomLocalInterceptor;
import architecture.web.interceptors.LocalizeURLInterceptor;
import architecture.web.interceptors.UrlLocaleInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class InterceptorConfiguration implements WebMvcConfigurer {
    private final String[] EXCLUDED_PATHS = new String[]{"/js/**", "/css/**", "/images/**", "/fetch/**", "/error",};

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        LocalizeURLInterceptor localizeURLInterceptor = new LocalizeURLInterceptor();
        registry.addInterceptor(localizeURLInterceptor).excludePathPatterns(EXCLUDED_PATHS).order(Ordered.HIGHEST_PRECEDENCE);

        UrlLocaleInterceptor localeInterceptor = new UrlLocaleInterceptor();
        registry.addInterceptor(localeInterceptor).excludePathPatterns(EXCLUDED_PATHS).order(Ordered.HIGHEST_PRECEDENCE - 1);

        LocaleChangeInterceptor lci = new CustomLocalInterceptor();
        lci.setParamName("lang");
        registry.addInterceptor(lci).excludePathPatterns(EXCLUDED_PATHS).order(Ordered.LOWEST_PRECEDENCE);
    }
}
