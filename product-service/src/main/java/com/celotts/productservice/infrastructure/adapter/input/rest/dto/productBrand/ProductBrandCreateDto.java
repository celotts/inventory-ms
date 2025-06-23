package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductBrandCreateDto {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Enabled flag is required")   // ✅ solo @NotNull
    private Boolean enabled;

    @NotBlank(message = "createdBy is required")
    private String createdBy;

    // Opcional al crear — sin @NotBlank
    private String updatedBy;
}