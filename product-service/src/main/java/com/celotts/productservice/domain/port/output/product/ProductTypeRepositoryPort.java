package com.celotts.productservice.domain.port.output.product;

import com.celotts.productservice.domain.model.ProductTypeModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductTypeRepositoryPort {

    // CRUD principal
    ProductTypeModel save(ProductTypeModel model);
    Optional<ProductTypeModel> findById(UUID id);
    Optional<ProductTypeModel> findByCode(String code);
    List<ProductTypeModel> findAll();
    void deleteById(UUID id);

    // Validaciones
    boolean existsById(UUID id);
    boolean existsByCode(String code);

    // Opcionales (si realmente los usas en casos de uso)
    Optional<String> findNameByCode(String code);
    List<String> findAllCodes();
}