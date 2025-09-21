package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productpricehistory;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
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
