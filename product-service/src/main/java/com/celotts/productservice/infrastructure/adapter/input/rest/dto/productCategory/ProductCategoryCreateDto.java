package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProductCategoryCreateDto {

    @NotNull(message = "Product ID is required")
    private UUID productId;

    @NotNull(message = "Category ID is required")
    private UUID categoryId;

    private LocalDateTime assignedAt;

    @NotNull(message = "Enabled flag is required")
    private Boolean enabled;

    @NotBlank(message = "createdBy is required")
    private String createdBy;

    private String updatedBy;
}
