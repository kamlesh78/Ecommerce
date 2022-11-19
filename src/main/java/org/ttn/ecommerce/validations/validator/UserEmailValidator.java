package org.ttn.ecommerce.validations.validator;

import org.springframework.beans.factory.annotation.Autowired;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.repository.UserRepository;
import org.ttn.ecommerce.validations.Email;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import java.util.Optional;

public class UserEmailValidator implements ConstraintValidator<Email,String> {

    @Autowired
    UserRepository userRepository;

    @Override
    public boolean isValid(String userEmail , ConstraintValidatorContext constraintValidatorContext) {
        System.out.println(userEmail);
        Optional<UserEntity> userEntity = userRepository.findByEmail(userEmail);
        if(userEntity.isPresent()){
            return false;
        }else{
            return true;
        }
    }
}
