package architecture.annotations;

import architecture.annotations.impl.LengthOrEmptyImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.TYPE_USE})
@Constraint(validatedBy = LengthOrEmptyImpl.class)
public @interface LengthOrEmpty {
    int min() default 0;

    int max() default Integer.MAX_VALUE;

    String message() default "{text.length.between}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
