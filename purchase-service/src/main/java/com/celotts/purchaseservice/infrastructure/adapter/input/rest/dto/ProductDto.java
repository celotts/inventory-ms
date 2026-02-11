package com.celotts.purchaseservice.infrastructure.adapter.input.rest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private UUID id;
    private String code;
    private String name;

    @JsonProperty("unit_code")
    private String unitCode;

    @JsonProperty("unit_price")
    private BigDecimal unitPrice;
}
