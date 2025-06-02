package com.celotts.productservice.infrastructure.adapter.output.postgres.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;            // ✅ AQUÍ va este import
import org.springframework.data.domain.Page;                    // ✅ AQUÍ va este import
import org.springframework.data.domain.Pageable;                // ✅ AQUÍ va este import
import org.springframework.stereotype.Repository;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.ProductEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, UUID> {

    Optional<ProductEntity> findByCode(String code);
    boolean existsByCode(String code);
    List<ProductEntity> findByProductTypeCode(String productTypeCode);
    List<ProductEntity> findByBrandId(UUID brandId);
    List<ProductEntity> findByEnabled(Boolean enabled);
    Page<ProductEntity> findByEnabled(Boolean enabled, Pageable pageable);
    long countByEnabled(Boolean enabled);

    @Query("SELECT p FROM ProductEntity p WHERE p.currentStock < p.minimumStock")
    List<ProductEntity> findByCurrentStockLessThanMinimumStock();

    List<ProductEntity> findByCurrentStockLessThan(Integer stock);
}