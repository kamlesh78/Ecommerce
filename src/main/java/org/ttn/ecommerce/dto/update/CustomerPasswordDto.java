package org.ttn.ecommerce.dto.update;

import lombok.Data;
import org.ttn.ecommerce.validations.CustomerPasswordMatcher;
import org.ttn.ecommerce.validations.Password;

import javax.validation.constraints.NotBlank;

@Data
@CustomerPasswordMatcher
public class CustomerPasswordDto {

    @NotBlank(message = "Password can not be empty")
    @Password
    private String password;

    @NotBlank(message = "Confirm Password can not be empty")
    private String confirmPassword;
}
