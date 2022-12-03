package org.ttn.ecommerce.entity.order;

import org.ttn.ecommerce.entity.product.ProductVariation;

import javax.persistence.*;


@Entity
public class OrderProduct {
    @Id
    @SequenceGenerator(name="order_product_sequence",sequenceName = "order_product_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "order_product_sequence")
    private Long id;

    private int quantity;
    private double price;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name="product_variation_id")
    private ProductVariation productVariation;

    @OneToOne(mappedBy = "orderProduct")
    private OrderStatus orderStatus;
}
