package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productPriceHistory;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class ProductPriceHistoryUpdateDto {

    @NotNull(message = "ID is required")
    UUID id;

    @DecimalMin(value = "0.00", inclusive = false, message = "Price must be greater than 0")
    @Digits(integer = 12, fraction = 2, message = "Price must have max 2 decimals")
    BigDecimal price;

    Boolean enabled;

    @PastOrPresent(message = "changedAt must be in the past or present")
    LocalDateTime changedAt;
}