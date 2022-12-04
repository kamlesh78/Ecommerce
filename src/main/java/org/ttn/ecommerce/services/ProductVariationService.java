package org.ttn.ecommerce.services;

import org.springframework.http.ResponseEntity;
import org.ttn.ecommerce.dto.product.ProductResponseDto;
import org.ttn.ecommerce.dto.product.ProductVariationDto;
import org.ttn.ecommerce.dto.responseDto.ProductVariationResponseDto;

import java.util.List;

public interface ProductVariationService {
    ResponseEntity<?> createProductVariation(ProductVariationDto productVariationDto);

    ResponseEntity<?> updateProductVariation(ProductVariationDto productVariationDto);

    ProductVariationResponseDto getProductVariation(Long productVariationId, String email);

    List<ProductResponseDto> viewAllProductsOfSeller(String email);

    ResponseEntity<?> viewProductVariationByProduct(Long productId);
}
