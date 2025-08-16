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

    Optional<CategoryEntity> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    List<CategoryEntity> findByNameContainingIgnoreCase(String name);

    // Variante limitada (usa @Query con LIMIT si tu JPA provider lo permite, o Pageable):
    @Query("""
    select c from CategoryEntity c
    where lower(c.name) like lower(concat('%', :q, '%'))
       or lower(c.description) like lower(concat('%', :q, '%'))
    """)
    List<CategoryEntity> findByNameOrDescriptionContainingIgnoreCase(@Param("q") String q,
                                                                     Pageable limitPage);
    // Si prefieres el int limit como en el adapter, puedes hacer:
    // default List<CategoryEntity> findByNameOrDescriptionContainingIgnoreCase(String q, int limit) {
    //     return findByNameOrDescriptionContainingIgnoreCase(q, PageRequest.of(0, limit));
    // }

    // Paginados
    boolean existsByName(String name);
    Page<CategoryEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    Page<CategoryEntity> findByActive(Boolean active, Pageable pageable);

    Page<CategoryEntity> findByNameContainingIgnoreCaseAndActive(String name, Boolean active, Pageable pageable);

    long countByActive(boolean active);

}