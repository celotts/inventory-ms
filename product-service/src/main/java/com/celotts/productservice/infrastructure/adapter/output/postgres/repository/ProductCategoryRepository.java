package com.celotts.productservice.infrastructure.adapter.output.postgres.repository;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ProductCategoryRepository extends JpaRepository<ProductCategory, UUID> {
    List<ProductCategory> findByProductId(UUID productId);
}