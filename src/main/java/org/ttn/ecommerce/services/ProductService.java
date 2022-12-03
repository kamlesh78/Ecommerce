package org.ttn.ecommerce.services;

import org.springframework.http.ResponseEntity;
import org.ttn.ecommerce.dto.product.ProductResponseDto;
import org.ttn.ecommerce.entity.product.Product;

import java.util.List;

public interface ProductService {
    ResponseEntity<?> addProduct(Product product, Long categoryId, String email);

    ResponseEntity<?> viewProductById(Long id, String email) throws Exception;

    String deleteProduct(String email, Long id);

    String updateProduct(String email, Long productId, Product productUpdate);

    String activateProduct(long productId);

    String deactivateProduct(Long id);

    //    public List<ProductResponseDTO> adminViewAllProducts(){
    //
    //        List<Product> products = productRepository.findAll();
    //         if(products.isEmpty()){
    //            throw new BadRequestException(messageSource.getMessage("api.error.productNotFound",null,Locale.ENGLISH));
    //        }
    //
    //         List<ProductResponseDTO> productResponseDTOList= new ArrayList<>();
    //        for(Product product: products){
    //
    //            ProductResponseDTO productResponseDTO = new ProductResponseDTO();
    //
    //            productResponseDTO.setId(product.getId());
    //            productResponseDTO.setName(product.getName());
    //            productResponseDTO.setBrand(product.getBrand());
    //            productResponseDTO.setDescription(product.getDescription());
    //            productResponseDTO.setIsActive(product.isActive());
    //            productResponseDTO.setIsCancellable(product.isCancellable());
    //            productResponseDTO.setIsReturnable(product.isReturnable());
    //            productResponseDTO.setCategory(product.getCategory());
    //            productResponseDTOList.add(productResponseDTO);
    //        }
    //        return productResponseDTOList;
    //
    //    }
    ResponseEntity<?> customerViewProduct(Long productId);

    ResponseEntity<List<List<ProductResponseDto>>> viewAllProductOfProduct(Long categoryId);

    List<ProductResponseDto> retrieveProducts(Long id);
}
