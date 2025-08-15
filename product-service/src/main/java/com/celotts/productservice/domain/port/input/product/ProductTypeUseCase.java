package com.celotts.productservice.domain.port.input.product;

import com.celotts.productservice.domain.model.ProductTypeModel;

import java.util.List;
import java.util.Optional;

public interface ProductTypeUseCase {
    boolean existsByCode(String code);
    Optional<String> findNameByCode(String code);
    List<String> findAllCodes();
    Optional<ProductTypeModel> findByCode(String code); // ← MODEL, no Entity
}