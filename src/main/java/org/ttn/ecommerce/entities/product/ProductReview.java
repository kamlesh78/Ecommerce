package org.ttn.ecommerce.entities.product;

import org.ttn.ecommerce.entities.Customer;

import javax.persistence.*;

@Entity
public class ProductReview{

    @EmbeddedId
    private ProductReviewKey id;

    private String review;

    private int rating;

    @ManyToOne
    @MapsId("customerId")
    @JoinColumn(name="customer_user_id")
    private Customer customer;

    @ManyToOne
    @MapsId("productId")
    @JoinColumn(name="product_id")
    private Product product;
}
