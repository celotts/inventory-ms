package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productunit;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUnitUpdateDto {

    @NotBlank(message = "{validation.product-unit.update.name.not-blank}")
    @Size(max = 100, message = "{validation.product-unit.update.name.size}")
    private String name;

    @NotBlank(message = "{validation.product-unit.update.description.not-blank}")
    @Size(max = 500, message = "{validation.product-unit.update.description.size}")
    private String description;

    @NotBlank(message = "{validation.product-unit.update.symbol.not-blank}")
    private String symbol;

    @NotNull(message = "{validation.product-unit.update.enabled.not-null}")
    private Boolean enabled;

    @NotBlank(message = "{validation.product-unit.update.updated-by.not-blank}")
    private String updatedBy;
}