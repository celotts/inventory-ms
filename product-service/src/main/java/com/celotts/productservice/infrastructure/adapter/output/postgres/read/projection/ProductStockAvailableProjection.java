package com.celotts.productservice.infrastructure.adapter.output.postgres.read.projection;

import java.math.BigDecimal;
import java.util.UUID;

public interface ProductStockAvailableProjection {
    UUID getProductId();
    String getCode();
    String getName();
    BigDecimal getAvailableStock();
}
