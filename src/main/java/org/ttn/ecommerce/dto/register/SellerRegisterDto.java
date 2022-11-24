package org.ttn.ecommerce.dto.register;

import lombok.Data;
import org.ttn.ecommerce.validations.Gst;
import org.ttn.ecommerce.validations.Password;
import org.ttn.ecommerce.validations.SellerPasswordMatcher;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;


@Data
@SellerPasswordMatcher()
public class SellerRegisterDto {

    private String firstName;
    private String lastName;
    private String middleName;

    @Password
    @NotBlank(message = "Phone number can not be empty")
    private String companyContact;

    @Email
    @NotBlank(message = "Email can not be empty")
    private String email;

    @Password
    @NotBlank(message = "Password can not be empty")
    private String password;

    @Password
    private String confirmPassword;

    @Gst
    private String gstNumber;

    @NotBlank(message = "Company name can not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$",flags = Pattern.Flag.CASE_INSENSITIVE,message = "Company name should be unique")
    private String companyName;






}
