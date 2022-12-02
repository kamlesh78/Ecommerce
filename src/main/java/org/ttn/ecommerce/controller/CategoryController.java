package org.ttn.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ttn.ecommerce.dto.category.CategoryDto;
import org.ttn.ecommerce.dto.category.CategoryMetaValueDto;
import org.ttn.ecommerce.dto.responseDto.categoryResponseDto.CategoryResponseDto;
import org.ttn.ecommerce.dto.responseDto.categoryResponseDto.MetaDataFieldResponse;
import org.ttn.ecommerce.dto.responseDto.categoryResponseDto.SellerCategoryResponseDTO;
import org.ttn.ecommerce.dto.responseDto.categoryResponseDto.SubCategoryResponseDto;
import org.ttn.ecommerce.entities.category.Category;
import org.ttn.ecommerce.entities.category.CategoryMetaDataField;
import org.ttn.ecommerce.entities.category.CategoryMetadataFieldValue;
import org.ttn.ecommerce.repository.categoryRepository.CategoryRepository;
import org.ttn.ecommerce.services.categoryService.CategoryService;
import org.ttn.ecommerce.services.UserServiceImpl;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserServiceImpl userDaoService;


    /**
            MetaData For Category
     */

    @PostMapping("create/metadata-field")
    public String createMetaField(@RequestBody CategoryMetaDataField categoryMetaDataField) {

        return categoryService.createMetaDataField(categoryMetaDataField);
    }


    @GetMapping("view/metadata-field")
    public List<MetaDataFieldResponse> viewMetaField() {

        return categoryService.getMetaDataField();
    }


    @PostMapping("create/metadata-field-value/{categoryId}/{metaDataFieldId}")
    public ResponseEntity<?> createMetaDataFieldValues(@PathVariable("categoryId") long categoryId,
                                                       @PathVariable("metaDataFieldId") Long metaDataFieldId,
                                                       @RequestBody CategoryMetaValueDto categoryMetaValueDto) {
        return categoryService.createMetaDataFieldValue(categoryId, metaDataFieldId, categoryMetaValueDto);
    }


    @PutMapping("/update/metadata-field-values/{categoryId}/{metaDataFieldId}")
    public String updateMetaDataFieldValues(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("metaDataFieldId") Long metaDataFieldId,
            @RequestBody CategoryMetaValueDto categoryMetaValueDto
    ) {
        return categoryService.updateMetaDataFieldValues(categoryId, metaDataFieldId, categoryMetaValueDto);
    }


    /*
                Controller for Category
     */
    @PostMapping("create/category")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDto categoryDto) {

        return categoryService.createCategory(categoryDto);
    }

    @GetMapping("view/category/{id}")
    public CategoryResponseDto viewCategory(@PathVariable("id") Long id){


        return categoryService.viewCategory(id);
    }

    @GetMapping("view/all-categories")
    public List<SubCategoryResponseDto> viewAllCategories(){

        return categoryService.viewAllCategory();
    }

    @PutMapping("update/category/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id,@RequestBody Category category){

        return categoryService.updateCategory(id,category);
    }


    @GetMapping("seller")
    public ResponseEntity<List<SellerCategoryResponseDTO>> viewSellerCategory(){
        List<SellerCategoryResponseDTO> responseList = categoryService.viewSellerCategory();
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }

}
