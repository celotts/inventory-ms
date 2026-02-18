package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productunit;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.validation.constraints.Pattern;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductUnitCreateDto {

    @NotBlank(message = "{validation.product-unit.create.code.not-blank}")
    @Size(max = 30, message = "{validation.product-unit.create.code.size}")
    private String code;

    @NotBlank(message = "{validation.product-unit.create.name.not-blank}")
    @Size(min = 2, max = 100, message = "{validation.product-unit.create.name.size}")
    @Pattern(
            regexp = "^[a-zA-Z0-9áéíóúÁÉÍÓÚñÑ\\s\\-_]{2,100}$",
            message = "{validation.product-unit.create.name.pattern}"
    )
    private String name;

    @NotBlank(message = "{validation.product-unit.create.description.not-blank}")
    @Size(max = 500, message = "{validation.product-unit.create.description.size}" )
    private String description;

    @NotNull(message = "{validation.product-unit.create.enabled.not-null}")
    private Boolean enabled;

    @NotBlank(message = "{validation.product-unit.create.symbol.not-blank}")
    private String symbol;

    @Size(max = 100, message = "{validation.product-unit.create.created-by.size}")
    @NotBlank(message = "{validation.product-unit.create.created-by.not-blank}")
    @Pattern(regexp = "^[\\p{L}0-9._\\-\\s@]+$", message = "{validation.product-unit.create.created-by.pattern}")
    private String createdBy;

    private String updatedBy;
}
