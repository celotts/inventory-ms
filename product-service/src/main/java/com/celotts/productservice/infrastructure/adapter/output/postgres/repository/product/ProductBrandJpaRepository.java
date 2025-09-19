package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductBrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductBrandJpaRepository extends JpaRepository<ProductBrandEntity, UUID> {

    // ===== ACTIVE QUERIES =====
    @Query("SELECT b FROM ProductBrandEntity b WHERE b.id = :id AND b.deletedAt IS NULL")
    Optional<ProductBrandEntity> findActiveById(@Param("id") UUID id);

    @Query("""
        SELECT b
        FROM ProductBrandEntity b
        WHERE LOWER(b.name) = LOWER(:name)
          AND b.deletedAt IS NULL
    """)
    Optional<ProductBrandEntity> findActiveByNameIgnoreCase(@Param("name") String name);

    @Query("""
        SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END
        FROM ProductBrandEntity b
        WHERE LOWER(b.name) = LOWER(:name)
          AND b.deletedAt IS NULL
    """)
    boolean existsActiveByNameIgnoreCase(@Param("name") String name);

    @Query("SELECT b FROM ProductBrandEntity b WHERE b.deletedAt IS NULL")
    List<ProductBrandEntity> findAllActive();

    @Query("SELECT b.name FROM ProductBrandEntity b WHERE b.id = :id AND b.deletedAt IS NULL")
    Optional<String> findActiveNameById(@Param("id") UUID id);

    @Query("SELECT b.id FROM ProductBrandEntity b WHERE b.deletedAt IS NULL")
    List<UUID> findAllActiveIds();

    // ===== ENABLE / DISABLE (solo activos) =====
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE ProductBrandEntity b
           SET b.enabled = true,
               b.updatedAt = CURRENT_TIMESTAMP
         WHERE b.id = :id
           AND b.deletedAt IS NULL
    """)
    int enableBrandById(@Param("id") UUID id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE ProductBrandEntity b
           SET b.enabled = false,
               b.updatedAt = CURRENT_TIMESTAMP
         WHERE b.id = :id
           AND b.deletedAt IS NULL
    """)
    int disableBrandById(@Param("id") UUID id);

    // ===== SOFT DELETE =====
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("""
        UPDATE ProductBrandEntity b
           SET b.deletedAt     = CURRENT_TIMESTAMP,
               b.deletedBy     = :deletedBy,
               b.deletedReason = :reason
         WHERE b.id = :id
           AND b.deletedAt IS NULL
    """)
    int softDelete(@Param("id") UUID id,
                   @Param("deletedBy") String deletedBy,
                   @Param("reason") String reason);

    // ===== (opc) legacy sin filtro de deletedAt =====
    boolean existsByNameIgnoreCase(String name);

    // OJO: nombre correcto es findByNameIgnoreCase (no "findNameIgnoreCase")
    Optional<ProductBrandEntity> findByNameIgnoreCase(String name);

    @Query("SELECT b.id FROM ProductBrandEntity b")
    List<UUID> findAllIds();
}