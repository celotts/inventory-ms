package com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PurchaseResponseDto {
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
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}
