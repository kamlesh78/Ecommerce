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


    ResponseEntity<?> customerViewProduct(Long productId);

    ResponseEntity<List<List<ProductResponseDto>>> viewAllProductOfProduct(Long categoryId);

    List<ProductResponseDto> retrieveProducts(Long id);

    ResponseEntity<?> adminViewProductById(Long id, String email);

    public ResponseEntity<?> adminViewAllProducts();
}
