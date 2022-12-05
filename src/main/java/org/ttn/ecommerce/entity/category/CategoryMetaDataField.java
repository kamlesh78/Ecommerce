package org.ttn.ecommerce.entity.category;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.ttn.ecommerce.auditing.Auditable;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "category_metadata_field")
public class CategoryMetaDataField extends Auditable<String> {

    @Id
    @SequenceGenerator(name = "category_metadata_sequence", sequenceName = "category_metadata_sequence", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "category_metadata_sequence")
    private Long id;

    private String name;

    @OneToMany(mappedBy = "categoryMetaDataField")
    private List<CategoryMetadataFieldValue> categoryMetadataFieldValues;
}
