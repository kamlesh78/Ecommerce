package org.ttn.ecommerce.dto.category;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class CategoryMetaValueDto {

    Set<String>  values;

}
