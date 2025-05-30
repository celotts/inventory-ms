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
}