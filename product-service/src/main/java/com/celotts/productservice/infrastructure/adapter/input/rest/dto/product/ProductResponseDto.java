package com.celotts.productservice.infrastructure.adapter.input.rest.dto.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
@EqualsAndHashCode
@ToString
public class ProductResponseDto {

    private UUID id;
    private String code;
    private String name;
    private String description;
    private UUID categoryId;
    private String categoryName;  // ← Para mostrar nombre de categoría
    private String unitCode;
    private UUID brandId;
    private String brandName;
    private Integer minimumStock;
    private Integer currentStock;
    private BigDecimal unitPrice;
    private Boolean enabled;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;

    // Campo calculado
    private Boolean lowStock;  // ✅ Usa getLowStock()
}