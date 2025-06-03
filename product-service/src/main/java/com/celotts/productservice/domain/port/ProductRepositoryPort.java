// ProductRepositoryPort - Puerto extendido con métodos adicionales
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
    Optional<ProductModel> findById(UUID id);
    void deleteById(UUID id);
    boolean existsById(UUID id);

    // Métodos de búsqueda adicionales
    Optional<ProductModel> findByCode(String code);
    boolean existsByCode(String code);
    List<ProductModel> findByProductTypeCode(String productTypeCode);
    List<ProductModel> findByBrandId(UUID brandId);
    List<ProductModel> findByEnabled(Boolean enabled);

    // Métodos con paginación
    Page<ProductModel> findByEnabled(Boolean enabled, Pageable pageable);

    // ✅ NUEVO - Método para búsqueda con filtros
    /**
     * Encuentra productos aplicando filtros opcionales de búsqueda
     * @param pageable configuración de paginación y ordenamiento
     * @param code filtro por código del producto (búsqueda parcial, case-insensitive)
     * @param name filtro por nombre del producto (búsqueda parcial, case-insensitive)
     * @param description filtro por descripción del producto (búsqueda parcial, case-insensitive)
     * @return página de productos que coinciden con los filtros aplicados
     */
    Page<ProductModel> findProductsWithFilters(Pageable pageable, String code, String name, String description);

    // Métodos de conteo
    long count();
    long countByEnabled(Boolean enabled);

    // Métodos de stock
    List<ProductModel> findByCurrentStockLessThanMinimumStock();
    List<ProductModel> findByCurrentStockLessThan(Integer stock);
}