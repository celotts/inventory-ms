package com.celotts.productservice.infrastructure.adapter.input.rest.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ProductCategoryResponseDTO {
    private UUID id;
    private UUID productId;
    private UUID categoryId;
    private LocalDateTime createdAt;
}