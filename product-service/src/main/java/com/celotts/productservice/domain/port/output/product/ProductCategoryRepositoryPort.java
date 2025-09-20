package com.celotts.productservice.domain.port.output.product;

import com.celotts.productservice.domain.model.product.ProductCategoryModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductCategoryRepositoryPort {

    // CRUD básico
    ProductCategoryModel save(ProductCategoryModel model);
    Optional<ProductCategoryModel> findById(UUID id);
    void deleteById(UUID id);

    // Búsquedas por relación producto–categoría
    Optional<ProductCategoryModel> findByProductIdAndCategoryId(UUID productId, UUID categoryId);
    boolean existsByProductIdAndCategoryId(UUID productId, UUID categoryId);

    // 🔐 Regla de negocio: evitar duplicado ACTIVO
    boolean existsByProductIdAndCategoryIdAndEnabledTrue(UUID productId, UUID categoryId);

    // Listados
    List<ProductCategoryModel> findAllByProductId(UUID productId);

    // Operaciones puntuales sobre la relación
    void deleteByProductIdAndCategoryId(UUID productId, UUID categoryId);

    // Toggle de estado (si decides exponerlo a nivel de repositorio)
    void disableById(UUID id);
    void enableById(UUID id);
}