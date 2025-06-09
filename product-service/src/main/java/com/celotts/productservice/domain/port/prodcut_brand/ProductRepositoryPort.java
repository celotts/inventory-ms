package com.celotts.productservice.domain.port.prodcut_brand;

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
    boolean existsByCode(String code);

    // Métodos de búsqueda adicionales
    Optional<ProductModel> findByCode(String code);

    // ✅ MANTENER: Métodos que SÍ usas
    List<ProductModel> findByCategoryId(UUID categoryId);
    List<ProductModel> findByBrandId(UUID brandId);
    Page<ProductModel> findByEnabled(Boolean enabled, Pageable pageable);

}