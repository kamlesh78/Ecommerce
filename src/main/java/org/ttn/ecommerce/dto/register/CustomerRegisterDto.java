package org.ttn.ecommerce.dto.register;

import lombok.Data;

import org.ttn.ecommerce.validations.CustomerPasswordMatcher;
import org.ttn.ecommerce.validations.Email;
import org.ttn.ecommerce.validations.Password;
import org.ttn.ecommerce.validations.PhoneNumber;

import javax.validation.constraints.*;

@Data

public class CustomerRegisterDto {


    private String firstName;
    private String lastName;
    private String middleName;

    @PhoneNumber
    @NotNull
    @NotBlank
    private String contact;


    @NotBlank(message = "Email can not be empty")
    @Email
    private String email;

    @NotBlank(message = "Password can not be empty")
    @Password
    private String password;

    private String confirmPassword;


}
