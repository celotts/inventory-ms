package com.celotts.productservice.infrastructure.adapter.input.rest.dto.category;

import com.celotts.productservice.infrastructure.common.dto.PageableRequestDto;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import jakarta.validation.constraints.NotBlank;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CategoryRequestDto extends PageableRequestDto {

    // --- Filtros ---
    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    private Boolean active;
    private String createdBy;
    private String updatedBy;

    // --- Paginación ---
    @Builder.Default
    @PositiveOrZero(message = "Page must be >= 0")
    private Integer page = 0;

    @Builder.Default
    @Min(value = 1, message = "Size must be >= 1")
    private Integer size = 20;

    // --- Ordenamiento ---
    /**
     * Campos permitidos para ordenar: name, createdAt, updatedAt, active
     * (ajusta a los nombres reales en tu entidad)
     */
    @Builder.Default
    @Pattern(
            regexp = "name|createdAt|updatedAt|active",
            message = "SortBy must be one of: name, createdAt, updatedAt, active"
    )
    private String sortBy = "name";

    @Builder.Default
    @Pattern(regexp = "ASC|DESC", message = "SortDir must be 'ASC' or 'DESC'")
    private String sortDir = "ASC";
}