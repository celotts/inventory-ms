package com.celotts.productservice.infrastructure.adapter.output.postgres.read.repository.projection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface LotExpiredRow {
    UUID getId();
    UUID getProductId();
    String getLotCode();
    BigDecimal getQuantity();
    LocalDate getExpirationDate();
    String getStage();
}