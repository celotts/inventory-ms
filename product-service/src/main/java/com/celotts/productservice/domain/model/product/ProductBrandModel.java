package com.celotts.productservice.domain.model.product;


import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductBrandModel {
    private UUID id;
    private String name;
    private String description;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    private LocalDateTime deletedAt;
    private String deletedBy;
    private String deletedReason;

    public void activate() { this.enabled = true; }
    public void deactivate() { this.enabled = false; }
}