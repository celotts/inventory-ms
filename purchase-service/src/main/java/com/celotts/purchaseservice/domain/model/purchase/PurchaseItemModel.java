package com.celotts.purchaseservice.domain.model.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseItemModel {
    private UUID id;
    private UUID productId;
    private String productCode;
    private String productName;
    private String unitSymbol;
    private BigDecimal quantity;
    private BigDecimal unitCost;
    
    // Tax fields
    private UUID taxId;
    private BigDecimal taxRate; // Guardamos la tasa histórica
    private BigDecimal taxAmount; // Monto calculado del impuesto para este item
}
