package com.celotts.productservice.domain.port.product.type.output;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTypeEntity;

import java.util.List;
import java.util.Optional;

public interface ProductTypeRepositoryPort {

    // Consultas “light”
    boolean existsByCode(String code);
    Optional<String> findNameByCode(String code);
    List<String> findAllCodes();

    // Consulta por código (devolver la entidad/modelo de ProductType)
    Optional<ProductTypeEntity> findByCode(String code);

    // (Opcional) si necesitas CRUD completo de ProductType, añade métodos para ProductType, no ProductUnit:
    // ProductTypeEntity save(ProductTypeEntity entity);
    // void deleteByCode(String code);
    // List<ProductTypeEntity> findAll();
}