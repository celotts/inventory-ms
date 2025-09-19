package com.celotts.productservice.domain.port.input.product;

import com.celotts.productservice.domain.model.product.ProductBrandModel;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductBrandUseCase {
    ProductBrandModel save(ProductBrandModel productBrand);
    Optional<ProductBrandModel> findById(UUID id);
    Optional<ProductBrandModel> findByName(String name);
    List<ProductBrandModel> findAll();
    boolean existsByName(String name);

    void deleteById(UUID id, String deletedBy, String reason);

    boolean existsById(UUID id);
    Optional<String> findNameById(UUID id);
    List<UUID> findAllIds();
    ProductBrandModel enableBrand(UUID id);
    ProductBrandModel disableBrand(UUID id);

    default ProductBrandModel create(ProductBrandModel productBrand) {
        return save(productBrand);
    }

    default ProductBrandModel update(UUID id, ProductBrandModel productBrand) {
        productBrand.setId(id);
        return save(productBrand);
    }

    // Opción B: mantener helper compatible con la nueva firma
    default void delete(UUID id) {
        deleteById(id, null, null); // o LocalDateTime.now()
    }
}