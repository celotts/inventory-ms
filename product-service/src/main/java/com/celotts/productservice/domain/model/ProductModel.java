package com.celotts.productservice.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductModel {
    private UUID id;
    private String code;
    private String description;
    private String productTypeCode;
    private String unitCode;
    private UUID brandId;
    private Integer minimumStock;
    private Integer currentStock;
    private BigDecimal unitPrice;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

}
