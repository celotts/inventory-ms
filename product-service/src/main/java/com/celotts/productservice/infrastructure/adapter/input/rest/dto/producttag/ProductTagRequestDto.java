package com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttag;

import com.celotts.productservice.infrastructure.common.dto.PageableRequestDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)  // incluye campos de la superclase en equals/hashCode
@ToString(callSuper = true)           // opcional, útil para logs
@SuperBuilder
public class ProductTagRequestDto extends PageableRequestDto {

    // --- Filtros (opcionales) ---
    @Size(min = 2, max = 50, message = "Name must be 2-50 chars")
    private String name;           // puedes tratarlo como 'contains' en el servicio

    @Size(max = 1000, message = "Description max length is 1000")
    private String description;

    private Boolean enabled;

    private String createdBy;
    private String updatedBy;

    // --- Rangos de fechas (opcionales) ---
    private LocalDateTime createdFrom;
    private LocalDateTime createdTo;
    private LocalDateTime updatedFrom;
    private LocalDateTime updatedTo;

    // --- Paginación ---
    @Builder.Default
    @PositiveOrZero(message = "Page must be >= 0")
    private Integer page = 0;

    @Builder.Default
    @Min(value = 1, message = "Dize must be >= 1")
    private Integer size = 20;

    // --- Ordenamiento ---
    /**
     * Campos permitidos (ajústalos a tu entidad real si difieren):
     * name, enabled, createdAt, updatedAt
     */
    @Builder.Default
    @Pattern(
            regexp = "name|enabled|createdAt|updatedAt",
            message = "SortBy must be one of: name, enabled, createdAt, updatedAt"
    )
    private String sortBy = "name";

    @Builder.Default
    @Pattern(regexp = "ASC|DESC", message = "SortDir must be 'ASC' or 'DESC'")
    private String sortDir = "ASC";
}