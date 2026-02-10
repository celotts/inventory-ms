package com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Schema(description = "DTO representing a purchase order response")
public class PurchaseResponseDto {

    @Schema(description = "Unique identifier of the purchase order", example = "d290f1ee-6c54-4b01-90e6-d701748f0851")
    private UUID id;

    @Schema(description = "Unique identifier of the supplier", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11")
    private UUID supplierId;

    @Schema(description = "Unique order number", example = "PO-2024-00123")
    private String orderNumber;

    @Schema(description = "Current status of the purchase order", example = "APPROVED")
    private String status;

    @Schema(description = "Currency code", example = "USD")
    private String currency;

    @Schema(description = "Subtotal amount", example = "1500.75")
    private BigDecimal subtotal;

    @Schema(description = "Total tax amount", example = "240.12")
    private BigDecimal taxTotal;

    @Schema(description = "Total discount amount", example = "50.00")
    private BigDecimal discountTotal;

    @Schema(description = "Grand total amount", example = "1690.87")
    private BigDecimal grandTotal;

    @Schema(description = "Expected delivery date", example = "2024-12-31")
    private LocalDate expectedAt;

    @Schema(description = "Date when the order was received", example = "2025-01-05T10:30:00")
    private LocalDateTime receivedAt;

    @Schema(description = "Additional notes", example = "Handle with care")
    private String notes;

    @Schema(description = "Timestamp when the record was created", example = "2024-11-20T14:00:00")
    private LocalDateTime createdAt;

    @Schema(description = "User who created the record", example = "carlos.lott")
    private String createdBy;

    @Schema(description = "Timestamp when the record was last updated", example = "2024-11-21T09:15:00")
    private LocalDateTime updatedAt;

    @Schema(description = "User who last updated the record", example = "admin")
    private String updatedBy;
}
