package org.ttn.ecommerce.services.impl;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.ttn.ecommerce.dto.product.CategoryDto;
import org.ttn.ecommerce.dto.product.ProductVariationDto;
import org.ttn.ecommerce.dto.product.ProductResponseDto;
import org.ttn.ecommerce.dto.responseDto.ProductVariationResponseDto;
import org.ttn.ecommerce.dto.update.ProductVariationUpdateDto;
import org.ttn.ecommerce.entity.user.UserEntity;
import org.ttn.ecommerce.entity.category.Category;
import org.ttn.ecommerce.entity.category.CategoryMetadataFieldValue;
import org.ttn.ecommerce.entity.product.Product;
import org.ttn.ecommerce.entity.product.ProductVariation;
import org.ttn.ecommerce.exception.ProductNotFoundException;
import org.ttn.ecommerce.exception.UserNotFoundException;
import org.ttn.ecommerce.repository.UserRepository.UserRepository;
import org.ttn.ecommerce.repository.categoryRepository.CategoryMetaDataFieldValueRepository;
import org.ttn.ecommerce.repository.productRepository.ProductRepository;
import org.ttn.ecommerce.repository.productRepository.ProductVariationRepository;
import org.ttn.ecommerce.services.ProductVariationService;

import java.awt.color.ProfileDataException;
import java.util.*;

@Service
public class ProductVariationServiceImpl implements ProductVariationService {

    private ProductVariationRepository productVariationRepository;
    private ProductRepository productRepository;
    private CategoryMetaDataFieldValueRepository categoryMetaDataFieldValueRepository;
    private UserRepository userRepository;
    @Autowired
    public ProductVariationServiceImpl(ProductVariationRepository productVariationRepository, ProductRepository productRepository, CategoryMetaDataFieldValueRepository categoryMetaDataFieldValueRepository, UserRepository userRepository) {
        this.productVariationRepository = productVariationRepository;
        this.productRepository = productRepository;
        this.categoryMetaDataFieldValueRepository = categoryMetaDataFieldValueRepository;
        this.userRepository = userRepository;
    }

    @Override
    public ResponseEntity<?> createProductVariation(ProductVariationDto productVariationDto){


        ProductVariation productVariation = new ProductVariation();
        Product product =   productRepository.findById(productVariationDto.getProductId())
                .orElseThrow(()-> new ProfileDataException("Product Not Found For This Id"));

        if(!product.isActive() || product.isDeleted()){
            return new ResponseEntity<>("Product is not Active Or Product is deleted", HttpStatus.BAD_REQUEST);
        }

        Category category = product.getCategory();
        List<CategoryMetadataFieldValue> categoryMetadataFieldValueList=
                categoryMetaDataFieldValueRepository.findByCategoryId(category.getId());

     Map<Object,Set<String>> meta = new LinkedHashMap<>();

       for( CategoryMetadataFieldValue categoryMetadataFieldValue : categoryMetadataFieldValueList){
           System.out.println(categoryMetadataFieldValue.getId());
       }
        for(CategoryMetadataFieldValue categoryMetadataFieldValue : categoryMetadataFieldValueList) {
            String[] values =  categoryMetadataFieldValue.getValue().split(",");
            List<String> list = Arrays.asList(values);
            Set<String> listSet = new HashSet<>(list);

            meta.put(categoryMetadataFieldValue.getCategoryMetaDataField().getName(),
                   listSet);
        }




        String metadata = productVariationDto.getMetaData();


        JSONObject jsonObj = new JSONObject(metadata);
        Iterator keys = jsonObj.keys();

        while(keys.hasNext()){
            String currentKey = (String)keys.next();

            System.out.println("current Key" + currentKey);

            if (meta.get(currentKey) == null){
                return new ResponseEntity<>("metadata value mismatch",HttpStatus.BAD_REQUEST);
            }
            if (!meta.get(currentKey).contains(jsonObj.getString(currentKey))){

               return new ResponseEntity<>("invalid value in metadata field",HttpStatus.BAD_REQUEST);
            }


        }

        productVariation.setProduct(product);
        productVariation.setPrice(productVariationDto.getPrice());
        productVariation.setMetadata(jsonObj.toString());
        productVariation.setQuantityAvailable(productVariationDto.getQuantityAvailable());
        productVariation.setActive(true);

        productVariationRepository.save(productVariation);
        return new ResponseEntity<>("product variation created successfully",HttpStatus.CREATED);
    }

//    @Override
//    public ResponseEntity<?> updateProductVariation(ProductVariationDto productVariationDto) {
//        return null;
//    }




