package org.ttn.ecommerce.dto.responseDto.categoryResponseDto;

import lombok.Data;
import org.ttn.ecommerce.entities.category.Category;

import java.util.List;

@Data
public class SellerCategoryResponseDTO {
   private Long id;
   private String name;
   private Category parent;
   private List<MetadataResponseDTO> metadata;
}