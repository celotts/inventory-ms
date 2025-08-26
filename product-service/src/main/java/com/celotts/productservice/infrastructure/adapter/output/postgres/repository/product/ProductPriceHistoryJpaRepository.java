// src/main/java/com/celotts/productservice/infrastructure/adapter/output/postgres/repository/product/ProductPriceHistoryJpaRepository.java
package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductPriceHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductPriceHistoryJpaRepository extends JpaRepository<ProductPriceHistoryEntity, UUID> {

    // Todos los precios de un producto, ordenados por changedAt DESC
    List<ProductPriceHistoryEntity> findAllByProduct_IdOrderByChangedAtDesc(UUID productId);

    // Último precio (más reciente) de un producto
    Optional<ProductPriceHistoryEntity> findTopByProduct_IdOrderByChangedAtDesc(UUID productId);
}