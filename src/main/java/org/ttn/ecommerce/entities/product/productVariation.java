package org.ttn.ecommerce.entities.product;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

public class productVariation {
    @Id
    @SequenceGenerator(name="product_variation_sequence",sequenceName = "product_variation_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "product_variation_sequence")
    private Long id;

    private Integer quantityAvailable;

    private Double price;

    private boolean isActive;

    private String primaryImageName;

}
