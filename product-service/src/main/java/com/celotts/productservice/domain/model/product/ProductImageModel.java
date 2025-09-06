package com.celotts.productservice.domain.model.product;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@With
@AllArgsConstructor
@NoArgsConstructor
public class ProductImageModel {
    private UUID id;
    private UUID productId;
    private String url;
    private LocalDateTime uploadedAt;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private String createdBy;
    private String updatedBy;
    private Boolean active;
    private LocalDateTime updateAt;

    public void activate() {
        this.enabled = true;
    }

    public void deactivate() {
        this.enabled = false;
    }

}
