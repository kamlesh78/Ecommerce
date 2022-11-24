package org.ttn.ecommerce.validations.validator;

import org.ttn.ecommerce.dto.register.SellerRegisterDto;
import org.ttn.ecommerce.validations.CustomerPasswordMatcher;
import org.ttn.ecommerce.validations.SellerPasswordMatcher;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class SellerPasswordValidator implements ConstraintValidator<SellerPasswordMatcher, Object> {


    @Override
    public void initialize( SellerPasswordMatcher sellerPasswordMatcher){

    }
    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        SellerRegisterDto sellerRegisterDto = (SellerRegisterDto) o;
        return sellerRegisterDto.getPassword().equals(sellerRegisterDto.getConfirmPassword());
    }
}


