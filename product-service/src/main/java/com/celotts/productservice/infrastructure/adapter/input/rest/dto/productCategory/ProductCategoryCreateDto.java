package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class ProductCategoryCreateDto {

    @NotNull(message = "Product ID is required")
    UUID productId;

    @NotNull(message = "Category ID is required")
    UUID categoryId;

    // Opcional; si null => default true en el use case
    Boolean enabled;
}
