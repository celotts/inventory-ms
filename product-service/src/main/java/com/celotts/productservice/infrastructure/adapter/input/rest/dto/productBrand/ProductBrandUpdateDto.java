package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand;

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

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotNull(message = "Enabled flag is required")
    private Boolean enabled;

    /** Usuario que realiza la actualización */
    @NotBlank(message = "updatedBy is required")
    private String updatedBy;
}