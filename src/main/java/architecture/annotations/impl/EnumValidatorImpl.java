package architecture.annotations.impl;

import architecture.annotations.EnumValidator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EnumValidatorImpl implements ConstraintValidator<EnumValidator, Enum<?>> {
    private Class enumToValidate;

    @Override
    public void initialize(EnumValidator constraintAnnotation) {
        this.enumToValidate = constraintAnnotation.enumClass();
    }

    @Override
    public boolean isValid(Enum<?> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        try {
            Enum currentEnum = Enum.valueOf(this.enumToValidate, value.toString());
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
