package architecture.annotations.impl;

import architecture.annotations.LengthOrEmpty;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class LengthOrEmptyImpl implements ConstraintValidator<LengthOrEmpty, String> {
    private int min;
    private int max;

    @Override
    public void initialize(LengthOrEmpty constraintAnnotation) {
        this.min = constraintAnnotation.min();
        this.max = constraintAnnotation.max();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isEmpty()) {
            return true;
        }
        int valueLength = value.length();
        return valueLength >= min && valueLength <= max;
    }
}
