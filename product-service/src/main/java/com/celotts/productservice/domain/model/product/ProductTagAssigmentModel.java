package com.celotts.productservice.domain.model.product;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class ProductTagAssigmentModel {
    private UUID id;
    private UUID product_id;
    private UUID tag_id;
    private LocalDateTime assigned_At;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updateAt;
    private String createdBy;
    private String updatedby;

    public ProductTagAssigmentModel withEnabled(boolean enabled) {
        return this.toBuilder().enabled(enabled).build();
    }
}