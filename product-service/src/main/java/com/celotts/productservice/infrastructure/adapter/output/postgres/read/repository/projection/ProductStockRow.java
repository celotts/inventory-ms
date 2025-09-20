package com.celotts.productservice.infrastructure.adapter.output.postgres.read.repository.projection;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductStockRow {
    UUID getProductId();
    String getCode();
    String getName();
    BigDecimal getAvailableStock();
}