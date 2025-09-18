package com.celotts.productservice.domain.port.output.product;

import com.celotts.productservice.domain.model.product.ProductBrandModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductBrandRepositoryPort {
    ProductBrandModel save(ProductBrandModel model);

    Optional<ProductBrandModel> findById(UUID id);
    Optional<ProductBrandModel> findByName(String name);
    List<ProductBrandModel> findAll();

    boolean existsByName(String name);
    boolean existsById(UUID id);
    void deleteById(UUID id);

    int softDelete(UUID id, String deletedBy, String reason);


    // Si tu UseCase los usa:
    Optional<String> findNameById(UUID id);
    List<UUID> findAllIds();

    // Si quieres exponer enable/disable desde el puerto, deben devolver modelo de dominio:
    ProductBrandModel enable(UUID id);
    ProductBrandModel disable(UUID id);
}