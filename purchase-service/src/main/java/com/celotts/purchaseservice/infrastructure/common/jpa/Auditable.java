package com.celotts.purchaseservice.infrastructure.common.jpa;

import java.time.LocalDateTime;

public interface Auditable {
    void setCreatedAt(LocalDateTime createdAt);
    void setUpdatedAt(LocalDateTime updatedAt);
    void setCreatedBy(String createdBy);
    void setUpdatedBy(String updatedBy);

    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();
    String getCreatedBy();
    String getUpdatedBy();
}
