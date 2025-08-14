package com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product_tag_assignment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductTagAssignment {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "tag_id", nullable = false)
    private ProductTagEntity tag;

    @Column(name = "created_at")
    private LocalDateTime assignedAt = LocalDateTime.now();
}
