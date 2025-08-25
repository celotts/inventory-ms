package com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttag;

import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTagUpdateDto {
    @Size(min = 2, max = 50)
    private String name;

    private String description;

    private Boolean enabled;

    private String updatedBy;
}