package org.ttn.ecommerce.validations.validator;

import org.ttn.ecommerce.dto.register.CustomerRegisterDto;
import org.ttn.ecommerce.entities.UserEntity;
import org.ttn.ecommerce.validations.ConfirmPassword;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatcher implements ConstraintValidator<ConfirmPassword,Object> {


    @Override
    public boolean isValid(Object user, ConstraintValidatorContext constraintValidatorContext) {
        CustomerRegisterDto customerRegisterDto = (CustomerRegisterDto) user;
        return customerRegisterDto.getPassword().equals(customerRegisterDto.getConfirmPassword());
    }
}
