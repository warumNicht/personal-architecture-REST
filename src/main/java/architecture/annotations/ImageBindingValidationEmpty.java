package architecture.annotations;

import architecture.annotations.impl.ImageBindingValidationImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.FIELD)
@Constraint(validatedBy = ImageBindingValidationImpl.class)
public @interface ImageBindingValidationEmpty {
    String message() default "Not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
