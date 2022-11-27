package org.ttn.ecommerce.dto.category;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
public class CategoryMetaValueDto {

    Set<String>  values;

}
