package com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttagassignment;

import com.celotts.productservice.infrastructure.common.dto.PageableRequestDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)   // ← importante
@ToString(callSuper = true)            // opcional
@SuperBuilder
public class ProductTagAssignmentRequestDto extends PageableRequestDto {

    // --- Filtros principales ---
    private UUID productId;
    private UUID tagId;
    private Boolean enabled;

    // --- Auditoría (opcionales) ---
    private String createdBy;
    private String updatedBy;

    // --- Rangos de fechas (opcionales) ---
    private LocalDateTime assignedFrom;
    private LocalDateTime assignedTo;
    private LocalDateTime createdFrom;
    private LocalDateTime createdTo;
    private LocalDateTime updatedFrom;
    private LocalDateTime updatedTo;

    // --- Paginación ---
    @Builder.Default
    @PositiveOrZero(message = "page must be >= 0")
    private Integer page = 0;

    @Builder.Default
    @Min(value = 1, message = "size must be >= 1")
    private Integer size = 20;

    // --- Ordenamiento ---
    /**
     * Campos permitidos para ordenar (ajusta a tu entidad si difiere):
     * productId, tagId, assignedAt, createdAt, updatedAt, enabled
     */
    @Builder.Default
    @Pattern(
            regexp = "productId|tagId|assignedAt|createdAt|updatedAt|enabled",
            message = "sortBy must be one of: productId, tagId, assignedAt, createdAt, updatedAt, enabled"
    )
    private String sortBy = "assignedAt";

    @Builder.Default
    @Pattern(regexp = "ASC|DESC", message = "sortDir must be 'ASC' or 'DESC'")
    private String sortDir = "DESC";
}