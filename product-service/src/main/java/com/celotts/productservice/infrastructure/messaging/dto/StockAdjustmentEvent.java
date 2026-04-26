package com.celotts.productservice.infrastructure.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

// This DTO must match the structure of the event sent by purchase-service
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAdjustmentEvent implements Serializable {
    private UUID eventId;
    private UUID purchaseId;
    private String reason;
    private List<StockAdjustmentItem> items;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class StockAdjustmentItem implements Serializable {
        private UUID productId;
        private int quantity;
    }
}
