package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class ProductCategoryCreateDto {

    @NotNull(message = "Product ID is required")
    UUID productId;

    @NotNull(message = "Category ID is required")
    UUID categoryId;

    // Si no envían, puedes asumir true en el use case o dejar default aquí:
    @Builder.Default
    Boolean enabled = Boolean.TRUE;

    @Size(max = 100, message = "createdBy max length is 100")
    String createdBy;
}