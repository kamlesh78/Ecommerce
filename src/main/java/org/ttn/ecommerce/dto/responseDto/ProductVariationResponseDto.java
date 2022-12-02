package org.ttn.ecommerce.dto.responseDto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.ttn.ecommerce.entities.product.Product;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

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
