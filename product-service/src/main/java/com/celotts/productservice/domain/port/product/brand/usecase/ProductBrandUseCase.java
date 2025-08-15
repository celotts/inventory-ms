package com.celotts.productservice.domain.port.product.brand.usecase;

import com.celotts.productservice.domain.model.ProductBrandModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductBrandUseCase {

    // Trabajamos con el modelo (no DTOs)
    ProductBrandModel save(ProductBrandModel brand);

    // Casos de uso explícitos
    ProductBrandModel create(ProductBrandModel brand);
    ProductBrandModel update(UUID id, ProductBrandModel brand);
    void delete(UUID id);

    // Lecturas / utilidades
    Optional<ProductBrandModel> findById(UUID id);
    Optional<ProductBrandModel> findByName(String name);
    List<ProductBrandModel> findAll();
    boolean existsByName(String name);
    boolean existsById(UUID id);
    Optional<String> findNameById(UUID id);
    List<UUID> findAllIds();

    // Compatibilidad (si aún hay llamadas)
    void deleteById(UUID id);

    // Enable/disable
    ProductBrandModel enableBrand(UUID id);
    ProductBrandModel disableBrand(UUID id);
}