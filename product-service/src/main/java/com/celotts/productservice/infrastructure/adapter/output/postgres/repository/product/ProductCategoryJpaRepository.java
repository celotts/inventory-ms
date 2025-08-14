package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductCategoryJpaRepository extends JpaRepository<ProductCategoryEntity, UUID> {
}