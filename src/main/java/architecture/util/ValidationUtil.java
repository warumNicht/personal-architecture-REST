package architecture.util;

import javax.validation.ConstraintViolation;
import java.util.Set;

public interface ValidationUtil {
    <T> boolean isValid(T model);

    <T> Set<ConstraintViolation<T>> getViolations(T model);
}
