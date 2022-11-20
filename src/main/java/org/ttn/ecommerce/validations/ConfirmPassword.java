package org.ttn.ecommerce.validations;

import org.ttn.ecommerce.validations.validator.PasswordMatcher;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Type;

import static java.lang.annotation.ElementType.TYPE;

@Target({TYPE})
@Constraint(validatedBy = PasswordMatcher.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfirmPassword {

    String message() default "{Password did not matched}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
