package org.ttn.ecommerce.validations;

import org.ttn.ecommerce.validations.validator.PhoneNumberValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Constraint(validatedBy = PhoneNumberValidator.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface PhoneNumber {
    String message() default "{Provided Phone number is not Valid}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
