package com.celotts.productserviceOld.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, UUID> {
}