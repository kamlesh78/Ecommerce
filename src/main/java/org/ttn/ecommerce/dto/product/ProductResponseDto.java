package org.ttn.ecommerce.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.ttn.ecommerce.dto.responseDto.categoryResponseDto.CategoryResponseDto;
import org.ttn.ecommerce.entities.category.Category;
import org.ttn.ecommerce.entities.product.Product;

import java.util.List;

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
