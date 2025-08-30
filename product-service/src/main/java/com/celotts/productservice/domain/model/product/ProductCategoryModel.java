package com.celotts.productservice.domain.model.product;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@With
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategoryModel {
    private UUID id;
    private UUID productId;
    private UUID categoryId;
    private LocalDateTime assignedAt;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private String createdBy;
    private Boolean active;
    private String updatedBy;
    private LocalDateTime updatedAt;


    public boolean isActive() {
        return enabled != null && enabled;
    }


    public void activate() {
        this.enabled = true;
    }

    public void deactivate() {
        this.enabled = false;
    }
}
