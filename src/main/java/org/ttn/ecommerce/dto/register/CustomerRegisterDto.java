package org.ttn.ecommerce.dto.register;

import lombok.Data;

import org.ttn.ecommerce.validations.CustomerPasswordMatcher;
import org.ttn.ecommerce.validations.Password;
import org.ttn.ecommerce.validations.UniqueEmail;
import org.ttn.ecommerce.validations.PhoneNumber;

import javax.validation.constraints.*;

@Data
@CustomerPasswordMatcher()
public class CustomerRegisterDto {


    @NotNull
    @NotBlank
    private String firstName;

    @NotNull
    @NotBlank
    private String lastName;

    private String middleName;


    @PhoneNumber
    private String contact;

    @UniqueEmail
    @NotNull
    @NotBlank
    private String email;

    @Password
    @NotNull
    @NotBlank
    private String password;

    private String confirmPassword;


}
