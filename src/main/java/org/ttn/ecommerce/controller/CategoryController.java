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
import org.ttn.ecommerce.entity.category.Category;
import org.ttn.ecommerce.entity.category.CategoryMetaDataField;
import org.ttn.ecommerce.services.CategoryService;
import org.ttn.ecommerce.services.UserService;
import org.ttn.ecommerce.services.impl.UserServiceImpl;
import org.ttn.ecommerce.services.impl.CategoryServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserService userDaoService;



    /**
     *     @Problem         :  Create MetaData Field
     *     @Constriant      :  Field Name Should be Unique
     */
    @PostMapping("create/metadata-field")
    public String createMetaField(@RequestBody CategoryMetaDataField categoryMetaDataField) {

        return categoryService.createMetaDataField(categoryMetaDataField);
    }


    /**
     *     @OutPut : Return All MetaData Fields
     */
    @GetMapping("view/metadata-field")
    public List<MetaDataFieldResponse> viewMetaField() {

        return categoryService.getMetaDataField();
    }



    /**
     *     @Problem         :  Create MetaData Field
     *     @Constriant      :  Field Name Should be Unique
     */
    @PostMapping("create/metadata-field-value/{categoryId}/{metaDataFieldId}")
    public ResponseEntity<?> createMetaDataFieldValues(@PathVariable("categoryId") long categoryId,
                                                       @PathVariable("metaDataFieldId") Long metaDataFieldId,
                                                       @RequestBody CategoryMetaValueDto categoryMetaValueDto) {
        return categoryService.createMetaDataFieldValue(categoryId, metaDataFieldId, categoryMetaValueDto);
    }


    /**
     *     @Constriant      :  Value Should be Unique for Category and MetaData Field
     *                      :  
     */
    @PutMapping("/update/metadata-field-values/{categoryId}/{metaDataFieldId}")
    public String updateMetaDataFieldValues(
            @PathVariable("categoryId") Long categoryId,
            @PathVariable("metaDataFieldId") Long metaDataFieldId,
            @RequestBody CategoryMetaValueDto categoryMetaValueDto
    ) {
        return categoryService.updateMetaDataFieldValues(categoryId, metaDataFieldId, categoryMetaValueDto);
    }



    /**
     *     @Problem     : Create New Category
     *     @Constriant  : Category name should be unique at root level and along breadth/depth in a tree
     */
    @PostMapping("create/category")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDto categoryDto) {

        return categoryService.createCategory(categoryDto);
    }


    /**
     *     @Constraint  : Category Id Should Be Valid
     *     @Output      : Category Details With Parent Categories UpTo root level and immediate children categories, and associated fields
     */
    @GetMapping("view/category/{id}")
    public CategoryResponseDto viewCategory(@PathVariable("id") Long id) {

        return categoryService.viewCategory(id);
    }


    /**
     *     @Output      : List of all categories, with each individual category's detail as stated above
     */
    @GetMapping("view/all-categories")
    public List<SubCategoryResponseDto> viewAllCategories() {

        return categoryService.viewAllCategory();
    }

    /**
     *     @Constraint    :  Category name should be unique at root level and along breadth/depth in a tree"
     *     @Output        :  If Category Name Is Valid It Should be Updated
     */
    @PutMapping("update/category/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable("id") Long id, @RequestBody Category category) {

        return categoryService.updateCategory(id, category);
    }


    @GetMapping("view/seller-categories")
    public ResponseEntity<List<SellerCategoryResponseDTO>> viewSellerCategory() {
        List<SellerCategoryResponseDTO> responseList = categoryService.viewSellerCategory();
        return new ResponseEntity<>(responseList, HttpStatus.OK);
    }


    /**
     *      @Consumer    <<Customer>>
     *      @param       id
     */
    @GetMapping(value = {"customer/view/categories","customer/view/categories/{id}"})
    public ResponseEntity<?> viewAllCategories(@PathVariable(value = "id",required = false)Long id){

        return  categoryService.listCategoriesOfCustomer(id);
    }
}
