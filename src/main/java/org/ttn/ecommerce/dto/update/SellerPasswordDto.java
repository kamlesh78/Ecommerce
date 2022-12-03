package org.ttn.ecommerce.dto.update;

import lombok.Data;
import org.ttn.ecommerce.validations.Password;
import org.ttn.ecommerce.validations.SellerPasswordMatcher;

import javax.validation.constraints.NotBlank;

@Data
@SellerPasswordMatcher
public class SellerPasswordDto {

    @NotBlank(message = "Password can not be empty")
    @Password
    private String password;

    @NotBlank(message = "Confirm Password can not be empty")
    private String confirmPassword;
}
