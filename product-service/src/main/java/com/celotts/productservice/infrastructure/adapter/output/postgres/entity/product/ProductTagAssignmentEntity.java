package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_tag_assignment")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductTagAssignmentEntity {

    @Id
    @GeneratedValue // Hibernate 6 soporta UUID nativo
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Column(name = "product_id", nullable = false, columnDefinition = "uuid")
    private UUID productId;

    @Column(name = "tag_id", nullable = false, columnDefinition = "uuid")
    private UUID tagId;

    @Column(name = "assigned_at")
    private LocalDateTime assignedAt;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "created_by", length = 100)
    private String createdBy;

    @Column(name = "updated_by", length = 100)
    private String updatedBy;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /* Defaults de app para que se comporte igual en Postgres y H2 */
    @PrePersist
    public void prePersist() {
        if (assignedAt == null) assignedAt = LocalDateTime.now();
        if (createdAt == null)  createdAt  = LocalDateTime.now();
        if (enabled == null)    enabled    = Boolean.TRUE;
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}