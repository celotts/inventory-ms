package com.celotts.productservice.domain.model.product;

import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@With
@AllArgsConstructor
@NoArgsConstructor
public class ProductTagAssignmentModel {
    private UUID id;
    private UUID productId;
    private UUID tagId;
    private LocalDateTime assignedAt;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String createdBy;
    private String updatedBy;

    public void activate() {
        this.enabled = true;
    }

    public void deactivate() {
        this.enabled = false;
    }

    public ProductTagAssignmentModel withEnabled(boolean enabled) {
        return this.toBuilder().enabled(enabled).build();
    }
}