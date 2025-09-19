package com.celotts.productservice.infrastructure.adapter.input.rest.dto.product;

import com.celotts.productservice.infrastructure.validation.BrandIdExists;
import com.celotts.productservice.infrastructure.validation.CategoryIdExists;
import com.celotts.productservice.infrastructure.validation.ProductIdExists;
import com.celotts.productservice.infrastructure.validation.UnitCodeExists;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateDto {

    @NotNull(message = "Product ID is required")
    @ProductIdExists // 🔹 Validar que el producto existe
    private UUID id;

    @Size(max = 50, message = "Product code must not exceed 50 characters")
    private String code;

    @Size(min = 2, max = 100, message = "Product name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @CategoryIdExists // 🔹 validación de referencia
    private UUID categoryId;

    @UnitCodeExists // 🔹 validación de referencia
    private String unitCode;

    @BrandIdExists // 🔹 validación de referencia
    private UUID brandId;

    @Min(value = 0, message = "Minimum stock must be greater than or equal to 0")
    private Integer minimumStock;

    @Min(value = 0, message = "Current stock must be greater than or equal to 0")
    private Integer currentStock;

    @DecimalMin(value = "0.0", inclusive = false, message = "Unit price must be greater than 0")
    private BigDecimal unitPrice;

    private String createdBy;
    private Boolean enabled;
    private String updatedBy;
}