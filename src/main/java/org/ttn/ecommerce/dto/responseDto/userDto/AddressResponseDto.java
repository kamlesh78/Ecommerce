package org.ttn.ecommerce.dto.responseDto.userDto;

import lombok.Data;
import org.ttn.ecommerce.entity.Address;

import java.util.Set;

@Data
public class AddressResponseDto {
    Set<Address> addressList;
}
