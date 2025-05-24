package com.celotts.productservice.infrastructure.adapter.input.rest.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductUpdateDTO {

    @Size(max = 50, message = "Product code must not exceed 50 characters")
    @Pattern(regexp = "^[A-Z0-9\\-_]{3,50}$", message = "Product code must contain only uppercase letters, numbers, hyphens and underscores, between 3-50 characters")
    private String code;

    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-_]{2,100}$", message = "Product name contains invalid characters")
    private String name; // ← AGREGAR ESTE CAMPO

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private String productTypeCode;

    private String unitCode;

    private UUID brandId;

    @Min(value = 0, message = "Minimum stock must be greater than or equal to 0")
    private Integer minimumStock;

    @Min(value = 0, message = "Current stock must be greater than or equal to 0")
    private Integer currentStock;

    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Unit price must have maximum 10 digits and 2 decimal places")
    private BigDecimal unitPrice;

    private Boolean enabled;

    private String updatedBy;
}