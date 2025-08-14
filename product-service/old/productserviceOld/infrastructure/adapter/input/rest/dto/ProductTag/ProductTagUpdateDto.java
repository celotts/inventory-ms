package com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.ProductTag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

@Value @Builder
public class ProductTagUpdateDto {
    @NotBlank
    @Size(min=2, max=50)
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-_]{2,50}$")
    String name;
    String description;
    Boolean enabled;
    @NotBlank String updatedBy;
}