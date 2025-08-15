package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ProductTypeJpaRepository extends JpaRepository<ProductTypeEntity, String> {
    boolean existsByCode(String code);

    Optional<ProductTypeEntity> findByCode(String code);

    @Query("select pt.name from ProductTypeEntity pt where pt.code = :code")
    Optional<String> findNameByCode(String code);

    @Query("select pt.code from ProductTypeEntity pt")
    List<String> findAllCodes();
}