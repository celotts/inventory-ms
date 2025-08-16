package com.celotts.productservice.domain.model;

import java.math.BigDecimal;
import java.util.UUID;


public interface ProductReference {
    String getCode();
    String getName();
    String getDescription();
    UUID getCategoryId();
    String getUnitCode();
    UUID getBrandId();
    Integer getMinimumStock();
    Integer getCurrentStock();
    BigDecimal getUnitPrice();
    Boolean getEnabled();
    String getCreatedBy();
    String getUpdatedBy();
}