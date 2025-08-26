package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productPriceHistory;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class ProductPriceHistoryResponseDto {
    UUID id;
    UUID productId;
    BigDecimal price;
    Boolean enabled;
    LocalDateTime changedAt;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String createdBy;
    String updatedBy;
}
