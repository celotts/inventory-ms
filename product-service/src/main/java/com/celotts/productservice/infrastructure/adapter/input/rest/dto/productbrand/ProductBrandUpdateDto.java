package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productbrand;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;        // ← importa Lombok

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductBrandUpdateDto {

    @NotBlank(message = "{validation.product-brand.update.name.not-blank}")
    @Size(max = 100, message = "{validation.product-brand.update.name.size}")
    private String name;

    @NotBlank(message = "{validation.product-brand.update.description.not-blank}")
    @Size(max = 500, message = "{validation.product-brand.update.description.size}")
    private String description;

    @NotNull(message = "{validation.product-brand.update.enabled.not-null}")
    private Boolean enabled;

    /** Usuario que realiza la actualización */
    @NotBlank(message = "{validation.product-brand.update.updated-by.not-blank}")
    private String updatedBy;
}