package com.celotts.productservice.domain.port.prodcut_brand;

import com.celotts.productservice.domain.model.ProductBrandModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductBrandRepositoryPort {

    ProductBrandModel save(ProductBrandModel productBrand);
    Optional<ProductBrandModel> findById(UUID id);
    Optional<ProductBrandModel> findByName(String name);
    List<ProductBrandModel> findAll();
    boolean existsByName(String name);
    boolean existsById(UUID id);
    void deleteById(UUID id);
}