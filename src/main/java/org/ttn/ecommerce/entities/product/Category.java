package org.ttn.ecommerce.entities.product;

import org.springframework.boot.autoconfigure.kafka.KafkaProperties;

import javax.persistence.*;
import java.util.List;

public class Category {

    @Id
    @SequenceGenerator(name="category_sequence",sequenceName = "category_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "category_sequence")
    private Long id;

    String name;

    @ManyToOne
    @JoinColumn(name="parent")
    private Category parentCategory;

    @OneToMany()
    private List<Category> subCategory;

    @OneToMany
    private List<Product> products;

    @OneToMany
    private List<CategoryMetaDataFieldRelation> metaFieldValues;




}
