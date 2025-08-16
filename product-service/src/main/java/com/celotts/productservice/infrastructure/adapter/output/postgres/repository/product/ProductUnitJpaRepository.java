package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductUnitJpaRepository extends JpaRepository<ProductUnitEntity, UUID> {

    boolean existsByCode(String code);

    @Query("select u.name from ProductUnitEntity u where u.code = :code")
    Optional<String> findNameByCode(String code);

    @Query("select u.code from ProductUnitEntity u")
    List<String> findAllCodes();
}