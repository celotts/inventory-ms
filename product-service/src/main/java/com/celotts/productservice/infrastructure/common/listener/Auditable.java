package com.celotts.productservice.infrastructure.common.listener;

import java.time.LocalDateTime;

public interface Auditable {
    void setCreatedAt(LocalDateTime createdAt);
    void setUpdatedAt(LocalDateTime updatedAt);
    void setCreatedBy(String createdBy);
    void setUpdatedBy(String updatedBy);
}
