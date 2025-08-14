package com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.category;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "category")
public class CategoryEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, length = 100)
    private String name;

    private String description;

    @Builder.Default
    @Column(name = "active")
    private Boolean active = Boolean.TRUE;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        if (this.createdAt == null) {
            this.createdAt = LocalDateTime.now();
        }
        if (this.active == null) {
            this.active = Boolean.TRUE;
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}