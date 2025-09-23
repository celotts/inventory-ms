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

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @NotNull
    @lombok.Builder.Default
    private Boolean enabled = false;

    private String createdBy;
    private String updatedBy;

    // Valida sortBy permitido SIN duplicar el campo (anotando el getter)
    @Pattern(
            regexp = "name|createdAt|updatedAt|enabled",
            message = "SortBy must be one of: name, createdAt, updatedAt, enabled"
    )
    @Override
    public String getSortBy() {
        return super.getSortBy();
    }
}