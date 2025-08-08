package com.celotts.productservice.infrastructure.adapter.input.rest.dto.product;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@EqualsAndHashCode(callSuper = false)
public class ProductCreateDto implements ProductReferenceDto {
    @NotBlank(message = "Product code is required")
    @Size(max = 50)
    private String code;

    @NotBlank
    @Size(min = 2, max = 100)
    private String name;

    @Size(max = 500)
    private String description;

    @NotNull
    private UUID categoryId;

    @NotBlank
    private String unitCode;

    @NotNull
    private UUID brandId;

    @NotNull
    @Min(0)
    private Integer minimumStock;

    @NotNull
    @Min(0)
    private Integer currentStock;

    @NotNull
    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal unitPrice;

    @Builder.Default
    private Boolean enabled = true;

    @NotBlank
    private String createdBy;

    private String updatedBy;
}