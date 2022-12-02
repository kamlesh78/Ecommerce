package org.ttn.ecommerce.dto.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class ProductVariationDto {

    @NotNull(message = "Product Id Can Not Be Null")
    private Long productId;

    @JsonProperty(value = "metaData")
    @NotNull(message = "MetaData Can Not Be Null")
    private String metaData;

    @Min(value = 0, message = "Quantity should be greater than 0")
    @NotNull(message = "Quantity Can Not Null")
    private int quantityAvailable;

    @Min(value = 0, message = "Price should be greater than 0")
    @NotNull(message ="Price Can Not Be Null")
    private double price;

}
