package com.celotts.productservice.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductModel {

    private UUID id;
    private String code;
    private String name;
    private String description;

    // Referencias
    private String productTypeCode;
    private String unitCode;
    private UUID brandId;

    // Stock y pricing
    private Integer minimumStock;
    private Integer currentStock;
    private BigDecimal unitPrice;

    // Estado
    private Boolean enabled;

    // Auditoría
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;
}