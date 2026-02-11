package com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Schema(description = "DTO for an item within a purchase order")
public class PurchaseItemCreateDto {

    @NotNull
    @Schema(description = "ID of the product being purchased", required = true)
    private UUID productId;

    @NotNull
    @Positive
    @Schema(description = "Quantity of the product being purchased", example = "10.5", required = true)
    private BigDecimal quantity;

    @NotNull
    @Positive
    @Schema(description = "Cost per unit of the product", example = "99.99", required = true)
    private BigDecimal unitCost;

    // Optional fields, can be added later if needed
    // private BigDecimal discountRate;
    // private UUID taxId;
}
