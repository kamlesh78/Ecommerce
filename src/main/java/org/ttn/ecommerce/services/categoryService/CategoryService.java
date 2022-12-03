package org.ttn.ecommerce.services.categoryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.ttn.ecommerce.dto.category.CategoryDto;
import org.ttn.ecommerce.dto.category.CategoryMetaValueDto;
import org.ttn.ecommerce.dto.responseDto.categoryResponseDto.*;
import org.ttn.ecommerce.entities.category.Category;
import org.ttn.ecommerce.entities.category.CategoryMetaDataField;
import org.ttn.ecommerce.entities.category.CategoryMetadataFieldValue;
import org.ttn.ecommerce.entities.category.MetaDataValueId;
import org.ttn.ecommerce.exception.CategoryNotFoundException;
import org.ttn.ecommerce.repository.categoryRepository.CategoryMetaDataFieldRepository;
import org.ttn.ecommerce.repository.categoryRepository.CategoryMetaDataFieldValueRepository;
import org.ttn.ecommerce.repository.categoryRepository.CategoryRepository;
import org.ttn.ecommerce.repository.productRepository.ProductRepository;

import java.util.ArrayList;
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

    public static  boolean check_subcategory(Category category,String name){
        if(category.getName().equals(name)) return false;
        for(Category checkCategory : category.getSubCategory()){
            boolean check = check_subcategory(checkCategory,name);
            if(check == false) return false;        }
        return true;

    }
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

    public List<MetaDataFieldResponse> getMetaDataField() {

        List<CategoryMetaDataField> categoryMetaDataFields= categoryMetaDataFieldRepository.findAll();
        List<MetaDataFieldResponse> metaList= new ArrayList<>();
        for(CategoryMetaDataField metaDataField : categoryMetaDataFields){

            MetaDataFieldResponse metaDataFieldResponse = new MetaDataFieldResponse();
            metaDataFieldResponse.setId(metaDataField.getId());
            metaDataFieldResponse.setName(metaDataField.getName());

            metaList.add(metaDataFieldResponse);
        }
        return metaList;

    }


    public ResponseEntity<?> createMetaDataFieldValue(Long categoryId,
                                                      Long metaDataFieldId,
                                                      CategoryMetaValueDto categoryMetaValueDto) {

        System.out.println(categoryId + " "+ metaDataFieldId);
        Category category = categoryRepository.findById(categoryId).
                orElseThrow(()-> new CategoryNotFoundException("Category Not Found"));

        CategoryMetaDataField categoryMetaDataField = categoryMetaDataFieldRepository.findById(metaDataFieldId).
                orElseThrow(()-> new CategoryNotFoundException("Category MetaData Field Not Found"));

        if(categoryMetaValueDto.getValues().size()<1){
            return new ResponseEntity<>("Category MetaDataFieldValue Should Have At least One Value ",HttpStatus.BAD_REQUEST);
        }
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



    /**             Update MetaDataField Values             */
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


    /**
     *  View Category
     *  */
    public CategoryResponseDto viewCategory(Long id) {

        Category category = categoryRepository.findById(id)
                .orElseThrow(()-> new CategoryNotFoundException("Category Not Found FOR ID " + id));

        CategoryResponseDto categoryResponseDto = new CategoryResponseDto();
        categoryResponseDto.setId(category.getId());
        categoryResponseDto.setName(category.getName());

        List<Category> subCategory = category.getSubCategory();

        SubCategoryResponseDto temp =new SubCategoryResponseDto();
        List<SubCategoryResponseDto> childCategoryList = new ArrayList<>();
        List<SubCategoryResponseDto> parentCategoryList = new ArrayList<>();


        /**
         *      Get All Child Categories
         */
        for(Category childCategory : subCategory){
            temp.setId(childCategory.getId());
            temp.setName(childCategory.getName());
            childCategoryList.add(temp);
        }
        categoryResponseDto.setChildCategories(childCategoryList);

        /**
         *          Get Parent Categories
         */
        ;

        Category parentCategory= category.getParentCategory();
        while(parentCategory!=null){
            SubCategoryResponseDto parentDto =new SubCategoryResponseDto();
            parentDto.setId(parentCategory.getId());
            parentDto.setName(parentCategory.getName());
            parentCategoryList.add(parentDto);
            parentCategory=parentCategory.getParentCategory();

        }
        categoryResponseDto.setParentCategories(parentCategoryList);
        return categoryResponseDto;
    }


    /*        View All Category           */
    public List<SubCategoryResponseDto> viewAllCategory(){

        List<Category> categoryList = categoryRepository.findAll();

        /*      List of Response DTO         */

        List<SubCategoryResponseDto> subCategoryResponseDtoList = new ArrayList<>();
        for (Category category : categoryList){
            SubCategoryResponseDto subCategoryResponseDto = new SubCategoryResponseDto();
            subCategoryResponseDto.setId(category.getId());
            subCategoryResponseDto.setName(category.getName());

            subCategoryResponseDtoList.add(subCategoryResponseDto);
        }

        return subCategoryResponseDtoList;

    }

    /*                Update Category             */
    public ResponseEntity<?> updateCategory(Long id, Category category) {

        Category category1 = categoryRepository.findById(id)
                .orElseThrow(()->new CategoryNotFoundException(
                        "Category Not Found For Id "+ id
                ));

        /**
         *  Check If Name Is Unique From Current Category Till Parent Category
         */

        Category parentCheck = category1.getParentCategory();
        while(parentCheck !=null){
            if(parentCheck.getName().equals(category.getName())){
                return new ResponseEntity<>("Category Name Already Exists",HttpStatus.BAD_REQUEST);
            }
            parentCheck = parentCheck.getParentCategory();
        }


        List<Category> subCategoryList = category1.getParentCategory().getSubCategory();

        /**
         *      Check if category name is unique along breadth/depth
         */
        Category parentCheck2 = category1.getParentCategory();
        if(check_subcategory(parentCheck2,category.getName())){
            category1.setName(category.getName());
            categoryRepository.save(category1);
            return  new ResponseEntity<>("Category Updated Successfully",HttpStatus.OK);

        }else{
            return new ResponseEntity<>("Category Name Already Used By SubCategories Please Use Unique Name",HttpStatus.BAD_REQUEST);
        }


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


    /**
     *  List of all leaf node categories along with their metadata fields,
     *  field's possible values and parent node chain details
     */

    public List<SellerCategoryResponseDTO> viewSellerCategory(){
        List<Category> categoryList = categoryRepository.findAll();
        List<SellerCategoryResponseDTO> resultList = new ArrayList<>();

        for(Category category: categoryList){
            if(category.getSubCategory().isEmpty()){

                List<CategoryMetadataFieldValue> metadataList =
                        categoryMetaDataFieldValueRepository.findByCategoryId(category.getId());

                SellerCategoryResponseDTO sellerResponse = new SellerCategoryResponseDTO();
                sellerResponse.setId(category.getId());
                sellerResponse.setName(category.getName());
                sellerResponse.setParent(category.getParentCategory());
                List<MetadataResponseDTO> metaList = new ArrayList<>();
                for (CategoryMetadataFieldValue metadata: metadataList){
                    MetadataResponseDTO metadataResponseDTO = new MetadataResponseDTO();
                    metadataResponseDTO.setMetadataId(metadata.getCategoryMetaDataField().getId());
                    metadataResponseDTO.setFieldName(metadata.getCategoryMetaDataField().getName());
                    metadataResponseDTO.setPossibleValues(metadata.getValue());
                    metaList.add(metadataResponseDTO);
                }
                sellerResponse.setMetadata(metaList);
                resultList.add(sellerResponse);
            }
        }
        return resultList;
    }
}
