package com.celotts.productservice.domain.model.views;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductStockAvailableView(
        UUID productId, String code, String name, BigDecimal availableStock
) {}