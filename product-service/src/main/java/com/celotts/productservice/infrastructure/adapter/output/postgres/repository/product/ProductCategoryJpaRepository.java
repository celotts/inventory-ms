package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductCategoryJpaRepository extends JpaRepository<ProductCategoryEntity, UUID> {

    // Búsqueda puntual (útil para lecturas o validaciones específicas)
    Optional<ProductCategoryEntity> findByProductIdAndCategoryId(UUID productId, UUID categoryId);

    // Existencia genérica (puede servir para migraciones/limpieza)
    boolean existsByProductIdAndCategoryId(UUID productId, UUID categoryId);

    // 🔐 Chequeo de duplicado "activo" (enabled = true) — para tu regla de negocio
    boolean existsByProductIdAndCategoryIdAndEnabledTrue(UUID productId, UUID categoryId);

    // Listado por producto (para endpoints GET /products/{id}/categories)
    List<ProductCategoryEntity> findAllByProductId(UUID productId);

    // Borrado puntual de la relación
    void deleteByProductIdAndCategoryId(UUID productId, UUID categoryId);
}