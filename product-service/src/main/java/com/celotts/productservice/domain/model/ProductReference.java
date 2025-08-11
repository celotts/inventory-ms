package com.celotts.productservice.domain.model;

import java.util.UUID;

public interface ProductReference {
    String getUnitCode();
    UUID getBrandId();
    UUID getCategoryId();
}