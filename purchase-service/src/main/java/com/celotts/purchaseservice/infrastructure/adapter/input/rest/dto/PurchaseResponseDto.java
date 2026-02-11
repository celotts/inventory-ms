package com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Schema(description = "DTO representing a purchase order response")
public class PurchaseResponseDto {

    @Schema(description = "Unique identifier of the purchase order")
    private UUID id;

    @Schema(description = "Unique identifier of the supplier")
    private UUID supplierId;

    @Schema(description = "Unique order number")
    private String orderNumber;

    @Schema(description = "Current status of the purchase order")
    private String status;

    @Schema(description = "Currency code")
    private String currency;

    @Schema(description = "Subtotal amount")
    private BigDecimal subtotal;

    @Schema(description = "Total tax amount")
    private BigDecimal taxTotal;

    @Schema(description = "Total discount amount")
    private BigDecimal discountTotal;

    @Schema(description = "Grand total amount")
    private BigDecimal grandTotal;

    @Schema(description = "Expected delivery date")
    private LocalDate expectedAt;

    @Schema(description = "Date when the order was received")
    private LocalDateTime receivedAt;

    @Schema(description = "Additional notes")
    private String notes;

    @Schema(description = "List of items in the purchase")
    private List<PurchaseItemResponseDto> items;

    @Schema(description = "Timestamp when the record was created")
    private LocalDateTime createdAt;

    @Schema(description = "User who created the record")
    private String createdBy;

    @Schema(description = "Timestamp when the record was last updated")
    private LocalDateTime updatedAt;

    @Schema(description = "User who last updated the record")
    private String updatedBy;
}
