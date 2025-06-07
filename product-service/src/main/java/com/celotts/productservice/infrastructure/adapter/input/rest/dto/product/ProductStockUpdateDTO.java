package com.celotts.productservice.infrastructure.adapter.input.rest.dto.product;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductStockUpdateDTO {

    @NotNull(message = "Stock value is required")
    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private Integer stock;

    private String updatedBy;
}

