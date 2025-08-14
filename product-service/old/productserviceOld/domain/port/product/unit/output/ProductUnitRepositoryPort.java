package com.celotts.productserviceOld.domain.port.product.unit.output;

import com.celotts.productserviceOld.domain.model.ProductUnitModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductUnitRepositoryPort {
    boolean existsByCode(String code);
    Optional<String> findNameByCode(String code);
    List<String> findAllCodes();

    ProductUnitModel save(ProductUnitModel model);
    Optional<ProductUnitModel> findById(UUID id);
    void deleteById(UUID id);
    List<ProductUnitModel> findAll();
}