package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productBrand;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;


@Data
@Builder
@AllArgsConstructor
public class ProductBrandResponseDto {

    private final UUID id;
    private final String name;
    private final String description;
    private final Boolean enabled;

    private final String createdBy;
    private final String updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING) // ← ISO-8601 por defecto
    private final LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime updatedAt;
}