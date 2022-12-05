package org.ttn.ecommerce.entity.product;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.ttn.ecommerce.auditing.Auditable;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "product_variation")
public class ProductVariation extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "products_variation_sequence", sequenceName = "products_variation_sequence", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "products_variation_sequence")
    private Long id;

    private int quantityAvailable;

    private double price;

    @JsonProperty
    private boolean isActive;

    private String primaryImageName;

    @JsonProperty
    @Column(columnDefinition = "JSON")
    private String metadata;


    @NotNull
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;


}
