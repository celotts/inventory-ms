// ProductRepositoryPort - Puerto extendido con métodos adicionales
package com.celotts.productservice.domain.port;

import com.celotts.productservice.domain.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepositoryPort {

    // Métodos básicos CRUD
    ProductModel save(ProductModel product);
    List<ProductModel> findAll();
    Page<ProductModel> findAll(Pageable pageable);  // ✅ AGREGAR ESTE MÉTODO
    Optional<ProductModel> findById(UUID id);
    void deleteById(UUID id);
    boolean existsById(UUID id);

    // Métodos de búsqueda adicionales
    Optional<ProductModel> findByCode(String code);
    boolean existsByCode(String code);  // ✅ YA LO TIENES
    List<ProductModel> findByProductTypeCode(String productTypeCode);
    List<ProductModel> findByBrandId(UUID brandId);
    List<ProductModel> findByEnabled(Boolean enabled);

    // Métodos con paginación
    Page<ProductModel> findByEnabled(Boolean enabled, Pageable pageable);

    // Métodos de conteo
    long count();
    long countByEnabled(Boolean enabled);

    // Métodos de stock
    List<ProductModel> findByCurrentStockLessThanMinimumStock();
    List<ProductModel> findByCurrentStockLessThan(Integer stock);
}