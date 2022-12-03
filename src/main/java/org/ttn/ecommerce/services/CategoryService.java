package org.ttn.ecommerce.services;

import org.springframework.http.ResponseEntity;
import org.ttn.ecommerce.dto.category.CategoryDto;
import org.ttn.ecommerce.dto.category.CategoryMetaValueDto;
import org.ttn.ecommerce.dto.responseDto.categoryResponseDto.CategoryResponseDto;
import org.ttn.ecommerce.dto.responseDto.categoryResponseDto.MetaDataFieldResponse;
import org.ttn.ecommerce.dto.responseDto.categoryResponseDto.SellerCategoryResponseDTO;
import org.ttn.ecommerce.dto.responseDto.categoryResponseDto.SubCategoryResponseDto;
import org.ttn.ecommerce.entity.category.Category;
import org.ttn.ecommerce.entity.category.CategoryMetaDataField;

import java.util.List;

public interface CategoryService {
    public  static boolean check_subcategory(Category category, String name) {
        if (category.getName().equals(name)) return false;
        for (Category checkCategory : category.getSubCategory()) {
            boolean check = check_subcategory(checkCategory, name);
            if (check == false) return false;
        }
        return true;

    }

    String createMetaDataField(CategoryMetaDataField metaDataField);

    List<MetaDataFieldResponse> getMetaDataField();

    ResponseEntity<?> createMetaDataFieldValue(Long categoryId,
                                               Long metaDataFieldId,
                                               CategoryMetaValueDto categoryMetaValueDto);

    String updateMetaDataFieldValues(Long categoryId, Long metaDataFieldId, CategoryMetaValueDto categoryMetaValueDto);

    ResponseEntity<?> createCategory(CategoryDto categoryDto);

    CategoryResponseDto viewCategory(Long id);

    /*        View All Category           */
    List<SubCategoryResponseDto> viewAllCategory();

    /*                Update Category             */
    ResponseEntity<?> updateCategory(Long id, Category category);

    ResponseEntity<?> listCategoriesOfCustomer(Long id);

    List<SellerCategoryResponseDTO> viewSellerCategory();
}
