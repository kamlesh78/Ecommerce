package org.ttn.ecommerce.dto.update;

import lombok.Data;
import org.ttn.ecommerce.validations.CustomerPasswordMatcher;
import org.ttn.ecommerce.validations.Password;
import org.ttn.ecommerce.validations.PhoneNumber;
import org.ttn.ecommerce.validations.UniqueEmail;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@CustomerPasswordMatcher()
public class CustomerUpdateDto {



    private String firstName;


    private String lastName;

    private String middleName;



    private String contact;








}
