package com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Schema(description = "DTO for an item within a purchase order response")
public class PurchaseItemResponseDto {
    private UUID id;
    private UUID productId;
    private String productCode;
    private String productName;
    private String unitSymbol;
    private BigDecimal quantity;
    private BigDecimal unitCost;
}
