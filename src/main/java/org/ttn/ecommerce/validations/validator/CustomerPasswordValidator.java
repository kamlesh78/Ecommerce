package org.ttn.ecommerce.validations.validator;


import org.ttn.ecommerce.dto.register.CustomerRegisterDto;
import org.ttn.ecommerce.validations.CustomerPasswordMatcher;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomerPasswordValidator implements ConstraintValidator<CustomerPasswordMatcher, Object> {

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        CustomerRegisterDto customerRegisterDto = (CustomerRegisterDto) o;
        return customerRegisterDto.getPassword().equals(customerRegisterDto.getConfirmPassword());
     }
}


