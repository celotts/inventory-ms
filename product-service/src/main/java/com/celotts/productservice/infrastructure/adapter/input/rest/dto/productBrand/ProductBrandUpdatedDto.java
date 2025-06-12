package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ProductBrandUpdatedDto {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    @Column(nullable = false, length = 100)
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    @Column(nullable = false, length = 500, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private boolean enabled = true;
}
