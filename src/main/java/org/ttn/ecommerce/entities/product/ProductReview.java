package org.ttn.ecommerce.entities.product;

import org.ttn.ecommerce.entities.Customer;

import javax.persistence.*;

@Entity
public class ProductReview{

    @Id
    @SequenceGenerator(name="category_sequence",sequenceName = "category_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "category_sequence")
    private Long id;

    private String review;

    private int rating;
    @ManyToOne
    @JoinColumn(name="customer_user_id")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;
}
