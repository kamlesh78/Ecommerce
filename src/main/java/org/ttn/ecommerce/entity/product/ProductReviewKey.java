package org.ttn.ecommerce.entity.product;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class ProductReviewKey implements Serializable {

    @Column(name = "CUSTOMER_ID")
    long customerId;

    @Column(name = "PRODUCT_ID")
    long productId;


}