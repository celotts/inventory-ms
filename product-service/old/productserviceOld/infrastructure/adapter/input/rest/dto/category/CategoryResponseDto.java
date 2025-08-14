package com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.category;

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
public class CategoryResponseDto {
    private UUID id;
    private String name;
    private String description;
    private Boolean active; // Enabled
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}