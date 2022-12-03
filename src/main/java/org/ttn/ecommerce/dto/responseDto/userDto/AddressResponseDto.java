package org.ttn.ecommerce.dto.responseDto.userDto;

import lombok.Data;
import org.ttn.ecommerce.entity.user.Address;

import java.util.Set;

@Data
public class AddressResponseDto {
    Set<Address> addressList;
}
