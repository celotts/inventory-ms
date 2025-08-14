package com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.product;

import jakarta.persistence.Column;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequestDto {

    @NotBlank(message = "Product code is required")
    @Size(max = 50, message = "Product code must not exceed 50 characters")
    @Pattern(regexp = "^[A-Z0-9\\-_]{3,50}$", message = "Product code must contain only uppercase letters, numbers, hyphens and underscores, between 3-50 characters")
    private String code;

    @NotBlank(message = "Product name is required")
    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-_]{2,100}$", message = "Product name contains invalid characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Category is required")
    private UUID categoryId;

    @NotBlank(message = "Unit code is required")
    private String unitCode;

    @NotNull(message = "Brand ID is required")
    private UUID brandId;

    @NotNull(message = "Minimum stock is required")
    @Min(value = 0, message = "Minimum stock must be greater than or equal to 0")
    private Integer minimumStock;

    @NotNull(message = "Current stock is required")
    @Min(value = 0, message = "Current stock must be greater than or equal to 0")
    private Integer currentStock;

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Unit price must have maximum 10 digits and 2 decimal places")
    private BigDecimal unitPrice;

    @Column(nullable = false)
    @Builder.Default
    private Boolean enabled = true;

    private String createdBy;
    private String updatedBy;
}