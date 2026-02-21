package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productbrand;

import com.celotts.productservice.infrastructure.common.dto.PageableRequestDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class ProductBrandRequestDto extends PageableRequestDto {

    @NotBlank(message = "{validation.product-brand.request.name.not-blank}")
    @Size(min = 2, max = 100, message = "{validation.product-brand.request.name.size}")
    private String name;

    @Size(max = 500, message = "{validation.product-brand.request.description.size}")
    private String description;

    @NotNull
    @lombok.Builder.Default
    private Boolean enabled = false;

    private String createdBy;
    private String updatedBy;

    // Valida sortBy permitido SIN duplicar el campo (anotando el getter)
    @Pattern(
            regexp = "name|createdAt|updatedAt|enabled",
            message = "{validation.product-brand.request.sort-by.pattern}"
    )
    @Override
    public String getSortBy() {
        return super.getSortBy();
    }
}