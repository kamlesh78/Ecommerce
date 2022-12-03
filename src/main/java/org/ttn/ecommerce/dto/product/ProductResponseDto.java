package org.ttn.ecommerce.dto.product;

import lombok.Data;

@Data
public class ProductResponseDto {

    private Long id;

    private String name;

    private String description;


    private boolean isCancellable;


    private boolean isReturnable;

    private boolean isActive;

    private boolean isDeleted;

    private String brand;

    CategoryDto category;
}
