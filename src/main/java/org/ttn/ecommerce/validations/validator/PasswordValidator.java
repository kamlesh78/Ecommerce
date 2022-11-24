package org.ttn.ecommerce.validations.validator;

import org.ttn.ecommerce.validations.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password,String> {

    @Override
    public boolean isValid(String passwordConstraint, ConstraintValidatorContext constraintValidatorContext) {
        if(passwordConstraint.matches("^(?=.*[0-9])"+ "(?=.*[a-z])(?=.*[A-Z])"+ "(?=.*[@#$%^&+=])"+ "(?=\\S+$).{8,20}$")){
            System.out.println(passwordConstraint);

            System.out.println("a");
            return true;

        }else{
            System.out.println(passwordConstraint);

            System.out.println("b");
            return false;

        }
    }
}
//"((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,15})"