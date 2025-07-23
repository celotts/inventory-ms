package com.celotts.productservice.domain.port.product.port.input;

import com.celotts.productservice.domain.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductPort {

    // --- CRUD ---
    ProductModel create(ProductModel product);
    ProductModel update(UUID id, ProductModel product);
    Optional<ProductModel> getById(UUID id);
    Optional<ProductModel> getByCode(String code);
    void delete(UUID id);

    // --- Consultas ---
    List<ProductModel> getAll();
    Page<ProductModel> getAll(Pageable pageable);
    Page<ProductModel> getAllWithFilters(Pageable pageable, String code, String name, String description);
    Page<ProductModel> getEnabledProducts(Pageable pageable);
    List<ProductModel> getDisabledProducts();
    List<ProductModel> getByCategory(UUID categoryId);
    List<ProductModel> getLowStockByCategory(UUID categoryId);
    List<ProductModel> getLowStockProducts();
    List<ProductModel> getByBrand(UUID brandId);

    // --- Acciones de negocio ---
    ProductModel enable(UUID id);
    void disable(UUID id);
    ProductModel updateStock(UUID id, int stock);

    // --- Métricas ---
    long count();
    long countEnabled();

    // --- Validaciones / Utilidades ---
    boolean existsById(UUID id);
    boolean existsByCode(String code);
    Optional<String> validateUnitCode(String code);
}