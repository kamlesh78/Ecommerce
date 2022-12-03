package org.ttn.ecommerce.entity.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@EqualsAndHashCode
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class MetaDataValueId implements Serializable {

    private static final long serialVersionUID = 2702030623316532366L;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "category_metadata_field_id")
    private Long categoryMetaDataFieldId;
}
