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

    // Búsqueda exacta (case-insensitive)
    Optional<CategoryEntity> findByNameIgnoreCase(String name);

    // Existencia (case-insensitive, consistente con el adapter)
    boolean existsByNameIgnoreCase(String name);

    // Búsqueda parcial por nombre (case-insensitive)
    List<CategoryEntity> findByNameContainingIgnoreCase(String name);

    // Búsqueda parcial en nombre o descripción con límite (Pageable controla LIMIT/OFFSET)
    @Query("""
        select c from CategoryEntity c
        where lower(c.name) like lower(concat('%', :q, '%'))
           or lower(c.description) like lower(concat('%', :q, '%'))
        order by c.name asc
    """)
    List<CategoryEntity> findByNameOrDescriptionContainingIgnoreCase(@Param("q") String q,
                                                                     Pageable limitPage);

    // ------------------ Métodos paginados ------------------ //

    Page<CategoryEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<CategoryEntity> findByActive(Boolean active, Pageable pageable);

    Page<CategoryEntity> findByNameContainingIgnoreCaseAndActive(String name, Boolean active, Pageable pageable);

    long countByActive(boolean active);
}