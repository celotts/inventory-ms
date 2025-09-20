package com.celotts.productservice.infrastructure.adapter.output.postgres.read.projection;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public interface LotViewProjection {
    UUID getId();
    UUID getProductId();
    String getLotCode();
    BigDecimal getQuantity();
    BigDecimal getUnitCost();
    Instant getReceivedAt();
    LocalDate getMfgDate();
    LocalDate getExpirationDate();
    String getStage();
    Boolean getEnabled();
}
