package com.celotts.productservice.infrastructure.adapter.input.rest.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;


import com.celotts.productservice.infrastructure.validation.BrandIdExists;
import com.celotts.productservice.infrastructure.validation.CategoryIdExists;
import com.celotts.productservice.infrastructure.validation.UnitCodeExists;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.DecimalMin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductCreateDto {

    @NotBlank(message = "{validation.product.create.code.not-blank}")
    @Size(max = 50, message = "{validation.product.create.code.size}")
    private String code;

    @NotBlank(message = "{validation.product.create.name.not-blank}")
    @Size(min = 2, max = 100, message = "{validation.product.create.name.size}")
    private String name;

    @Size(max = 255, message = "{validation.product.create.description.size}")
    private String description;

    @NotNull(message = "{validation.product.create.category-id.not-null}")
    @CategoryIdExists
    private UUID categoryId;

    @NotBlank(message = "{validation.product.create.unit-code.not-blank}")
    @UnitCodeExists
    private String unitCode;

    @NotNull(message = "{validation.product.create.brand-id.not-null}")
    @BrandIdExists
    private UUID brandId;

    @NotNull(message = "{validation.product.create.minimum-stock.not-null}")
    @Min(value = 0, message = "{validation.product.create.minimum-stock.min}")
    private Integer minimumStock;

    @NotNull(message = "{validation.product.create.current-stock.not-null}")
    @Min(value = 0, message = "{validation.product.create.current-stock.min}")
    private Integer currentStock;

    @NotNull(message = "{validation.product.create.unit-price.not-null}")
    @DecimalMin(value = "0.0", inclusive = false, message = "{validation.product.create.unit-price.decimal-min}")
    private BigDecimal unitPrice;

    @Builder.Default
    private Boolean enabled = true;

    @Size(max = 255, message = "{validation.product.create.created-by.size}")
    @NotBlank(message = "{validation.product.create.created-by.not-blank}")
    private String createdBy;

    @Size(max = 255)
    private String updatedBy;

    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    String updatedAt;

    @JsonFormat(shape =  JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    String createdAt;
}