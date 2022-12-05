package org.ttn.ecommerce.entity.category;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.ttn.ecommerce.auditing.Auditable;
import org.ttn.ecommerce.entity.product.Product;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "category")
public class Category extends Auditable<String> {

    String name;
    @Id
    @SequenceGenerator(name = "category_sequence", sequenceName = "category_sequence", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_sequence")
    private Long id;
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "parent_category_id")
    private Category parentCategory;

    @JsonBackReference
    @OneToMany(mappedBy = "parentCategory")
    private List<Category> subCategory;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<Product> products;

    @OneToMany(mappedBy = "category")
    private List<CategoryMetadataFieldValue> categoryMetadataFieldValues;


}
