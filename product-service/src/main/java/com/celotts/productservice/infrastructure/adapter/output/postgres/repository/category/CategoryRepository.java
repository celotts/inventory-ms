package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.category;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.category.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

    Optional<CategoryEntity> findByName(String name);

    @Query("SELECT c FROM CategoryEntity c WHERE c.name LIKE %:name%")
    List<CategoryEntity> findByNameContaining(@Param("name") String name);

    boolean existsByName(String name);

    @Query("SELECT c FROM CategoryEntity c ORDER BY c.createdAt DESC")
    List<CategoryEntity> findAllOrderByCreatedAtDesc();

    List<CategoryEntity> findByActive(Boolean active);

    /**
     * Buscar categorías por estado con paginación
     */
    Page<CategoryEntity> findByActive(Boolean active, Pageable pageable);

    /**
     * Buscar por nombre con paginación (case insensitive)
     */
    Page<CategoryEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    /**
     * Buscar por nombre y estado con paginación (case insensitive)
     */
    Page<CategoryEntity> findByNameContainingIgnoreCaseAndActive(String name, Boolean active, Pageable pageable);

    /**
     * Contar por estado
     */
    long countByActive(Boolean active);

    /**
     * Buscar por nombre (case insensitive) - si no lo tienes ya
     */
    Optional<CategoryEntity> findByNameIgnoreCase(String name);

    /**
     * Verificar existencia por nombre (case insensitive) - si no lo tienes ya
     */
    boolean existsByNameIgnoreCase(String name);

    /**
     * Buscar por nombre conteniendo (case insensitive) - si no lo tienes ya
     */
    List<CategoryEntity> findByNameContainingIgnoreCase(String name);
}