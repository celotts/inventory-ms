package com.celotts.productservice.infrastructure.common.listener;

import java.time.LocalDateTime;

public interface Auditable {

    // Setters
    void setCreatedAt(LocalDateTime createdAt);
    void setCreatedBy(String createdBy);
    void setUpdatedAt(LocalDateTime updatedAt);
    void setUpdatedBy(String updatedBy);


    // Getters
    LocalDateTime getCreatedAt();
    String getCreatedBy();
    LocalDateTime getUpdatedAt();
    String getUpdatedBy();
}
