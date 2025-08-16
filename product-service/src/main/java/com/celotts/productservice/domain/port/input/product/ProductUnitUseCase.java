package com.celotts.productservice.domain.port.input.product;

import com.celotts.productservice.domain.model.ProductUnitModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductUnitUseCase {
    ProductUnitModel save(ProductUnitModel model);
    List<ProductUnitModel> findAll();
    Optional<ProductUnitModel> findById(UUID id);
    ProductUnitModel update(UUID id, ProductUnitModel model);
    void deleteById(UUID id);
    boolean existsByCode(String code);
    Optional<String> findNameByCode(String code);
    List<String> findAllCodes();
}