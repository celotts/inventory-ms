package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productTag;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTagCreateDto {
    @NotBlank
    @Size(min = 2, max = 50, message = "name must be 2-50 chars")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-_]{2,50}$")
    String name;

    @Size(max = 1000, message = "description max length is 1000")
    String description;

    @Builder.Default
    Boolean enabled = Boolean.TRUE;

    @Size(max = 100, message = "createdBy max length is 100")
    @NotBlank String createdBy;
}