package com.celotts.productservice.domain.port.input.product;

import com.celotts.productservice.domain.model.product.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductUseCase {

    ProductModel createProduct(ProductModel cmd);                    // ← antes ProductReference
    ProductModel updateProduct(UUID id, ProductModel cmd);           // ← antes ProductReference

    ProductModel getProductById(UUID id);
    ProductModel getProductByCode(String code);
    void hardDeleteProduct(UUID id);

    ProductModel enableProduct(UUID id);
    void disableProduct(UUID id);
    ProductModel updateStock(UUID id, int stock);

    Page<ProductModel> getAllProducts(Pageable pageable);
    Page<ProductModel> getAllProductsWithFilters(Pageable pageable, String code, String name, String description);
    Page<ProductModel> getActiveProducts(Pageable pageable);
    List<ProductModel> getInactiveProducts();
    List<ProductModel> getProductsByCategory(UUID categoryId);
    List<ProductModel> getLowStockByCategory(UUID categoryId);
    List<ProductModel> getLowStockProducts();
    List<ProductModel> getProductsByBrand(UUID brandId);
    List<ProductModel> getAll();

    long countProducts();
    long countActiveProducts();

    Optional<String> validateUnitCode(String code);
    boolean existsById(UUID id);
    boolean existsByCode(String code);
}