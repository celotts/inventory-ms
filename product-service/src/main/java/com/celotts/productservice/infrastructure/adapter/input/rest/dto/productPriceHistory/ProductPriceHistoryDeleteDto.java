package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productPriceHistory;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class ProductPriceHistoryDeleteDto {

    @NotNull(message = "ID is required")
    UUID id;

    @Size(max = 100, message = "updatedBy max length is 100")
    String updatedBy;

    @PastOrPresent(message = "updatedAt must be in the past or present")
    LocalDateTime updatedAt;
}