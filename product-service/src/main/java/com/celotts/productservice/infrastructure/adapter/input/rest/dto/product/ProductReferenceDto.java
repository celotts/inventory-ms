package com.celotts.productservice.infrastructure.adapter.input.rest.dto.product;

import java.util.UUID;

public interface ProductReferenceDto {
    String getUnitCode();
    UUID getBrandId();
    UUID getCategoryId();
}