package architecture.annotations.impl;

import architecture.annotations.ImageBindingValidationEmpty;
import architecture.constants.AppConstants;
import architecture.domain.models.bindingModels.images.ImageBindingModel;
import org.hibernate.validator.constraintvalidation.HibernateConstraintValidatorContext;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class ImageBindingValidationImpl implements ConstraintValidator<ImageBindingValidationEmpty, ImageBindingModel> {
    @Override
    public boolean isValid(ImageBindingModel value, ConstraintValidatorContext context) {
        if (value == null || value.getUrl() == null || value.getName() == null) {
            return false;
        }
        if (value.getName().isEmpty() && value.getUrl().isEmpty()) {
            return true;
        }

        boolean isValid = true;

        //url
        if (value.getUrl().isEmpty()) {
            context.buildConstraintViolationWithTemplate("{text.empty}")
                    .addPropertyNode("url")
                    .addConstraintViolation();
            isValid = false;
        }

        if (!value.getUrl().matches(AppConstants.URL_REGEX_PATTERN)) {
            context.buildConstraintViolationWithTemplate("{url}")
                    .addPropertyNode("url")
                    .addConstraintViolation();
            isValid = false;
        }

        //name
        if (value.getName().isEmpty()) {
            context.buildConstraintViolationWithTemplate("{text.empty}")
                    .addPropertyNode("name")
                    .addConstraintViolation();
            isValid = false;
        }

        if (value.getName().length() < AppConstants.NAME_MIN_LENGTH) {
            HibernateConstraintValidatorContext hibernateContext = context.unwrap(HibernateConstraintValidatorContext.class);

            hibernateContext.addMessageParameter("min", AppConstants.NAME_MIN_LENGTH)
                    .buildConstraintViolationWithTemplate("{text.length.min}")
                    .addPropertyNode("name")
                    .addConstraintViolation();
            isValid = false;
        }

        boolean isUpperCase = !value.getName().isEmpty() && Character.isUpperCase(value.getName().charAt(0));
        if (!isUpperCase) {
            context.buildConstraintViolationWithTemplate("{text.beginUppercase}")
                    .addPropertyNode("name")
                    .addConstraintViolation();
            isValid = false;
        }
        return isValid;
    }
}
