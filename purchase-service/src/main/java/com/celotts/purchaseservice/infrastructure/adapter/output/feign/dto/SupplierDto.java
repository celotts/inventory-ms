package com.celotts.purchaseservice.infrastructure.adapter.output.feign.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties; // <--- Importante
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true) // <--- ESTO SOLUCIONA EL ERROR 503
public class SupplierDto {
    private UUID id;
    private String name;

    @JsonProperty("active")
    private boolean active;
}