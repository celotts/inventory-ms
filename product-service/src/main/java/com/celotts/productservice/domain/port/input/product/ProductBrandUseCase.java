package com.celotts.productservice.domain.port.input.product;

import com.celotts.productservice.domain.model.ProductBrandModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ProductBrandUseCase {
    ProductBrandModel save(ProductBrandModel productBrand);
    Optional<ProductBrandModel> findById(UUID id);
    Optional<ProductBrandModel> findByName(String name);
    List<ProductBrandModel> findAll();
    boolean existsByName(String name);
    void deleteById(UUID id);
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

    default void delete(UUID id) {
        deleteById(id);
    }
}
