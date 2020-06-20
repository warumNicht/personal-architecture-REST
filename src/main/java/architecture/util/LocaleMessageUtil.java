package architecture.util;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.Locale;

public class LocaleMessageUtil {
    private static MessageSource messageSource;

    static {
        messageSource = SpringContext.getBean(MessageSource.class);
    }

    public static String getLocalizedMessage(String message) {
        Locale locale = LocaleContextHolder.getLocale();
        String messageLocalized = messageSource.getMessage(message, null, locale);
        return messageLocalized;
    }

    public static String getLocalizedMessage(String message, Locale locale) {
        return messageSource.getMessage(message, null, locale);
    }
}
