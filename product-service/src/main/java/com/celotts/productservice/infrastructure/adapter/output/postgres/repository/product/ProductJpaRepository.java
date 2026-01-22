package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductCategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, UUID> {

    // ---------- Exactos / existencia ----------
    boolean existsByCode(String code);
    boolean existsByName(String name);
    Optional<ProductEntity> findByCode(String code);

    // ---------- Enabled / conteos ----------
    long countByEnabled(boolean enabled);
    Page<ProductEntity> findByEnabled(Boolean enabled, Pageable pageable);

    // ---------- Marca ----------
    Page<ProductEntity> findByBrandId(UUID brandId, Pageable pageable);

    // ---------- Categoría (JOIN con tabla puente) ----------
    // ---------- Categoría (Corregido con subconsulta para evitar errores de JOIN) ----------
    @Query(value = """
        SELECT p FROM ProductEntity p
        WHERE p.id IN (SELECT pc.productId FROM ProductCategoryEntity pc WHERE pc.categoryId = :categoryId)
        """,
            countQuery = """
        SELECT COUNT(p) FROM ProductEntity p
        WHERE p.id IN (SELECT pc.productId FROM ProductCategoryEntity pc WHERE pc.categoryId = :categoryId)
        """)
    Page<ProductEntity> findByCategoryId(@Param("categoryId") UUID categoryId, Pageable pageable);

    // Categoría + stock <= max (Corregido)
    @Query(value = """
            SELECT p FROM ProductEntity p
            WHERE p.id IN (SELECT pc.productId FROM ProductCategoryEntity pc WHERE pc.categoryId = :categoryId)
              AND (p.currentStock IS NOT NULL AND p.currentStock <= :maxStock)
            """,
            countQuery = """
            SELECT COUNT(p) FROM ProductEntity p
            WHERE p.id IN (SELECT pc.productId FROM ProductCategoryEntity pc WHERE pc.categoryId = :categoryId)
              AND (p.currentStock IS NOT NULL AND p.currentStock <= :maxStock)
            """)
    Page<ProductEntity> findByCategoryAndMaxStock(@Param("categoryId") UUID categoryId,
                                                  @Param("maxStock") BigDecimal maxStock,
                                                  Pageable pageable);

    // ---------- Stock bajo (sin categoría) ----------
    Page<ProductEntity> findByCurrentStockLessThanEqual(BigDecimal maxStock, Pageable pageable);

    // ---------- Búsqueda con filtros opcionales ----------
    @Query(value = """
            SELECT p
            FROM ProductEntity p
            WHERE (:code IS NULL OR LOWER(p.code) LIKE LOWER(CONCAT('%', :code, '%')))
              AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:description IS NULL OR LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%')))
            """,
            countQuery = """
            SELECT COUNT(p)
            FROM ProductEntity p
            WHERE (:code IS NULL OR LOWER(p.code) LIKE LOWER(CONCAT('%', :code, '%')))
              AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
              AND (:description IS NULL OR LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%')))
            """)
    Page<ProductEntity> findAllWithFilters(Pageable pageable,
                                           @Param("code") String code,
                                           @Param("name") String name,
                                           @Param("description") String description);
}