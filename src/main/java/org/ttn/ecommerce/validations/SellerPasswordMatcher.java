package org.ttn.ecommerce.validations;

import org.ttn.ecommerce.validations.validator.SellerPasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = SellerPasswordValidator.class)
@Target({TYPE})
public @interface SellerPasswordMatcher {

    String message() default "{Password did not matched}";

    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
