package com.celotts.productservice.domain.port.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;

import java.util.List;
import java.util.Optional;

public interface ProductTypePort {
    boolean existsByCode(String code);
    Optional<String> findNameByCode(String code);
    List<String> findAllCodes();
    Optional<ProductTypeEntity> findByCode(String code);
}