package org.ttn.ecommerce.dto.register;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Data
public class SellerRegisterDto {

    private String firstName;
    private String lastName;
    private String middleName;

    @Pattern(regexp = "(^$|[0-9]{10})",message = "Phone number should be of 10 digits")
    @NotBlank(message = "Phone number can not be empty")
    private String companyContact;

    @Email(flags = Pattern.Flag.CASE_INSENSITIVE,message = "Email should be unique and valid")
    @NotBlank(message = "Email can not be empty")
    private String email;

    @NotBlank(message = "Password can not be empty")
    private String password;

    @NotBlank(message = "Company name can not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$",flags = Pattern.Flag.CASE_INSENSITIVE,message = "Company name should be unique")
    private String companyName;

    private String gstNumber;


    //  private String confirmPassword;

}
