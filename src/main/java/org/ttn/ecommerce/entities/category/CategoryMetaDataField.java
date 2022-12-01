package org.ttn.ecommerce.entities.category;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "category_metadata_field")
public class CategoryMetaDataField {

    @Id
    @SequenceGenerator(name="category_metadata_sequence",sequenceName = "category_metadata_sequence",initialValue = 1,allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "category_metadata_sequence")
    private Long id;

    private String name;

     @OneToMany(mappedBy = "categoryMetaDataField")
    private List<CategoryMetadataFieldValue> categoryMetadataFieldValues;
}
