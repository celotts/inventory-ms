package com.celotts.productservice.infrastructure.adapter.input.rest.dto.product;

import java.util.UUID;

import com.celotts.productservice.infrastructure.validation.ProductIdExists;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;



@Data
public class ProductStockUpdateDto {

    @NotNull(message = "Product ID is required")
    @ProductIdExists // 🔹 Nueva validación de existencia
    private UUID productId;

    @NotNull(message = "Stock value is required")
    @Min(value = 0, message = "Stock must be greater than or equal to 0")
    private Integer stock;

    private String updatedBy;
}

