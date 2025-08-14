package com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productBrand;


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
public class ProductBrandRequestDto {
    @NotBlank(message = "Name is required")
    @Size(min = 2, max =100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull
    @Builder.Default
    private Boolean enabled = false;

    private String createdBy;
    private String updatedBy;
}
