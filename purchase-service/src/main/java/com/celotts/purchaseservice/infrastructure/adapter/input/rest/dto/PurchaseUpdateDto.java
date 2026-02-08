package com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class PurchaseUpdateDto {
    private UUID supplierId;
    private String orderNumber;
    private String status;
    private String currency;
    private BigDecimal subtotal;
    private BigDecimal taxTotal;
    private BigDecimal discountTotal;
    private BigDecimal grandTotal;
    private LocalDate expectedAt;
    private String notes;
    private String updatedBy;
}
