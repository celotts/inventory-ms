package com.celotts.productservice.infrastructure.adapter.input.rest.dto.product;

import com.fasterxml.jackson.annotation.JsonInclude;

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