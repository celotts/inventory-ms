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
public class ProductUnitUpdateDto {

    @NotBlank(message = "Name is required")
    @Size(max = 100, message = "Name must no exceed 100 characters")
    private String name;

    private String description;

    @NotNull(message = "Enabled flag is required")
    private Boolean enabled;

    @NotBlank(message = "update is required")
    private String updatedBy;


}
