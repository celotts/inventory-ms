package com.celotts.productservice.domain.exception.model;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class ProductTagModel {
    UUID id;
    String name;
    String description;
    Boolean enabled;
    String createdBy;
    String updatedBy;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public ProductTagModel withEnabled(boolean enabled){
        return this.toBuilder().enabled(enabled).build();
    }
}