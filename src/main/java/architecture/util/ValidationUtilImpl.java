package architecture.util;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

public class ValidationUtilImpl implements ValidationUtil {
    private Validator validator;

    public ValidationUtilImpl() {
        this.validator = Validation.buildDefaultValidatorFactory().getValidator();
    }

    @Override
    public <T> boolean isValid(T model) {
        return this.validator.validate(model).isEmpty();
    }

    @Override
    public <T> Set<ConstraintViolation<T>> getViolations(T model) {
        return this.validator.validate(model);
    }
}
