package com.celotts.productservice.domain.model.product;


import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@With
@AllArgsConstructor
@NoArgsConstructor
public class ProductPriceHistoryModel {
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
