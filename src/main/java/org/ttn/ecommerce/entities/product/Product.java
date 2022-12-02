package org.ttn.ecommerce.entities.product;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.ttn.ecommerce.entities.Seller;
import org.ttn.ecommerce.entities.category.Category;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@SQLDelete(sql = "UPDATE Product SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Product {

    @Id
    @SequenceGenerator(name="product_sequence",sequenceName = "product_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "product_sequence")
    private Long id;

    private String name;

    private String description;

    @JsonProperty
    private boolean isCancellable;

    @JsonProperty
    private boolean isReturnable;

    @JsonProperty
    private boolean isActive;

    private boolean isDeleted;

    private String brand;


    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "seller_user_id")
    private Seller seller;



    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="category_id")
    private Category category;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<ProductVariation> productVariations;

    @JsonIgnore
    @OneToMany(mappedBy = "product")
    private List<ProductReview> productReviews;

    public boolean isCancellable() {
        return isCancellable;
    }

    public boolean isReturnable() {
        return isReturnable;
    }

    public boolean isActive() {
        return isActive;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
}
