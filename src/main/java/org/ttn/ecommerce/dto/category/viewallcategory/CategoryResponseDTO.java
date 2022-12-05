package org.ttn.ecommerce.dto.category.viewallcategory;

import lombok.Data;
import org.ttn.ecommerce.dto.responseDto.categoryResponseDto.MetadataResponseDTO;
import org.ttn.ecommerce.entity.category.Category;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class CategoryResponseDTO {
    private long id;
    private String name;
    private Category parent;
    private Set<ChildCategoryDTO> children = new HashSet<>();
    private List<MetadataResponseDTO> metadataList = new ArrayList<>();

}