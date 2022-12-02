package org.ttn.ecommerce.dto.responseDto;

import lombok.Data;

@Data
public class MetadataResponseDTO {
    private Long metadataId;
    private String fieldName;
    private String possibleValues;
}