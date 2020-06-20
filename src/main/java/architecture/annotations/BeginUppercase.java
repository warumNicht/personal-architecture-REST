package architecture.annotations;

import architecture.annotations.impl.BeginUppercaseImpl;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = {ElementType.FIELD, ElementType.TYPE_USE})
@Constraint(validatedBy = BeginUppercaseImpl.class)
public @interface BeginUppercase {

    boolean allowNull() default false;

    boolean allowEmpty() default false;

    String message() default "{text.beginUppercase}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
