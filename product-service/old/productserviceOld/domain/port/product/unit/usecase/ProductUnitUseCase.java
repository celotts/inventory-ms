package com.celotts.productserviceOld.domain.port.product.unit.usecase;

import com.celotts.productserviceOld.domain.model.ProductUnitModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductUnitUseCase {

    ProductUnitModel save(ProductUnitModel model);

    Optional<ProductUnitModel> findById(UUID id);
    List<ProductUnitModel>     findAll();

    void deleteById(UUID id);
    boolean existsById(UUID id);
    boolean existsByCode(String code);
    Optional<String> findNameByCode(String code);
    List<String> findAllCodes();
}