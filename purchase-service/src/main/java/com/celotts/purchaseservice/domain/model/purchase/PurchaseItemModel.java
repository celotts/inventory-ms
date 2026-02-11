package com.celotts.purchaseservice.domain.model.purchase;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
public class PurchaseItemModel {
    private UUID id;
    private UUID productId;
    private String productCode;
    private String productName;
    private String unitSymbol;
    private BigDecimal quantity;
    private BigDecimal unitCost;
}
