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
    Page<ProductModel> findInactive(Pageable pageable);

    Page<ProductModel> findByCategory(UUID categoryId, Pageable pageable);
    Page<ProductModel> findByBrand(UUID brandId, Pageable pageable);

    // Low stock
    Page<ProductModel> findLowStock(Pageable pageable, int threshold);
    Page<ProductModel> findLowStockByCategory(UUID categoryId, Pageable pageable, int threshold);

    // Validaciones y mantenimiento
    boolean existsByName(String name);
    boolean existsById(UUID id);
    boolean existsByCode(String code);
    long countAll();
    long countActive();
    ProductModel updateStock(UUID id, int newStock);

    void deleteById(UUID id);
}
