package com.celotts.productservice.infrastructure.adapter.output.postgres.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;            // ✅ AQUÍ va este import
import org.springframework.data.domain.Page;                    // ✅ AQUÍ va este import
import org.springframework.data.domain.Pageable;                // ✅ AQUÍ va este import
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.ProductEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {
    Page<ProductEntity> findByProductTypeCode(String productTypeCode, Pageable pageable);

    Page<ProductEntity> findByBrandId(UUID brandId, Pageable pageable);

    Page<ProductEntity> findByEnabled(Boolean enabled, Pageable pageable);


    Page<ProductEntity> findByCurrentStockLessThan(Integer stock, Pageable pageable);

    Optional<ProductEntity> findByCode(String code);
    boolean existsByCode(String code);
    List<ProductEntity> findByProductTypeCode(String productTypeCode);
    List<ProductEntity> findByBrandId(UUID brandId);

    @Query("SELECT p FROM ProductEntity p WHERE " +
            "p.enabled = true AND " +
            "(:code IS NULL OR :code = '' OR LOWER(p.code) LIKE LOWER(CONCAT('%', :code, '%'))) AND " +
            "(:name IS NULL OR :name = '' OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))) AND " +
            "(:description IS NULL OR :description = '' OR LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%')))")
    Page<ProductEntity> findProductsWithFilters(Pageable pageable,
                                                @Param("code") String code,
                                                @Param("name") String name,
                                                @Param("description") String description);
    long countByEnabled(Boolean enabled);





    @Query("""
    SELECT p FROM ProductEntity p
    WHERE (:code IS NULL OR LOWER(p.code) LIKE LOWER(CONCAT('%', :code, '%')))
      AND (:name IS NULL OR LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%')))
      AND (:description IS NULL OR LOWER(p.description) LIKE LOWER(CONCAT('%', :description, '%')))
""")
    Page<ProductEntity> findAllWithFilters(
            @Param("code") String code,
            @Param("name") String name,
            @Param("description") String description,
            Pageable pageable
    );

    @Query("SELECT p FROM ProductEntity p WHERE p.currentStock < p.minimumStock")
    Page<ProductEntity> findProductsWithLowStock(Pageable pageable);
}