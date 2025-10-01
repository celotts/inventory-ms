package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductTypeJpaRepository extends JpaRepository<ProductTypeEntity, UUID> {
    Optional<ProductTypeEntity> findByCode(String code);
    boolean existsByCode(String code);

    @Query("select pt.name from ProductTypeEntity pt where pt.code = :code")
    Optional<String> findNameByCode(String code);

    @Query("select pt.code from ProductTypeEntity pt")
    List<String> findAllCodes();
}