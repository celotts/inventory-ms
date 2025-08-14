package com.celotts.productservice.applications.usecase.product;

// si la sigues usando

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.model.ProductReference;
import com.celotts.productservice.domain.port.category.output.CategoryRepositoryPort;
import com.celotts.productservice.domain.port.input.product.ProductBrandPort;
import com.celotts.productservice.domain.port.input.product.ProductUseCase;
import com.celotts.productservice.domain.port.output.product.ProductRepositoryPort;
import com.celotts.productservice.domain.port.output.product.ProductUnitRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.*;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductCreate;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductUpdateDto;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductRequestMapper;

import com.celotts.productservice.domain.exception.ResourceNotFoundException;
import com.celotts.productservice.domain.exception.ResourceAlreadyExistsException;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductUseCaseImpl implements ProductUseCase {

    private final ProductRepositoryPort productRepositoryPort;
    private final ProductUnitRepositoryPort productUnitPort;
    private final ProductBrandPort productBrandPort;
    private final CategoryRepositoryPort categoryRepositoryPort;
    private final ProductRequestMapper productRequestMapper;

    @Autowired
    public ProductUseCaseImpl(
            ProductRepositoryPort productRepositoryPort,
            ProductUnitRepositoryPort productUnitPort,
            @Qualifier("productBrandAdapter") ProductBrandPort productBrandPort,
            @Qualifier("categoryAdapter") CategoryRepositoryPort categoryRepositoryPort,
            ProductRequestMapper productRequestMapper
    ) {
        this.productRepositoryPort = productRepositoryPort;
        this.productUnitPort = productUnitPort;
        this.productBrandPort = productBrandPort;
        this.categoryRepositoryPort = categoryRepositoryPort;
        this.productRequestMapper = productRequestMapper;
    }

    @Override
    public ProductModel createProduct(ProductCreate dto) {
        if (productRepositoryPort.findByCode(dto.getCode()).isPresent()) {
            throw new ResourceAlreadyExistsException("Product", dto.getCode());
        }
        validateReferences(dto);
        ProductModel model = productRequestMapper.toModel(dto);
        return productRepositoryPort.save(model);
    }

    @Override
    public ProductModel updateProduct(UUID id, ProductUpdateDto dto) {
        ProductModel existing = productRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        validateReferences(dto);
        productRequestMapper.updateModelFromDto(existing, dto);
        return productRepositoryPort.save(existing);
    }

    @Override
    public ProductModel getProductById(UUID id) {
        return productRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
    }

    @Override
    public ProductModel getProductByCode(String code) {
        return productRepositoryPort.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "Product not found with code: " + code));
    }

    @Override
    public void hardDeleteProduct(UUID id) {
        productRepositoryPort.deleteById(id);
    }

    @Override
    public ProductModel enableProduct(UUID id) {
        ProductModel product = getProductById(id);
        return productRepositoryPort.save(product.withEnabled(true));
    }


    @Override
    public ProductModel updateStock(UUID id, int stock) {
        ProductModel product = getProductById(id);
        return productRepositoryPort.save(product.withCurrentStock(stock));
    }

    @Override
    public Page<ProductModel> getAllProducts(Pageable pageable) {
        return productRepositoryPort.findAll(pageable);
    }

    @Override
    public Page<ProductModel> getAllProductsWithFilters(Pageable pageable, String code, String name, String description) {
        return productRepositoryPort.findAllWithFilters(pageable, code, name, description);
    }

    @Override
    public Page<ProductModel> getActiveProducts(Pageable pageable) {
        return productRepositoryPort.findByEnabled(true, pageable);
    }

    @Override
    public List<ProductModel> getInactiveProducts() {
        return productRepositoryPort.findByEnabled(false, Pageable.unpaged()).getContent();
    }

    @Override
    public List<ProductModel> getProductsByCategory(UUID categoryId) {
        return productRepositoryPort.findByCategoryId(categoryId);
    }

    @Override
    public List<ProductModel> getLowStockByCategory(UUID categoryId) {
        return productRepositoryPort.findByCategoryId(categoryId).stream()
                .filter(ProductModel::lowStock)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductModel> getLowStockProducts() {
        return productRepositoryPort.findAll().stream()
                .filter(ProductModel::lowStock)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductModel> getProductsByBrand(UUID brandId) {
        return productRepositoryPort.findByBrandId(brandId);
    }

    @Override
    public long countProducts() {
        return productRepositoryPort.findAll().size();
    }

    @Override
    public long countActiveProducts() {
        return productRepositoryPort.findByEnabled(true, Pageable.unpaged()).getTotalElements();
    }

    @Override
    public Optional<String> validateUnitCode(String code) {
        if (!productUnitPort.existsByCode(code)) {
            return Optional.empty();
        }
        return productUnitPort.findNameByCode(code);
    }

    private void validateReferences(@Valid ProductReference dto) {
        if (!productUnitPort.existsByCode(dto.getUnitCode())) {
            throw new ResourceNotFoundException("Product", "Invalid unit code: " + dto.getUnitCode());
        }
        if (!productBrandPort.existsById(dto.getBrandId())) {
            throw new ResourceNotFoundException("Product", "Invalid brand ID: " + dto.getBrandId());
        }
        if (!categoryRepositoryPort.existsById(dto.getCategoryId())) {
            throw new ResourceNotFoundException("Product", "Invalid category ID: " + dto.getCategoryId());
        }
    }

    @Override
    public boolean existsById(UUID id) {
        return productRepositoryPort.existsById(id); // ✅
    }

    @Override
    public boolean existsByCode(String code) {
        return productRepositoryPort.findByCode(code).isPresent(); // ✅
    }

    @Override
    public List<ProductModel> getAll() {
        return productRepositoryPort.findAll();
    }

    @Override
    public ProductModel disableProduct(UUID id) {
        ProductModel product = productRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "No se encontró el producto con ID: " + id));

        ProductModel updatedProduct = product.withEnabled(false);

        return productRepositoryPort.save(updatedProduct);
    }


}