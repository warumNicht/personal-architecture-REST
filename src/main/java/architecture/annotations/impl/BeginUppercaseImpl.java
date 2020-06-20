package architecture.annotations.impl;

import architecture.annotations.BeginUppercase;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class BeginUppercaseImpl implements ConstraintValidator<BeginUppercase, String> {
    private boolean allowNull;
    private boolean allowEmpty;

    @Override
    public void initialize(BeginUppercase constraintAnnotation) {
        this.allowEmpty = constraintAnnotation.allowEmpty();
        this.allowNull = constraintAnnotation.allowNull();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        if (value == null) {
            return this.allowNull;
        }
        if (value.isEmpty()) {
            return this.allowEmpty;
        }
        char firstChar = value.charAt(0);
        return Character.isUpperCase(firstChar);
    }
}
