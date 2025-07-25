package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productCategory;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryResponseDto {
    private UUID id;
    private UUID productId;
    private UUID categoryId;
    private Boolean enabled;
    private String updatedBy;
    private String createdBy;
    private LocalDateTime assignedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}