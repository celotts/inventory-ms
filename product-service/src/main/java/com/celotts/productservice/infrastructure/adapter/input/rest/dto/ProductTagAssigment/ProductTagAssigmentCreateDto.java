package com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductTagAssigment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTagAssigmentCreateDto {

    @NotNull(message = "productId is required")
    private UUID productId;

    @NotNull(message = "tagId is required")
    private UUID tagId;

    // Opcional: si no viene, en el servicio puedes asumir TRUE
    @Builder.Default
    private Boolean enabled = Boolean.TRUE;

    @Size(max = 100, message = "createdBy max length is 100")
    @Pattern(regexp = "^[\\p{L}0-9._\\-\\s@]+$", message = "createdBy has invalid characters")
    private String createdBy;
}