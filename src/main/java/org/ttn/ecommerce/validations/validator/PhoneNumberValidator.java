package org.ttn.ecommerce.validations.validator;

import org.ttn.ecommerce.validations.PhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber, String> {


    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {

        if(phoneNumber.matches("^$|[0-9]{10}")){
            return true;
        }else{
            return false;
        }
    }
}
