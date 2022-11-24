package org.ttn.ecommerce.validations.validator;

import org.ttn.ecommerce.validations.Gst;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class GstValidator implements ConstraintValidator<Gst,String>{
    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        if(s.matches("^([0][1-9]|[1-2][0-9]|[3][0-7])([a-zA-Z]{5}[0-9]{4}[a-zA-Z]{1}[1-9a-zA-Z]{1}[zZ]{1}[0-9a-zA-Z]{1})+$")){
            return true;
        }else{
            return false;
        }
    }
}
