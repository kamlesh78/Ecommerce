package org.ttn.ecommerce.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SellerUpdateDto {


    private String firstName;

    private String lastName;

    private String email;

    private String middleName;

    private String companyContact;

    private String companyName;
}
