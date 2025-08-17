package com.celotts.productservice.domain.port.output.product;

import com.celotts.productservice.domain.model.product.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public interface ProductRepositoryPort {

    ProductModel save(ProductModel model);

    Optional<ProductModel> findById(UUID id);
    Optional<ProductModel> findByCode(String code);

    // Listado y filtros
    Page<ProductModel> findAll(Pageable pageable);
    Page<ProductModel> findAllWithFilters(Pageable pageable, String code, String name, String description);
    Page<ProductModel> findActive(Pageable pageable);
    Page<ProductModel> findInactive(Pageable pageable); // <- antes List

    Page<ProductModel> findByCategory(UUID categoryId, Pageable pageable); // <- antes List
    Page<ProductModel> findByBrand(UUID brandId, Pageable pageable);      // <- antes List

    // Low stock (si usas minimumStock como umbral interno, quita el parámetro)
    Page<ProductModel> findLowStock(Pageable pageable, int threshold);
    Page<ProductModel> findLowStockByCategory(UUID categoryId, Pageable pageable, int threshold);
    boolean existsByName(String name);
    // Validaciones y mantenimiento
    boolean existsById(UUID id);
    boolean existsByCode(String code);
    long countAll();
    long countActive();
    ProductModel updateStock(UUID id, int newStock);

    void deleteById(UUID id);
}