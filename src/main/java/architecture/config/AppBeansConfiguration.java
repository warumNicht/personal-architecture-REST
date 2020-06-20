package architecture.config;

import architecture.constants.AppConstants;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletAutoConfiguration;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.http.CacheControl;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class AppBeansConfiguration implements WebMvcConfigurer {

    @Bean
    public LocaleResolver localeResolver() {
        CustomLocalResolver customLocalResolver = new CustomLocalResolver();
        customLocalResolver.setDefaultLocale(AppConstants.DEFAULT_LOCALE);
        customLocalResolver.setCookieHttpOnly(true);
        customLocalResolver.setCookieName(AppConstants.LOCALE_COOKIE_NAME);
        customLocalResolver.setCookieMaxAge(60 * 60);
        return customLocalResolver;
    }

    @Bean
    public ServletRegistrationBean dispatcherRegistration(DispatcherServlet dispatcherServlet) {

        ServletRegistrationBean registration = new ServletRegistrationBean(
                dispatcherServlet, "/bg/*", "/de/*", "/en/*", "/fr/*", "/es/*");

        registration.setName(DispatcherServletAutoConfiguration.DEFAULT_DISPATCHER_SERVLET_REGISTRATION_BEAN_NAME);
        return registration;
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource
                = new ReloadableResourceBundleMessageSource();

        messageSource.setBasenames(
                "classpath:/messages/common/messages",
                "classpath:/messages/home/messages",
                "classpath:/messages/nav/messages",
                "classpath:/messages/validation/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource());
        return bean;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(AppConstants.CASH_MAX_AGE, TimeUnit.HOURS).cachePublic());
    }
}
