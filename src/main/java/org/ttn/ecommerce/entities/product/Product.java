package org.ttn.ecommerce.entities.product;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ttn.ecommerce.entities.Seller;
import org.ttn.ecommerce.entities.category.Category;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Product {

    @Id
    @SequenceGenerator(name="product_sequence",sequenceName = "product_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "product_sequence")
    private Long id;

    private String name;

    private String description;

    private boolean isCancellable;

    private boolean isReturnable;

    private boolean isActive;

    private boolean isDeleted;

    private String brand;


    @ManyToOne
    @JoinColumn(name = "seller_user_id")
    private Seller seller;

    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @OneToMany(mappedBy = "product")
    private List<ProductVariation> productVariations;

    @OneToMany(mappedBy = "product")
    private List<ProductReview> productReviews;



}
