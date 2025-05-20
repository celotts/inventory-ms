package com.celotts.productservice.infrastructure.common.entity;

import com.celotts.productservice.infrastructure.common.listener.Auditable;
import jakarta.persistence.Column;

import java.time.LocalDateTime;

public abstract  class BaseEntity  implements Auditable {

    @Column(name= "created_at", updatable = false)
    protected LocalDateTime createdAt;

    @Column(name= "updated_at")
    protected LocalDateTime updatedAt;

    @Column(name= "created_by")
    protected String createdBy;

    @Column(name= "updated_by")
    protected String updatedBy;

    @Override
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
