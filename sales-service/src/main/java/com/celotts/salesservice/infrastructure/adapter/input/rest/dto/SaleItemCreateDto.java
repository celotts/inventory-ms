package com.celotts.salesservice.infrastructure.adapter.input.rest.dto;

import com.celotts.salesservice.domain.model.PreparationArea;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class SaleItemCreateDto {
    @NotNull(message = "Product ID is required")
    private UUID productId;
    
    private String productName;
    
    @NotNull(message = "Quantity is required")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;
    
    @NotNull(message = "Unit price is required")
    private BigDecimal unitPrice;
    
    @NotNull(message = "Preparation area is required")
    private PreparationArea area;
}