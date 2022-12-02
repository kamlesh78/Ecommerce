package org.ttn.ecommerce.dto.responseDto.userDto;

import lombok.Data;

@Data
public class CustomerResponseDto {

    private Long id;
    private String firstName;
    private String lastName;
    private boolean isActive;
    private String contact;
    private String profileImageUrl;
}
