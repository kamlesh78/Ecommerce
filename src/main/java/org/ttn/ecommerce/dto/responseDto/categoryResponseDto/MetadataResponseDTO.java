package org.ttn.ecommerce.dto.responseDto.categoryResponseDto;

import lombok.Data;

@Data
public class MetadataResponseDTO {
    private Long metadataId;
    private String fieldName;
    private String possibleValues;
}