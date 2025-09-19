package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product;

import com.celotts.productservice.infrastructure.common.jpa.BaseEntity; // ✅ Import correcto
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_tag_assignment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true)
public class ProductTagAssignmentEntity extends BaseEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "id", columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "product_id", nullable = false, columnDefinition = "uuid")
    private UUID productId;

    @Column(name = "tag_id", nullable = false, columnDefinition = "uuid")
    private UUID tagId;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    @PrePersist
    public void prePersist() {
        if (assignedAt == null) assignedAt = LocalDateTime.now();
    }
}