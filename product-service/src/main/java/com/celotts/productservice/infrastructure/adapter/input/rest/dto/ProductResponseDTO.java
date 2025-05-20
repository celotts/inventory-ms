package com.celotts.productservice.infrastructure.adapter.input.rest.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.UUID;

@Getter
@Builder
public class ProductResponseDTO {
    private UUID id;
    private String code;
    private String description;
    private String productType;
    private String unit;
    private UUID brandId;
    private Double minimumStock;
    private Double currentStock;
    private Double unitPrice;
    private Boolean enabled;
}