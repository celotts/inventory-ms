package com.celotts.productservice.infrastructure.adapter.input.rest.dto.product;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class ProductUpdateDTO {

    @Size(max = 50, message = "Product code must not exceed 50 characters")
    private String code;

    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private UUID categoryId;  // ✅ CORRECTO (no String productTypeCode)

    private String unitCode;
    private UUID brandId;
    private Integer minimumStock;
    private Integer currentStock;
    private BigDecimal unitPrice;
    private Boolean enabled;
    private String updatedBy;
}