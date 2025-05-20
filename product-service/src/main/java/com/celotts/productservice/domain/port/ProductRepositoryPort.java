package com.celotts.productservice.domain.port;

import com.celotts.productservice.domain.model.ProductModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductRepositoryPort {
    ProductModel save(ProductModel product);
    List<ProductModel> findAll();
    Optional<ProductModel> findById(UUID id);

}
