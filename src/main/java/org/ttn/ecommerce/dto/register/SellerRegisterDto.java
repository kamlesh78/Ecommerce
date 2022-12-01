package org.ttn.ecommerce.dto.register;

import lombok.Data;
import org.ttn.ecommerce.validations.Gst;
import org.ttn.ecommerce.validations.Password;
import org.ttn.ecommerce.validations.PhoneNumber;
import org.ttn.ecommerce.validations.SellerPasswordMatcher;

import javax.validation.constraints.*;


@Data
@SellerPasswordMatcher()
public class SellerRegisterDto {

    @NotEmpty(message = "Please Provide First Name")
    private String firstName;

    @NotEmpty(message = "Please Provide Last Name")
    private String lastName;

    private String middleName;

    @PhoneNumber
    @NotEmpty(message = "Phone number can not be empty")
    private String companyContact;

    @Email
    @NotEmpty(message = "Email can not be empty")
    private String email;

    @Password
    @NotEmpty(message = "Password can not be empty")
    private String password;

    @Password
    @NotEmpty(message ="Confirm Password can not be Empty")
    private String confirmPassword;

    @NotEmpty(message = "Gst Number can not be empty")
    @Gst
    private String gstNumber;

    @NotEmpty(message = "Company name can not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9 ]*$",flags = Pattern.Flag.CASE_INSENSITIVE,message = "Company name should be unique")
    private String companyName;




}
