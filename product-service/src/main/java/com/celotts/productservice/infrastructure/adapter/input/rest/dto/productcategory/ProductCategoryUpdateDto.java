package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productcategory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategoryUpdateDto {

    @NotNull(message = "{validation.product-category.update.id.not-null}")
    private UUID id;

    private LocalDateTime asignedAt;

    @NotNull(message = "{validation.product-category.update.enabled.not-null}")
    private Boolean enabled;

    @NotBlank(message = "{validation.product-category.update.updated-by.not-blank}")
    private String updatedBy;

}
