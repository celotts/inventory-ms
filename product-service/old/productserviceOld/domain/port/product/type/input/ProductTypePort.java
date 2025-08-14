package com.celotts.productserviceOld.domain.port.product.type.input;

import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;

import java.util.List;
import java.util.Optional;

public interface ProductTypePort {
    boolean existsByCode(String code);
    Optional<String> findNameByCode(String code);
    List<String> findAllCodes();
    Optional<ProductTypeEntity> findByCode(String code);
}