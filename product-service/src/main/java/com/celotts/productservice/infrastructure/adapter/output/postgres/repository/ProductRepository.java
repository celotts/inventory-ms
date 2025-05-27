package com.celotts.productservice.infrastructure.adapter.output.postgres.repository;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID> {

    Optional<Product> findByCode(String code);

    List<Product> findByProductTypeCode(String productTypeCode);

    List<Product> findByBrandId(UUID brandId);  // ← ESTA LÍNEA DEBE EXISTIR

    List<Product> findByEnabled(Boolean enabled);

    Page<Product> findByEnabled(Boolean enabled, Pageable pageable);

    long countByEnabled(Boolean enabled);

    List<Product> findByCurrentStockLessThan(Integer stock);

    @Query("SELECT p FROM Product p WHERE p.currentStock < p.minimumStock")
    List<Product> findByCurrentStockLessThanMinimumStock();
}