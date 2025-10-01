package com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttype;

import com.celotts.productservice.infrastructure.common.dto.PageableRequestDto;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

/**
 * Request DTO para filtrar y paginar ProductType.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class ProductTypeRequestDto extends PageableRequestDto {

    // --- Filtros principales ---
    private String code;
    private String name;
    private String description;
    private Boolean enabled;

    // --- Auditoría / Metadatos ---
    private String createdBy;
    private String updatedBy;

    // --- Rango de fechas ---
    private LocalDateTime createdFrom;
    private LocalDateTime createdTo;
    private LocalDateTime updatedFrom;
    private LocalDateTime updatedTo;

    // ✅ whitelist de campos permitidos para ordenar
    @Override
    @Pattern(
            regexp = "code|name|description|enabled|createdAt|updatedAt",
            message = "SortBy must be one of: code, name, description, enabled, createdAt, updatedAt"
    )
    public String getSortBy() {
        return super.getSortBy();
    }
}