    @Override
    public ProductVariationResponseDto getProductVariation(Long productVariationId, String email) {

        ProductVariation productVariation= productVariationRepository.findById(productVariationId)
                .orElseThrow(()->new ProductNotFoundException("Product  Variation Not Found For Given ID"));

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(()-> new UserNotFoundException("USer Not Found"));

        if(!(userEntity.getId() == productVariation.getProduct().getSeller().getId())){

        }


        ProductVariationResponseDto productVariationDto=new ProductVariationResponseDto();
        productVariationDto.setId(productVariation.getId());
        productVariationDto.setPrice(productVariation.getPrice());
        productVariationDto.setActive(productVariation.getProduct().isActive());
        productVariationDto.setQuantityAvailable(productVariation.getQuantityAvailable());
        productVariationDto.setProduct(productVariation.getProduct());


        return productVariationDto;
     }

    @Override
    public List<ProductResponseDto> viewAllProductsOfSeller(String email) {

        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(()->new UserNotFoundException("User Not Found"));

        List<Product> productList = productRepository.findProductsById(userEntity.getId())
                .orElseThrow(()->new ProductNotFoundException("No Product Found For Current User"));

        List<ProductResponseDto> productResponseDtoList = new ArrayList<>();



        for(Product product : productList) {
            if (!product.isDeleted()) {


            ProductResponseDto productResponseDto = new ProductResponseDto();
            CategoryDto categoryDto = new CategoryDto();
            categoryDto.setId(product.getCategory().getId());
            categoryDto.setName(product.getCategory().getName());


            productResponseDto.setBrand(product.getBrand());
            productResponseDto.setActive(product.isActive());
            productResponseDto.setDeleted(product.isDeleted());
            productResponseDto.setId(product.getId());
            productResponseDto.setName(product.getName());
            productResponseDto.setDescription(product.getDescription());
            productResponseDto.setCancellable(product.isCancellable());
            productResponseDto.setReturnable(product.isReturnable());

            productResponseDto.setCategory(categoryDto);

            productResponseDtoList.add(productResponseDto);
        }
        }


        return productResponseDtoList;

    }

    @Override
    public ResponseEntity<?> viewProductVariationByProduct(Long productId) {

        Product product=productRepository.findById(productId)
                .orElseThrow(()->new ProductNotFoundException("Product Not Found For Given ID"));

        if(product.isDeleted()){
            return new ResponseEntity<>("Product IS Deleted",HttpStatus.BAD_REQUEST);
        }

        List<ProductVariation> productVariationList = product.getProductVariations();
        List<ProductVariationDto> productResponseDtoList = new ArrayList<>();

        for(ProductVariation productVariation:productVariationList){
            ProductVariationDto productVariationDto = new ProductVariationDto();
            productVariationDto.setProductId(productVariation.getId());
            productVariationDto.setMetaData(productVariation.getMetadata());
            productVariationDto.setPrice(productVariation.getPrice());
            productVariationDto.setQuantityAvailable(productVariation.getQuantityAvailable());

            productResponseDtoList.add(productVariationDto);
        }

        return new ResponseEntity<>(productResponseDtoList,HttpStatus.OK);
    }
}
