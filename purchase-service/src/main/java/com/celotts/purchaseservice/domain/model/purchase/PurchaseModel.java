package com.celotts.purchaseservice.domain.model.purchase;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseModel {
    private UUID id;
    private UUID supplierId;
    private String orderNumber;
    private String status;
    private String currency;
    private BigDecimal subtotal;
    private BigDecimal taxTotal;
    private BigDecimal discountTotal;
    private BigDecimal grandTotal;
    private LocalDate expectedAt;
    private LocalDateTime receivedAt;
    private String notes;

    // Items
    private List<PurchaseItemModel> items;

    // Auditoria
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy;
    private String deletedReason;

    public void normalize() {
        if (orderNumber != null) orderNumber = orderNumber.trim().toUpperCase();
        if (currency != null) currency = currency.trim().toUpperCase();
        if (notes != null) notes = notes.trim();
        if (status == null) status = "DRAFT";
    }
}
