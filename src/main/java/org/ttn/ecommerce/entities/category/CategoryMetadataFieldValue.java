package org.ttn.ecommerce.entities.category;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name="category_metadata_field_values")
public class CategoryMetadataFieldValue {

    @Id
    @SequenceGenerator(name="category_metadata_sequence",sequenceName = "category_metadata_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "category_metadata_sequence")
    private Long id;

    private String value;

    @ManyToOne
    @MapsId
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "category_metadata_field_id")
    private CategoryMetaDataField categoryMetaDataField;


}