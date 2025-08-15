package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.category;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.category.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

// CategoryJpaRepository (limpio)
public interface CategoryJpaRepository extends JpaRepository<CategoryEntity, UUID> {

    Optional<CategoryEntity> findByNameIgnoreCase(String name); // opcionalmente case-insensitive
    boolean existsByNameIgnoreCase(String name);

    // filtros no paginados (si de verdad los usas)
    List<CategoryEntity> findByActive(Boolean active);
    List<CategoryEntity> findByNameContainingIgnoreCase(String name);

    // filtros paginados
    Page<CategoryEntity> findByActive(Boolean active, Pageable pageable);
    Page<CategoryEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<CategoryEntity> findByNameContainingIgnoreCaseAndActive(String name, Boolean active, Pageable pageable);

    long countByActive(boolean active);

    // Búsqueda libre (name/description)
    @Query("""
           select c from CategoryEntity c
           where lower(c.name) like concat('%', lower(?1), '%')
              or lower(c.description) like concat('%', lower(?1), '%')
           """)
    Page<CategoryEntity> searchByNameOrDescription(String term, Pageable pageable);
}