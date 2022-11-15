package org.ttn.ecommerce.dto.register;

import lombok.Data;

import javax.validation.constraints.*;

@Data
public class CustomerRegisterDto {


    private String firstName;
    private String lastName;
    private String middleName;

    @Pattern(regexp = "(^$|[0-9]{10})",message = "Phone number should be of 10 digits")
    private String contact;

    @Email(flags = Pattern.Flag.CASE_INSENSITIVE,message = "Email should be unique and valid")
    @NotBlank(message = "Email can not be empty")
    private String email;

    @NotBlank(message = "Password can not be empty")
    private String password;
    //  private String confirmPassword;


}
