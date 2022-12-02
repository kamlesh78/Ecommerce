package org.ttn.ecommerce.dto.product.responseDto.userDto;

import lombok.Data;
import org.ttn.ecommerce.entities.Address;

import java.util.List;
import java.util.Set;

@Data
public class AddressResponseDto {
    Set<Address> addressList;
}
