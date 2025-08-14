package com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.productUnit;

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

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must not exceed 100 characters")
    private String name;

    @NotBlank(message = "Description is required")
    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    @NotBlank(message = "Symbol is required")
    private String symbol;

    @NotNull(message = "Enabled flag is required")
    private Boolean enabled;

    @NotBlank(message = "updatedBy is required")
    private String updatedBy;


}
