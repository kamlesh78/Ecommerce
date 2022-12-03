package org.ttn.ecommerce.dto.responseDto.userDto;

import lombok.Data;
import org.ttn.ecommerce.entity.Address;

import java.util.Set;

@Data
public class SellerResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private String profileImageUrl;
    private String gst;
    private String companyContact;
    private String companyName;

    private Set<Address> address;
}
