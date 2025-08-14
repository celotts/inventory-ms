package com.celotts.productserviceOld.domain.port.product.port.usecase;

import com.celotts.productserviceOld.domain.model.ProductModel;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.product.ProductCreate;
import com.celotts.productserviceOld.infrastructure.adapter.input.rest.dto.product.ProductUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductUseCase {


    ProductModel createProduct(ProductCreate dto);
    ProductModel updateProduct(UUID id, ProductUpdateDto dto);

    ProductModel getProductById(UUID id);
    ProductModel getProductByCode(String code);
    void hardDeleteProduct(UUID id);

    // --- Activación / Estado ---
    ProductModel enableProduct(UUID id);
    //void disableProduct(UUID id);
    ProductModel disableProduct(UUID id);
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
    List<ProductModel> getAll();

    // --- Métricas ---
    long countProducts();
    long countActiveProducts();

    // --- Utilidades ---
    Optional<String> validateUnitCode(String code);
    boolean existsById(UUID id);
    boolean existsByCode(String code);


}