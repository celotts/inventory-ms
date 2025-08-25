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
    private UUID productId;      // <- antes product_id
    private UUID tagId;          // <- antes tag_id
    private LocalDateTime assignedAt; // <- antes assigned_At
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;  // <- antes updateAt
    private String createdBy;
    private String updatedBy;    // <- antes updatedby

    public ProductTagAssignmentModel withEnabled(boolean enabled) {
        return this.toBuilder().enabled(enabled).build();
    }
}