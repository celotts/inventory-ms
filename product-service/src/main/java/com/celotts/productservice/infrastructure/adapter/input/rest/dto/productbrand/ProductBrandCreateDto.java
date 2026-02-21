package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productbrand;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductBrandCreateDto {

    @NotBlank(message = "{validation.product-brand.create.name.not-blank}")
    @Size(max = 120, message = "{validation.product-brand.create.name.size}")
    private String name;

    @Size(max = 1000, message = "{validation.product-brand.create.description.size}")
    private String description; // opcional

    // opcional; en el use case default = true si es null
    private Boolean enabled;

    @NotBlank(message = "{validation.product-brand.create.created-by.not-blank}")
    private String createdBy;

    private String updatedBy;
}