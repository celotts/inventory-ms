package com.celotts.productservice.infrastructure.adapter.input.rest.dto.producttag;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class ProductTagResponseDto {
    UUID id;
    String name;
    String description;
    Boolean enabled;
    String createdBy;
    String updatedBy;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime createdAt;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime updatedAt;
}