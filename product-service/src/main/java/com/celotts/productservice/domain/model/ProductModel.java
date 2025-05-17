package com.celotts.productservice.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class ProductModel {
    private UUID id;
    private String code;
    private String description;
    private String productTypeCode;
    private String unitCode;
    private UUID brandId;
    private Double minimumStock;
    private Double currentStock;
    private Double unitPrice;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

}
