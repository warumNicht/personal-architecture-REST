package architecture.web.controllers;

import architecture.domain.CountryCodes;
import architecture.services.interfaces.LocaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public abstract class BaseController {
    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private LocaleService localeService;

    protected CountryCodes getCurrentCookieLocale() {
        return this.localeService.getCurrentCookieLocale();
    }

    protected String getLocale() {
        return this.localeService.getLocale();
    }
}
