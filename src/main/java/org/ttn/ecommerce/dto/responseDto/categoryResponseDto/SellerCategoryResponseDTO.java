package org.ttn.ecommerce.dto.responseDto.categoryResponseDto;

import lombok.Data;
import org.ttn.ecommerce.dto.category.CategoryDto;
import org.ttn.ecommerce.entity.category.Category;

import java.util.List;

@Data
public class SellerCategoryResponseDTO {
   private Long id;
   private String name;
   private CategoryDto parent;
   private List<MetadataResponseDTO> metadata;
}