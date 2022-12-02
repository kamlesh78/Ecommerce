package org.ttn.ecommerce.dto.responseDto.userDto;

import lombok.Data;
import org.aspectj.apache.bcel.classfile.Module;
import org.ttn.ecommerce.entities.Address;

import javax.validation.constraints.NotNull;
import java.util.List;
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
