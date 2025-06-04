package com.celotts.productservice.infrastructure.adapter.input.rest.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class ProductCategoryRequestDTO {

    @NotNull(message = "Product ID is required")
    private UUID productId;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;
}