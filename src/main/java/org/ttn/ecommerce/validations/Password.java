package org.ttn.ecommerce.validations;

import org.ttn.ecommerce.validations.validator.PasswordValidator;
import org.ttn.ecommerce.validations.validator.UserEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD})
public @interface Password {
    String message() default "{ Password must contain one digit from 1 to 9, " +
            "one lowercase letter, one uppercase letter, one special character, no space, " +
            "and it must be 8-16 characters long}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
