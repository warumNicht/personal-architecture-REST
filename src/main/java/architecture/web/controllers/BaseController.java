package architecture.web.controllers;

import architecture.domain.CountryCodes;
import architecture.services.interfaces.LocaleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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

    protected HashMap<String, List<String>> getBindingErrorsMap(List<ObjectError> allErrors) {
        HashMap<String, List<String>> errors = new HashMap<>();
        for (ObjectError currentError : allErrors) {
            FieldError fieldError = (FieldError) currentError;
            errors.putIfAbsent(fieldError.getField(), new ArrayList<>());
            errors.get(fieldError.getField()).add(fieldError.getDefaultMessage());
        }
        return errors;
    }
}
