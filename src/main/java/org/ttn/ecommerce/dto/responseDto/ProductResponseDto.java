package org.ttn.ecommerce.dto.responseDto;

import lombok.Data;
import org.ttn.ecommerce.entities.category.Category;
import org.ttn.ecommerce.entities.product.Product;

import java.util.List;

@Data
public class ProductResponseDto {

    Product product;
    Category category;
}
