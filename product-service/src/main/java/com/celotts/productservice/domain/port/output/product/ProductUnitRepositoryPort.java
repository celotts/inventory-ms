package com.celotts.productservice.domain.port.output.product;

import com.celotts.productservice.domain.model.product.ProductUnitModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductUnitRepositoryPort {

    ProductUnitModel save(ProductUnitModel model);
    Optional<ProductUnitModel> findById(UUID id);
    Optional<ProductUnitModel> findByCode(String code); // Añadido
    List<ProductUnitModel> findAll();

    void deleteById(UUID id);
    boolean existsById(UUID id);
    boolean existsByCode(String code);
    Optional<String> findNameByCode(String code);
    Optional<String> findDescriptionByCode(String code);
    List<String> findAllCodes();
}
