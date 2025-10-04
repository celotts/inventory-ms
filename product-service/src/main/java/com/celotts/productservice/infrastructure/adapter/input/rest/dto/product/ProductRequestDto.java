package com.celotts.productservice.infrastructure.adapter.input.rest.dto.product;

import com.celotts.productservice.infrastructure.common.dto.PageableRequestDto;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@SuperBuilder
public class ProductRequestDto extends PageableRequestDto {

    // --- Filtros principales ---
    private String code;
    private String name;
    private UUID categoryId;
    private UUID brandId;
    private String unitCode;
    private Boolean enabled;

    private String description;
    // --- Rango de precios ---
    private BigDecimal unitPriceMin;
    private BigDecimal unitPriceMax;

    // --- Rango de stock ---
    private Integer minimumStockMin;
    private Integer minimumStockMax;
    private Integer currentStockMin;
    private Integer currentStockMax;

    // --- Auditoría / Metadatos (opcionales) ---
    private String createdBy;
    private String updatedBy;

    // --- Rango de fechas ---
    private LocalDateTime createdFrom;
    private LocalDateTime createdTo;
    private LocalDateTime updatedFrom;
    private LocalDateTime updatedTo;

    @Override
    @Pattern(
            regexp = "code|name|unitPrice|currentStock|minimumStock|categoryId|brandId|unitCode|enabled|createdAt|updatedAt",
            message = "SortBy must be one of: code, name, unitPrice, currentStock, minimumStock, categoryId, brandId, unitCode, enabled, createdAt, updatedAt"
    )
    public String getSortBy() {
        return super.getSortBy();
    }
}