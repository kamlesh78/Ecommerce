package org.ttn.ecommerce.validations;

import org.ttn.ecommerce.validations.validator.GstValidator;
import org.ttn.ecommerce.validations.validator.UserEmailValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = GstValidator.class)
@Target({ElementType.FIELD})
public @interface Gst {
    String message() default "{Provided Gst number is not valid ! Please provide valid Gst number}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
