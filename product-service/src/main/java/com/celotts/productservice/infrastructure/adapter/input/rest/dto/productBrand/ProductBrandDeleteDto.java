package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductBrandDeleteDto {
    @NotBlank(message = "Category ID is required")
    @NotNull(message = "Category ID is not null")
    private UUID id;
}
