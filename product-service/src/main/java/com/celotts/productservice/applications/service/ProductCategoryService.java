package com.celotts.productservice.applications.service;

import com.celotts.productservice.domain.model.ProductCategoryModel;
import com.celotts.productservice.domain.port.CategoryPort;
import com.celotts.productservice.domain.port.ProductCategoryPort;
import com.celotts.productservice.domain.port.ProductRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductCategoryService {

    private final ProductCategoryPort port;
    private final ProductRepositoryPort productPort;
    private final CategoryPort categoryPort;

    public ProductCategoryModel create(ProductCategoryModel model) {
        if (!productPort.existsById(model.productId())) {
            throw new IllegalArgumentException("Product not found");
        }
        if (!categoryPort.existsById(model.categoryId())) {
            throw new IllegalArgumentException("Category not found");
        }
        return port.create(model);
    }

    public List<ProductCategoryModel> getAll() {
        return port.findAll();
    }

    public ProductCategoryModel getById(UUID id) {
        return port.findById(id).orElseThrow(() -> new IllegalArgumentException("Assignment not found"));
    }

    public List<ProductCategoryModel> getByProductId(UUID productId) {
        return port.findByProductId(productId);
    }

    public void delete(UUID id) {
        if (!port.existsById(id)) throw new IllegalArgumentException("Assignment not found");
        port.delete(id);
    }
}