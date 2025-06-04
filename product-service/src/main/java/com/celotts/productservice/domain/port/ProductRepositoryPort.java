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
    Page<ProductModel> findAll(Pageable pageable);
    Page<ProductModel> findAllWithFilters(Pageable pageable, String code, String name, String description);
    Optional<ProductModel> findById(UUID id);
    void deleteById(UUID id);
    boolean existsById(UUID id);

    // Métodos de búsqueda adicionales
    Optional<ProductModel> findByCode(String code);
    List<ProductModel> findByProductTypeCode(String productTypeCode);
    List<ProductModel> findByBrandId(UUID brandId);
    Page<ProductModel> findByEnabled(Boolean enabled, Pageable pageable);

    //  Método para búsqueda con filtros
    Page<ProductModel> findProductsWithFilters(Pageable pageable, String code, String name, String description);

    // Métodos de conteo
    long count();
    long countByEnabled(Boolean enabled);

    // Métodos de stock
    Page<ProductModel> findByCurrentStockLessThanMinimumStock(Pageable pageable);
    Page<ProductModel> findByCurrentStockLessThan(Integer stock, Pageable pageable);

    boolean existsByCode(String code);

    Page<ProductModel> findByProductTypeCode(String typeCode, Pageable pageable);
    Page<ProductModel> findByBrandId(UUID brandId, Pageable pageable);
}