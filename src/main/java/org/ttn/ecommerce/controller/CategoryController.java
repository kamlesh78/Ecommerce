package org.ttn.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.ttn.ecommerce.dto.category.CategoryDto;
import org.ttn.ecommerce.entities.category.CategoryMetaDataField;
import org.ttn.ecommerce.services.CategoryService;
import org.ttn.ecommerce.services.UserDaoService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    CategoryService categoryService;

    @Autowired
    UserDaoService userDaoService;

    @PostMapping("create/metadata-field")
    public String createMetaField(@RequestBody CategoryMetaDataField categoryMetaDataField){
        return categoryService.createMetaDataField(categoryMetaDataField);
    }

    @GetMapping("view/metadata-field")
    public ResponseEntity<?> viewMetaField(){

        return categoryService.getMetaDataField();
    }

    @PostMapping("add/category")
    public ResponseEntity<?> addCategory(@RequestBody CategoryDto categoryDto){

        return categoryService.createCategory(categoryDto);

    }
}
