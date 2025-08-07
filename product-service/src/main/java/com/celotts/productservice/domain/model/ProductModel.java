package com.celotts.productservice.domain.model;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true) // <-- aquí está el cambio
@With
@AllArgsConstructor
@NoArgsConstructor
public class ProductModel {
    UUID id;
    String code;
    String name;
    String description;

    UUID categoryId;

    String unitCode;
    UUID brandId;
    Integer minimumStock;
    Integer currentStock;
    BigDecimal unitPrice;
    Boolean enabled;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    String createdBy;
    String updatedBy;

    public boolean lowStock() {
        return currentStock != null && minimumStock != null && currentStock < minimumStock;
    }
}