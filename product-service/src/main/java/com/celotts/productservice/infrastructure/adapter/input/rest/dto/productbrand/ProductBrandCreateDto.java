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

    @NotBlank(message = "Name is required")
    @Size(max = 120, message = "Name must not exceed 120 characters")
    private String name;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description; // opcional

    // opcional; en el use case default = true si es null
    private Boolean enabled;

    @NotBlank(message = "createdBy is required")
    private String createdBy;

    private String updatedBy;
}