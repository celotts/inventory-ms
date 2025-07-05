package com.celotts.productservice.domain.port.product.root.input;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductUseCase {

    // --- CRUD ---
    ProductModel createProduct(ProductRequestDTO requestDTO);
    ProductModel updateProduct(UUID id, ProductRequestDTO requestDTO);
    ProductModel getProductById(UUID id);
    ProductModel getProductByCode(String code);
    void hardDeleteProduct(UUID id);

    // --- Activación / Estado ---
    ProductModel enableProduct(UUID id);
    void disableProduct(UUID id);
    ProductModel updateStock(UUID id, int stock);

    // --- Consultas ---
    Page<ProductModel> getAllProducts(Pageable pageable);
    Page<ProductModel> getAllProductsWithFilters(Pageable pageable, String code, String name, String description);
    Page<ProductModel> getActiveProducts(Pageable pageable);
    List<ProductModel> getInactiveProducts();
    List<ProductModel> getProductsByCategory(UUID categoryId);
    List<ProductModel> getLowStockByCategory(UUID categoryId);
    List<ProductModel> getLowStockProducts();
    List<ProductModel> getProductsByBrand(UUID brandId);

    // --- Métricas ---
    long countProducts();
    long countActiveProducts();

    // --- Utilidades ---
    Optional<String> validateUnitCode(String code);
}