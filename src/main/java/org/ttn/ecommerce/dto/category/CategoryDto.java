package org.ttn.ecommerce.dto.category;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryDto {
    String name;
    private Long parentId;
}
