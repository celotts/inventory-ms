package com.celotts.productservice.domain.port.product_brand;

import com.celotts.productservice.domain.model.ProductBrandModel;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


public interface ProductBrandUseCase {
    /*
    📍 Ubicación lógica: Dominio → Entrada

    🔧 Rol:
    Este es el puerto de entrada, define lo que el dominio expone como casos de uso. Por ejemplo, lo que un servicio como ProductBrandService implementa.
    */
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
}
