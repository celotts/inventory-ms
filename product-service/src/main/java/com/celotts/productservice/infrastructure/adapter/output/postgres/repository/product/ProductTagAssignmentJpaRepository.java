package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTagAssignmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductTagAssignmentJpaRepository
        extends JpaRepository<ProductTagAssignmentEntity, UUID> {

    List<ProductTagAssignmentEntity> findByProductId(UUID productId);

    List<ProductTagAssignmentEntity> findByTagId(UUID tagId);

    Optional<ProductTagAssignmentEntity> findByProductIdAndTagId(UUID productId, UUID tagId);

    boolean existsByProductIdAndTagId(UUID productId, UUID tagId);

    // nuevos
    List<ProductTagAssignmentEntity> findByEnabled(Boolean enabled);
    long countByEnabled(Boolean enabled);

    @Query("""
        select pta from ProductTagAssignmentEntity pta
        where (:productId is null or pta.productId = :productId)
          and (:tagId     is null or pta.tagId = :tagId)
          and (:enabled   is null or pta.enabled = :enabled)
    """)
    List<ProductTagAssignmentEntity> search(UUID productId, UUID tagId, Boolean enabled);
}