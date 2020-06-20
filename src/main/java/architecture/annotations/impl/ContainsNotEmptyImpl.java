package architecture.annotations.impl;

import architecture.annotations.ContainsNotEmpty;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Map;

public class ContainsNotEmptyImpl implements ConstraintValidator<ContainsNotEmpty, Map<?, String>> {
    @Override
    public boolean isValid(Map<?, String> value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        for (String s : value.values()) {
            if (s != null && !s.isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
