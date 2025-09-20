package com.celotts.productservice.domain.port.output.read.perishable.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductStockDto(
        UUID productId,
        String code,
        String name,
        BigDecimal availableStock
) {}