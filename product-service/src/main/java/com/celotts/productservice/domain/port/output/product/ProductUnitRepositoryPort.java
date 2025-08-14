package com.celotts.productservice.domain.port.output.product;

import com.celotts.productservice.domain.model.ProductUnitModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductUnitRepositoryPort {

    ProductUnitModel save(ProductUnitModel model);
    Optional<ProductUnitModel> findById(UUID id);
    List<ProductUnitModel> findAll();

    void deleteById(UUID id);
    boolean existsById(UUID id);          // ← añade esto
    boolean existsByCode(String code);    // si ya lo agregaste, déjalo
    Optional<String> findNameByCode(String code);
    List<String> findAllCodes();

    // (Opcional) si luego cambias el use case para usar existsById directamente:
    // boolean existsById(UUID id);
}