package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder  // ✅ Agregar esta anotación
@NoArgsConstructor
@AllArgsConstructor
public class ProductBrandResponseDto {

    private UUID id;
    private String name;
    private String description;
    private boolean enabled;


    private String createdBy;
    private String updatedBy;
    private String createdAt;
    private String updatedAt;

}
