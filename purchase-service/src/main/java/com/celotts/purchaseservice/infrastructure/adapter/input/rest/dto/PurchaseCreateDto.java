package com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
@Schema(description = "DTO for creating a new purchase order")
public class PurchaseCreateDto {

    @NotNull
    @Schema(description = "Unique identifier of the supplier", example = "a0eebc99-9c0b-4ef8-bb6d-6bb9bd380a11", required = true)
    private UUID supplierId;

    @NotBlank
    @Schema(description = "Unique order number for the purchase", example = "PO-2024-00123", required = true)
    private String orderNumber;

    @Schema(description = "Initial status of the purchase order", example = "DRAFT", defaultValue = "DRAFT")
    private String status;

    @Schema(description = "Currency of the purchase", example = "USD", defaultValue = "USD")
    private String currency;

    @Positive
    @Schema(description = "Total amount before taxes and discounts", example = "1500.75")
    private BigDecimal subtotal;

    @Schema(description = "Total tax amount", example = "240.12")
    private BigDecimal taxTotal;

    @Schema(description = "Total discount amount", example = "50.00")
    private BigDecimal discountTotal;

    @Positive
    @Schema(description = "The final total amount (subtotal + tax - discount)", example = "1690.87")
    private BigDecimal grandTotal;

    @Schema(description = "Expected delivery date", example = "2024-12-31")
    private LocalDate expectedAt;

    @Schema(description = "Additional notes for the purchase order", example = "Handle with care")
    private String notes;

    @Schema(description = "User who created the purchase order", example = "carlos.lott")
    private String createdBy;
}
