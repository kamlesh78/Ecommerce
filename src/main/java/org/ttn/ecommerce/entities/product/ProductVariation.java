package org.ttn.ecommerce.entities.product;

import lombok.Data;
import org.ttn.ecommerce.entities.order.OrderProduct;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name="product_variation")
public class ProductVariation {

    @Id
    @SequenceGenerator(name="product_variation_sequence",sequenceName = "product_variation_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "product_variation_sequence")
    private Long id;

    private int quantityAvailable;

    private double price;

    private boolean isActive;

    private String primaryImageName;


    @Column(columnDefinition = "JSON")
    private String metadata;

    @NotNull
    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product;

    @OneToMany(mappedBy = "order")
    List<OrderProduct> orderProducts;

}
