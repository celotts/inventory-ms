package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit;


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
public class ProductUnitCreateDto {
    @NotBlank
    private String code;

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters" )
    private String description;

    @NotNull(message = "Enabled flag is required")
    private Boolean enabled;

    @NotBlank(message = "Symbol is required")
    private String symbol;

    @NotBlank(message = "createdBy is required")
    private String createdBy;

    private String updatedBy;
}
