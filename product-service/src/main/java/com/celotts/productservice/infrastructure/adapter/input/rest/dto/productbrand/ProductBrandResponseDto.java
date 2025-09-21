package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productbrand;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode; // ✅ ESTA LÍNEA
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder(toBuilder = true)
@AllArgsConstructor
@EqualsAndHashCode
public class ProductBrandResponseDto {

    private final UUID id;
    private final String name;
    private final String description;
    private final Boolean enabled;

    private final String createdBy;
    private final String updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private final LocalDateTime updatedAt;
}