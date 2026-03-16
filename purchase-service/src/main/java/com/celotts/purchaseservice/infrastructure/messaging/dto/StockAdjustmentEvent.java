package com.celotts.purchaseservice.infrastructure.messaging.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockAdjustmentEvent implements Serializable {
    private UUID eventId;
    private UUID purchaseId;
    private String reason; // e.g., "PURCHASE_RECEIVED"
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
