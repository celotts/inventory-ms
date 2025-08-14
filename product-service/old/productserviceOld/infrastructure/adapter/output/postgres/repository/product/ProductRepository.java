package com.celotts.productserviceOld.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    // Métodos existentes...
    Optional<ProductEntity> findByCode(String code);
    boolean existsByCode(String code);
    List<ProductEntity> findByCategoryId(UUID categoryId);
    List<ProductEntity> findByBrandId(UUID brandId);
    Page<ProductEntity> findByEnabled(Boolean enabled, Pageable pageable);


    @Query("""
    SELECT p FROM ProductEntity p
    WHERE (:code IS NULL OR LOWER(p.code) LIKE LOWER(CONCAT('%', :code, '%')))
      AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:description IS NULL OR LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%')))
    """)
    Page<ProductEntity> findAllWithFilters(
            Pageable pageable,
            @Param("code") String code,
            @Param("name") String name,
            @Param("description") String description
    );

    @Query("SELECT p.code FROM ProductEntity p")
    List<String> findAllCodes();
}