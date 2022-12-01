package org.ttn.ecommerce.services.product;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.ttn.ecommerce.dto.product.ProductVariationDto;
import org.ttn.ecommerce.entities.category.Category;
import org.ttn.ecommerce.entities.category.CategoryMetadataFieldValue;
import org.ttn.ecommerce.entities.product.Product;
import org.ttn.ecommerce.entities.product.ProductVariation;
import org.ttn.ecommerce.repository.categoryRepository.CategoryMetaDataFieldValueRepository;
import org.ttn.ecommerce.repository.productRepository.ProductRepository;
import org.ttn.ecommerce.repository.productRepository.ProductVariationRepository;

import java.awt.color.ProfileDataException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductVariationService {

    private ProductVariationRepository productVariationRepository;
    private ProductRepository productRepository;
    private CategoryMetaDataFieldValueRepository categoryMetaDataFieldValueRepository;

    @Autowired
    public ProductVariationService(ProductVariationRepository productVariationRepository, ProductRepository productRepository, CategoryMetaDataFieldValueRepository categoryMetaDataFieldValueRepository) {
        this.productVariationRepository = productVariationRepository;
        this.productRepository = productRepository;
        this.categoryMetaDataFieldValueRepository = categoryMetaDataFieldValueRepository;
    }

    public ResponseEntity<?> createProductVariation(ProductVariation productVariationDto){

        System.out.println(productVariationDto.getPrice());
//        ProductVariation productVariation = new ProductVariation();
//        Product product =   productRepository.findById(productVariationDto.getProductId())
//                .orElseThrow(()-> new ProfileDataException("Product Not Found For This Id"));
//
//        if(!product.isActive() || product.isDeleted()){
//            return new ResponseEntity<>("Product is not Active Or Product is deleted", HttpStatus.BAD_REQUEST);
//        }
//
//        Category category = product.getCategory();
//        List<CategoryMetadataFieldValue> categoryMetadataFieldValueList=
//                categoryMetaDataFieldValueRepository.findByCategoryId(category.getId());
//        Map<Object,String> meta = new LinkedHashMap<>();
//
//        for(CategoryMetadataFieldValue categoryMetadataFieldValue : categoryMetadataFieldValueList) {
//            meta.put(categoryMetadataFieldValue.getCategoryMetaDataField().getName(),
//                    categoryMetadataFieldValue.getValue());
//        }
//
//        String metadata = productVariationDto.getMetaData();
//
//        JSONObject jsonObj = new JSONObject(metadata);
//        Iterator keys = jsonObj.keys();
//        while(keys.hasNext()){
//            String currentKey = (String)keys.next();
//
//            if (meta.get(currentKey) == null){
//                return new ResponseEntity<>("meta value mismatch",HttpStatus.BAD_REQUEST);
//            }
//            if (!meta.get(currentKey).contains(jsonObj.getString(currentKey))){
//                return new ResponseEntity<>("invalid value in meta field",HttpStatus.BAD_REQUEST);
//            }
//
//        }
//
//        productVariation.setProduct(product);
//        productVariation.setPrice(productVariationDto.getPrice());
//        productVariation.setMetadata(productVariation.getMetadata());
//        productVariation.setQuantityAvailable(productVariationDto.getQuantityAvailable());
//        productVariation.setActive(true);
//
//        productVariationRepository.save(productVariation);
        return new ResponseEntity<>("product variation created successfully",HttpStatus.CREATED);
    }
}
