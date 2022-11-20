package org.ttn.ecommerce.validations.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.repository.UserRepository;
import org.ttn.ecommerce.validations.Email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Optional;
import java.util.regex.Pattern;

public class UserEmailValidator implements ConstraintValidator<Email,String> {

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean isValid(String userEmail , ConstraintValidatorContext constraintValidatorContext) {
        System.out.println(userEmail);
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\."+"[a-zA-Z0-9_+&*-]+)*@" +"(?:[a-zA-Z0-9-]+\\.)+[a-z" +"A-Z]{2,7}$";
        Pattern pat = Pattern.compile(emailRegex);

        Optional<UserEntity> userEntity = userRepository.findByEmail(userEmail);
        if(userEntity.isPresent()){
            if(pat.matcher(userEmail).matches()){
                return true;
            }else{
                return  false;
            }
         }else{
            return true;
        }
    }
}
