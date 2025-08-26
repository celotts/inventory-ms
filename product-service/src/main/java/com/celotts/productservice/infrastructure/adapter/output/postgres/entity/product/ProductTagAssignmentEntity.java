package com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_tag_assignment")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductTagAssignmentEntity {

    @Id
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id; // Se genera en DB por gen_random_uuid(); no uses @GeneratedValue

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
}