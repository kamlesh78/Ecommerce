package org.ttn.ecommerce.validations;

import org.ttn.ecommerce.validations.validator.UserEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = UserEmailValidator.class)
@Target({ElementType.FIELD})
public @interface UniqueEmail {
    String message() default "Account with this email already exists || Or Email address is invalid";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
