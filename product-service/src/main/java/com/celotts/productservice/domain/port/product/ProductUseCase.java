package com.celotts.productservice.domain.port.product;

import com.celotts.productservice.domain.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductUseCase {
    //TODO: NO SE USA
    ProductModel createProduct(ProductModel model);
    ProductModel updateProduct(UUID id, ProductModel model);
    //TODO: NO SE USA
    ProductModel getProductById(UUID id);
    ProductModel getProductByCode(String code);
    List<ProductModel> getAllProducts();
    Page<ProductModel> getAllProducts(Pageable pageable);
    Page<ProductModel> getAllProductsWithFilters(Pageable pageable, String code, String name, String description);
    void deleteProduct(UUID id);
    void hardDeleteProduct(UUID id);
    ProductModel enableProduct(UUID id);
    ProductModel disableProduct(UUID id);
    ProductModel updateStock(UUID id, int stock);
    boolean existsById(UUID id);
    Page<ProductModel> getActiveProducts(Pageable pageable);
    List<ProductModel> getInactiveProducts();
    List<ProductModel> getProductsByCategory(UUID categoryId);
    List<ProductModel> getLowStockByCategory(UUID categoryId);
    List<ProductModel> getLowStockProducts();
    List<ProductModel> getProductsByBrand(UUID brandId);
    long countProducts();
    long countActiveProducts();
    Optional<String> validateUnitCode(String code);
}