package com.celotts.productservice.domain.port.product.unit.usecase;

import com.celotts.productservice.domain.model.ProductUnitModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductUnitUseCase {
    ProductUnitModel create(ProductUnitModel model);
    ProductUnitModel update(UUID id, ProductUnitModel model); // ✅ añadir
    void delete(UUID id);
    Optional<ProductUnitModel> findById(UUID id);
    List<ProductUnitModel> findAll();
    boolean existsById(UUID id);
    boolean existsByCode(String code);
    Optional<String> findNameByCode(String code);
    List<String> findAllCodes();
    ProductUnitModel save(ProductUnitModel model);
    void deleteById(UUID id);
}