package org.ttn.ecommerce.entity.category;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.ttn.ecommerce.auditing.Auditable;

import javax.persistence.*;

@Data
@NoArgsConstructor
@Entity
@Table(name = "category_metadata_field_values")
public class CategoryMetadataFieldValue extends Auditable<String> {

    @EmbeddedId
    private MetaDataValueId id = new MetaDataValueId();

    @ManyToOne
    @MapsId("categoryId")
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @MapsId("categoryMetaDataFieldId")
    @JoinColumn(name = "category_metadata_field_id")
    private CategoryMetaDataField categoryMetaDataField;

    private String value;


}