package com.celotts.productservice.infrastructure.adapter.input.rest.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ProductRequestDTO {

    @NotBlank
    @Size(max = 50)
    private String code;

    private String description;

    @NotBlank
    private String productTypeCode;

    @NotBlank
    private String unitCode;

    @NotNull
    private UUID brandId;

    @NotNull
    @Min(0)
    private Integer minimunStock;

    @NotNull
    @Min(0)
    private Integer currentStock;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal unitPrice;

    private Boolean enabled = true;

    private String createdBy;
    private String updatedBy;



}
