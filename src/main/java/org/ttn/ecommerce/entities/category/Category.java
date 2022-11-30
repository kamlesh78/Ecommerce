package org.ttn.ecommerce.entities.category;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ttn.ecommerce.entities.product.Product;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name="category")
public class Category {

    @Id
    @SequenceGenerator(name="category_sequence",sequenceName = "category_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "category_sequence")
    private Long id;

    String name;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="parent_category_id")
    private Category parentCategory;


    @JsonIgnore
    @OneToMany(mappedBy = "parentCategory")
    private List<Category> subCategory;

    @OneToMany(mappedBy = "category",cascade = CascadeType.ALL)
    private List<Product> products;

    @OneToMany(mappedBy = "category")
    private List<CategoryMetadataFieldValue> categoryMetadataFieldValues;




}
