package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productUnit;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class ProductUnitResponseDto {

    private final UUID   id;
    private final String code;          // ← NUEVO
    private final String name;
    private final String description;
    private final boolean enabled;

    private final String createdBy;
    private final String updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime updatedAt;
}
