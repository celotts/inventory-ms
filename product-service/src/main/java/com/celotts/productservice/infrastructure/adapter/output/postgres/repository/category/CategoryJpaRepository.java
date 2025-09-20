package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.category;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.category.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

import java.util.Optional;
import java.util.UUID;

public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, UUID> {

    // --------- Exactos / existencia ---------
    Optional<CategoryEntity> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);

    // --------- Búsquedas simples ---------
    Page<CategoryEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Usa la propiedad REAL de la entidad: enabled
    Page<CategoryEntity> findByEnabled(Boolean enabled, Pageable pageable);
    Page<CategoryEntity> findByNameContainingIgnoreCaseAndEnabled(String name, Boolean enabled, Pageable pageable);
    long countByEnabled(boolean enabled);

    // “Activos lógicos” = no borrados y habilitados
    Page<CategoryEntity> findByDeletedAtIsNullAndEnabledTrue(Pageable pageable);
    Page<CategoryEntity> findByNameContainingIgnoreCaseAndDeletedAtIsNullAndEnabledTrue(String name, Pageable pageable);

    // --------- Búsqueda por nombre o descripción (PAGINADA) ---------
    @Query(
            value = """
            SELECT c
            FROM CategoryEntity c
            WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(c.description) LIKE LOWER(CONCAT('%', :q, '%'))
            """,
            countQuery = """
            SELECT COUNT(c)
            FROM CategoryEntity c
            WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(c.description) LIKE LOWER(CONCAT('%', :q, '%'))
            """
    )
    Page<CategoryEntity> searchByNameOrDescription(@Param("q") String q, Pageable pageable);

    // Variante solo activos lógicos (útil para catálogos en UI)
    @Query(
            value = """
            SELECT c
            FROM CategoryEntity c
            WHERE c.deletedAt IS NULL AND c.enabled = TRUE
              AND (
                   LOWER(c.name) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(c.description) LIKE LOWER(CONCAT('%', :q, '%'))
              )
            """,
            countQuery = """
            SELECT COUNT(c)
            FROM CategoryEntity c
            WHERE c.deletedAt IS NULL AND c.enabled = TRUE
              AND (
                   LOWER(c.name) LIKE LOWER(CONCAT('%', :q, '%'))
                OR LOWER(c.description) LIKE LOWER(CONCAT('%', :q, '%'))
              )
            """
    )
    Page<CategoryEntity> searchActiveByNameOrDescription(@Param("q") String q, Pageable pageable);
}