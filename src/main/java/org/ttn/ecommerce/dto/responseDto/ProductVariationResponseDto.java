package org.ttn.ecommerce.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.ttn.ecommerce.entity.product.Product;

@Data
public class ProductVariationResponseDto {

    private Long id;

    private int quantityAvailable;

    private double price;

    @JsonProperty
    private boolean isActive;

    private String primaryImageName;

    private Product product;


}
