package com.celotts.productservice.infrastructure.adapter.input.rest.dto.productcategory;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class ProductCategoryDeleteDto {

    @NotNull(message = "ID is required")
    UUID id;

    @Size(max = 100, message = "updatedBy max length is 100")
    String updatedBy;

    @PastOrPresent(message = "updatedAt must be in the past or present")
    LocalDateTime updatedAt;

    String deletedBy;

    @Size(max = 255, message = "Reason must not exceed 255 characters")
    String reason;
}