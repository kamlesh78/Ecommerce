package org.ttn.ecommerce.dto.update;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductVariationUpdateDto {

    private String metaData;

    private int quantityAvailable;

    private double price;
}
