package org.ttn.ecommerce.dto.responseDto.categoryResponseDto;

import lombok.Data;
import org.ttn.ecommerce.entities.category.Category;

import java.util.List;

@Data
public class CategoryResponseDto {

    private Long id;
    private String name;
    private List<SubCategoryResponseDto> parentCategories;
    private List<SubCategoryResponseDto> childCategories;
}
