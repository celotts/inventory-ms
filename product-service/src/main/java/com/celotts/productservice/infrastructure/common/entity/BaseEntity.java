package com.celotts.productservice.infrastructure.common.entity;

import com.celotts.productservice.infrastructure.common.jpa.Auditable;
import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
//TODO: not used
@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity implements Auditable {
    //TODO: NO SE USA esta clase
    @Column(name = "created_at", updatable = false)
    protected LocalDateTime createdAt;

    @Column(name = "updated_at")
    protected LocalDateTime updatedAt;

    @Column(name = "created_by", length = 100)
    protected String createdBy;

    @Column(name = "updated_by", length = 100)
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

    // Getters adicionales para el interfaz Auditable
    @Override
    public LocalDateTime getCreatedAt() {
        return this.createdAt;
    }

    @Override
    public LocalDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    @Override
    public String getCreatedBy() {
        return this.createdBy;
    }

    @Override
    public String getUpdatedBy() {
        return this.updatedBy;
    }
}
