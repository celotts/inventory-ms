package com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

@Data
public class PurchaseCreateDto {
    @NotNull
    private UUID supplierId;

    @NotBlank
    private String orderNumber;

    private String status;
    private String currency;

    @Positive
    private BigDecimal subtotal;

    private BigDecimal taxTotal;
    private BigDecimal discountTotal;

    @Positive
    private BigDecimal grandTotal;

    private LocalDate expectedAt;
    private String notes;
    private String createdBy;
}
