package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductCategoryJpaRepository extends JpaRepository<ProductCategoryEntity, UUID> {

    Optional<ProductCategoryEntity> findByProductIdAndCategoryId(UUID productId, UUID categoryId);

    boolean existsByProductIdAndCategoryId(UUID productId, UUID categoryId);

    List<ProductCategoryEntity> findAllByProductId(UUID productId);

    void deleteByProductIdAndCategoryId(UUID productId, UUID categoryId);
}