package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.category;

import com.celotts.productservice.domain.model.CategoryModel;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.category.CategoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CategoryRepository extends JpaRepository<CategoryEntity, UUID> {

    Optional<CategoryEntity> findByName(String name);
    boolean existsByName(String name);
    List<CategoryEntity> findByActive(Boolean active);

    List<CategoryEntity> findByNameContaining(String name);

    Page<CategoryEntity> findByActive(Boolean active, Pageable pageable);
    Page<CategoryEntity> findByNameContaining(String name, Pageable pageable);
    Page<CategoryEntity> findByNameContainingAndActive(String name, Boolean active, Pageable pageable);

    long countByActive(Boolean active);

    List<CategoryEntity> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);

    @Query("SELECT c FROM CategoryEntity c WHERE " +
            "(LOWER(c.name) LIKE LOWER(CONCAT('%', :term, '%')) OR " +
            "LOWER(c.description) LIKE LOWER(CONCAT('%', :term, '%'))) " +
            "ORDER BY c.name ASC")
    List<CategoryEntity> findByNameOrDescription(@Param("term") String term, @Param("limit") int limit);

}