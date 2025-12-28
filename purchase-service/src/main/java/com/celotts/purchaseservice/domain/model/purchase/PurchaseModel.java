package com.celotts.purchaseservice.domain.model.purchase;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class PurchaseModel {
    private UUID id;
    private UUID supplierId;
    private String orderNumber;
    private String status;
    private String currency;
    private BigDecimal subtotal;
    private BigDecimal taxTotal;
    private BigDecimal discountTotal;
    private BigDecimal grandTotal; // Corregido: granTotal -> grandTotal
    private LocalDate expectedAt;
    private LocalDateTime receivedAt;
    private String notes;

    // Auditoria
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
    private LocalDateTime deletedAt;
    private String deletedBy; // Corregido: deletdBy -> deletedBy
    private String deletedReason; // Corregido: deleteReason -> deletedReason

    public void normalize() { // Sugerencia: normalize (en inglés como tus variables)
        if (orderNumber != null) orderNumber = orderNumber.trim().toUpperCase();
        if (currency != null) currency = currency.trim().toUpperCase();
        if (notes != null) notes = notes.trim();
        if (status == null) status = "DRAFT";
    }
}