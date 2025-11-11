package com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.tax;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.JoinColumn;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class TaxResponseDto {
    private UUID id;

    private String code;
    private String name;
    private BigDecimal rate;
    private String jurisdiction;

    @JsonProperty("tax_type")
    private String taxType;

    @JsonProperty("valid_from")
    private LocalDate validFrom;

    @JsonProperty("valid_to")
    private LocalDate validTo;

    @JsonProperty("active")
    private Boolean enabled;

    private String createdBy;
    private String updatedBy;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime updatedAt;


}
