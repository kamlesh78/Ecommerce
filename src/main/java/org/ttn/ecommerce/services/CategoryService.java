package org.ttn.ecommerce.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttn.ecommerce.dto.category.CategoryDto;
import org.ttn.ecommerce.dto.category.CategoryMetaValueDto;
import org.ttn.ecommerce.entities.category.Category;
import org.ttn.ecommerce.entities.category.CategoryMetaDataField;
import org.ttn.ecommerce.entities.category.CategoryMetadataFieldValue;
import org.ttn.ecommerce.entities.category.MetaDataValueId;
import org.ttn.ecommerce.exception.CategoryNotFoundException;
import org.ttn.ecommerce.repository.categoryRepository.CategoryMetaDataFieldRepository;
import org.ttn.ecommerce.repository.categoryRepository.CategoryMetaDataFieldValueRepository;
import org.ttn.ecommerce.repository.categoryRepository.CategoryRepository;
import org.ttn.ecommerce.repository.productRepository.ProductRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    CategoryMetaDataFieldValueRepository categoryMetaDataFieldValueRepository;

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


    public ResponseEntity<?> createMetaDataFieldValue(Long categoryId,
                                                      Long metaDataFieldId,
                                                      CategoryMetaValueDto categoryMetaValueDto) {

        System.out.println(categoryId + " "+ metaDataFieldId);
        Category category = categoryRepository.findById(categoryId).
                orElseThrow(()-> new CategoryNotFoundException("Category Not Found"));

        CategoryMetaDataField categoryMetaDataField = categoryMetaDataFieldRepository.findById(metaDataFieldId).
                orElseThrow(()-> new CategoryNotFoundException("Category MetaData Field Not Found"));

        System.out.println(category.getName() + " " + categoryMetaDataField.getName());
        CategoryMetadataFieldValue categoryMetadataFieldValue = new CategoryMetadataFieldValue();

        String values = categoryMetaValueDto.getValues()
                .stream().collect(Collectors.joining(","));

        System.out.println(values);


        categoryMetadataFieldValue.setCategory(category);
        categoryMetadataFieldValue.setCategoryMetaDataField(categoryMetaDataField);
        categoryMetadataFieldValue.setValue(values);

        categoryMetaDataFieldValueRepository.save(categoryMetadataFieldValue);

        return new ResponseEntity<>("Category MetaDataValue Created",HttpStatus.CREATED);

    }



    /*          Update MetaDataField Values             */
    public String updateMetaDataFieldValues(Long categoryId, Long metaDataFieldId, CategoryMetaValueDto categoryMetaValueDto) {

        Category category = categoryRepository.findById(categoryId).
                orElseThrow(()-> new CategoryNotFoundException("Category Not Found"));

        CategoryMetaDataField categoryMetaDataField = categoryMetaDataFieldRepository.findById(metaDataFieldId).
                orElseThrow(()-> new CategoryNotFoundException("Category MetaData Field Not Found"));

        String values = categoryMetaValueDto.getValues()
                .stream().collect(Collectors.joining(","));

        MetaDataValueId metaDataValueId = new MetaDataValueId(categoryId,metaDataFieldId);

        CategoryMetadataFieldValue categoryMetadataFieldValue = categoryMetaDataFieldValueRepository.findById(metaDataValueId)
                .orElseThrow(()->new CategoryNotFoundException(
                   "Category MetaData Not Found For CategoryId -> "
                +categoryId + "And MetaDataField Value ID -> " + metaDataFieldId));

        categoryMetadataFieldValue.setValue(values);

        categoryMetaDataFieldValueRepository.save(categoryMetadataFieldValue);

        return "Category MetaData Field Value Updated !";
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


    /*           View Category            */
    public ResponseEntity<?> viewCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new CategoryNotFoundException("Category Not Found FOR ID " + id));

        return new ResponseEntity<>(category,HttpStatus.OK);
    }


    /*        View All Category           */
    public ResponseEntity<?> viewAllCategory(){

        List<Category> categoryList = categoryRepository.findAll();
        return new ResponseEntity<>(categoryList,HttpStatus.OK);
    }

    /*                Update Category             */
    public ResponseEntity<?> updateCategory(Long id, Category category) {

        Category category1 = categoryRepository.findById(id)
                .orElseThrow(()->new CategoryNotFoundException(
                        "Category Not Found For Id "+ id
                ));

        Optional<Category> category2 = categoryRepository.findByName(category.getName());
        if(category2.isPresent()){
            return new ResponseEntity<>("Category Name Already Exists",HttpStatus.BAD_REQUEST);
        }

        category1.setName(category.getName());
        categoryRepository.save(category1);

        return  new ResponseEntity<>("Category Updated Successfully",HttpStatus.OK);

    }

    public ResponseEntity<?> listCategoriesOfCustomer(Long id) {

        Category category=null;
        if(id!=null){

            category = categoryRepository.findById(id).orElseThrow(()->new CategoryNotFoundException("Category Not Found"));

            List<Category> categoryList = category.getSubCategory();
            return new ResponseEntity<>(categoryList,HttpStatus.OK);
        }else{

            List<Category> categoryList = categoryRepository.findAllRootCategories();
            return new ResponseEntity<>(categoryList,HttpStatus.OK);
        }

    }
}
