package com.celotts.productservice.infrastructure.adapter.output.postgres.read.repository.projection;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public interface LotsExpiringSoonRow {
    UUID getLotId();
    String getLotCode();
    LocalDate getExpirationDate();
    BigDecimal getQuantity();
    String getCode();
    String getName();
    UUID getProductId();
}