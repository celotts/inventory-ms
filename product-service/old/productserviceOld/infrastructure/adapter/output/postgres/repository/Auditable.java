package com.celotts.productserviceOld.infrastructure.adapter.output.postgres.repository;

import java.time.LocalDateTime;

public interface Auditable {
    //TODO: no usages
    // Setters
    void setCreatedAt(LocalDateTime createdAt);
    void setCreatedBy(String createdBy);
    void setUpdatedAt(LocalDateTime updatedAt);
    void setUpdatedBy(String updatedBy);

    //TODO: no usages
    // Getters
    LocalDateTime getCreatedAt();
    String getCreatedBy();
    LocalDateTime getUpdatedAt();
    String getUpdatedBy();
}
