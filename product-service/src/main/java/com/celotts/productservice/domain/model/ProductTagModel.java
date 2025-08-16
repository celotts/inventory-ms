package com.celotts.productservice.domain.model;

import lombok.Builder;
import lombok.Value;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder(toBuilder = true)
public class ProductTagModel {
    private UUID id;
    private String name;
    private String description;
    private Boolean enabled;
    private String createdBy;
    private String updatedBy;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public ProductTagModel withEnabled(boolean enabled){
        return this.toBuilder().enabled(enabled).build();
    }
}