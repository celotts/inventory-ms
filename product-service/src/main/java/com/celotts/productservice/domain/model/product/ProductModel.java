package com.celotts.productservice.domain.model.product;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@With
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {
    private UUID id;
    private String code;
    private String name;
    private String description;

    private UUID categoryId;

    private String unitCode;
    private UUID brandId;
    private Integer minimumStock;
    private Integer currentStock;
    private BigDecimal unitPrice;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Boolean active;
    private String createdBy;
    private String updatedBy;

    public void activate() {
        this.enabled = true;
    }

    public void deactivate() {
        this.enabled = false;
    }

    public boolean lowStock() {
        return currentStock != null && minimumStock != null && currentStock < minimumStock;
    }
}