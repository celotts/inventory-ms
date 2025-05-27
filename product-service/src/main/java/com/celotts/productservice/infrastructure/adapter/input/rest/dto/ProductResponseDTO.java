package com.celotts.productservice.infrastructure.adapter.input.rest.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Builder
public class ProductResponseDTO {

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

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    private String createdBy;

    private String updatedBy;

    // Campo calculado
    private Boolean lowStock;

    // Constructor adicional para incluir el cálculo de lowStock
    public static ProductResponseDTO fromModel(com.celotts.productservice.domain.model.ProductModel model) {
        return ProductResponseDTO.builder()
                .id(model.getId())
                .code(model.getCode())
                .description(model.getDescription())
                .productTypeCode(model.getProductTypeCode())
                .unitCode(model.getUnitCode())
                .brandId(model.getBrandId())
                .minimumStock(model.getMinimumStock())
                .currentStock(model.getCurrentStock())
                .unitPrice(model.getUnitPrice())
                .enabled(model.getEnabled())
                .createdAt(model.getCreatedAt())
                .updatedAt(model.getUpdatedAt())
                .createdBy(model.getCreatedBy())
                .updatedBy(model.getUpdatedBy())
                .lowStock(model.getCurrentStock() != null && model.getMinimumStock() != null &&
                        model.getCurrentStock() < model.getMinimumStock())
                .build();
    }
}