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

    @NotNull(message = "ID is required for update")
    private UUID id;

    private LocalDateTime asignedAt;

    @NotNull(message = "Enabled flag is required")
    private Boolean enabled;

    @NotBlank(message = "updatedBy is required")
    private String updatedBy;

}
