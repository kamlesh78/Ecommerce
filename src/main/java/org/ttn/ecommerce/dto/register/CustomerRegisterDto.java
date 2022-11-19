package org.ttn.ecommerce.dto.register;

import lombok.Data;
import org.ttn.ecommerce.validations.ConfirmPassword;
import org.ttn.ecommerce.validations.Email;
import org.ttn.ecommerce.validations.Password;
import org.ttn.ecommerce.validations.PhoneNumber;

import javax.validation.constraints.*;

@Data
@ConfirmPassword
public class CustomerRegisterDto {


    private String firstName;
    private String lastName;
    private String middleName;

   // @Pattern(regexp = "(^$|[0-9]{10})",message = "Phone number should be of 10 digits")
    @NotNull
    @NotBlank

    @PhoneNumber
    private String contact;

    @Email
    @NotBlank(message = "Email can not be empty")
    private String email;

    @NotBlank(message = "Password can not be empty")
    @Password
    private String password;

    private String confirmPassword;


}
