package com.celotts.productservice.domain.port.output.product;

import com.celotts.productservice.domain.model.ProductModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.*;

public interface ProductRepositoryPort {
    ProductModel save(ProductModel model);
    Optional<ProductModel> findById(UUID id);
    Optional<ProductModel> findByCode(String code);
    Page<ProductModel> findAll(Pageable pageable);
    Page<ProductModel> findAllWithFilters(Pageable pageable, String code, String name, String description);
    Page<ProductModel> findActive(Pageable pageable);
    List<ProductModel> findInactive();
    List<ProductModel> findByCategory(UUID categoryId);
    List<ProductModel> findLowStockByCategory(UUID categoryId);
    List<ProductModel> findLowStock();
    List<ProductModel> findByBrand(UUID brandId);
    void deleteById(UUID id);
}