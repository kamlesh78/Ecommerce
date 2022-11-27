package org.ttn.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttn.ecommerce.dto.category.CategoryDto;
import org.ttn.ecommerce.entities.category.Category;
import org.ttn.ecommerce.entities.category.CategoryMetaDataField;
import org.ttn.ecommerce.exception.CategoryNotFoundException;
import org.ttn.ecommerce.repository.CategoryMetaDataFieldRepository;
import org.ttn.ecommerce.repository.CategoryRepository;
import org.ttn.ecommerce.repository.ProductRepository;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class CategoryService {


//    @Autowired
//    CategoryRepository categoryRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    CategoryMetaDataFieldRepository categoryMetaDataFieldRepository;

    @Autowired
    ProductRepository productRepository;

    public String createMetaDataField(CategoryMetaDataField metaDataField) {
        String name = metaDataField.getName();
        Optional<CategoryMetaDataField> categoryMetaDataField = categoryMetaDataFieldRepository.findByName(name);
        if(categoryMetaDataField.isPresent()){
            return "MetaData Field Already Exists! MetaData Filed Should Be Unique.";
        }else{

            categoryMetaDataFieldRepository.save(metaDataField);
            return "New MetaData Field Created.";
        }
    }

    public ResponseEntity<?> getMetaDataField() {

        List<CategoryMetaDataField> categoryMetaDataFields= categoryMetaDataFieldRepository.findAll();
        return new ResponseEntity<>(categoryMetaDataFields, HttpStatus.OK);

    }

    public ResponseEntity<?> createCategory(CategoryDto categoryDto) {

        /*
            Validation 1 : Parent category should not be associated with any product
            Validation 2 : Category name should be unique
         */


        /*                 Validation 1                       */
        String categoryName =categoryDto.getName();
        Long  parentCategoryId= categoryDto.getParentId();
        Category parent=null;
        if(parentCategoryId !=null){
             parent = categoryRepository.findById(parentCategoryId)
                    .orElseThrow(()->new CategoryNotFoundException("Parent Category Id Is Not Valid!"));

            /*      Check if Category contains Product       */
            if(productRepository.existsByCategoryId(parent.getId())>0){
                return new ResponseEntity<>("Parent Category Should Not Associated With Any Product",HttpStatus.BAD_REQUEST);
            }

        }

        /*                Validation 2                   */
        Optional<Category> category = categoryRepository.findByName(categoryName);
        if(category.isPresent()){
            return new ResponseEntity<>("Category Already Exists",HttpStatus.BAD_REQUEST);
        }

        Category category1 = new Category();
        category1.setName(categoryDto.getName());
        category1.setParentCategory(parent);

        categoryRepository.save(category1);

        return new ResponseEntity<>("Category Created",HttpStatus.CREATED);

    }
}
