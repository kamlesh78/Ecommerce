package org.ttn.ecommerce.validations.validator;

import org.ttn.ecommerce.validations.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password,String> {

    @Override
    public boolean isValid(String passwordConstraint, ConstraintValidatorContext constraintValidatorContext) {
        System.out.println(passwordConstraint);
        if(passwordConstraint.matches("((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,15})")){
            return true;

        }else{
            return false;

        }
    }
}
