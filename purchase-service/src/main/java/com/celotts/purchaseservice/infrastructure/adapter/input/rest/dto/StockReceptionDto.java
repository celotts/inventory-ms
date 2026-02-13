package com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Builder
public class StockReceptionDto {
    private UUID productId;
    private BigDecimal quantity;
    private BigDecimal unitCost;
    private String lotCode;
    private LocalDate expirationDate;
    private String referenceNumber;
    private UUID supplierId;
}
