package com.celotts.productservice.applications.usecase;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.category.output.CategoryRepositoryPort;
import com.celotts.productservice.domain.port.product.brand.output.ProductBrandRepositoryPort;
import com.celotts.productservice.domain.port.product.root.input.ProductUseCase;
import com.celotts.productservice.domain.port.product.root.output.ProductRepositoryPort;
import com.celotts.productservice.domain.port.product.unit.output.ProductUnitRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.product.ProductRequestDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductAlreadyExistsException;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductNotFoundException;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductDtoMapper;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductRequestMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductUseCaseImpl implements ProductUseCase {

    private final ProductRepositoryPort repository;
    private final ProductUnitRepositoryPort productUnitPort;
    private final ProductBrandRepositoryPort productBrandPort;
    private final CategoryRepositoryPort categoryPort;
    private final ProductRequestMapper productRequestMapper;

    @Autowired
    public ProductUseCaseImpl(
            ProductRepositoryPort repository,
            ProductUnitRepositoryPort productUnitPort,
            @Qualifier("productBrandAdapter") ProductBrandRepositoryPort productBrandPort,
            @Qualifier("categoryAdapter") CategoryRepositoryPort categoryPort,
            ProductRequestMapper productRequestMapper
    ) {
        this.repository = repository;
        this.productUnitPort = productUnitPort;
        this.productBrandPort = productBrandPort;
        this.categoryPort = categoryPort;
        this.productRequestMapper = productRequestMapper;
    }

    @Override
    public ProductModel createProduct(ProductRequestDTO dto) {
        if (repository.findByCode(dto.getCode()).isPresent()) {
            throw new ProductAlreadyExistsException("Product code already exists: " + dto.getCode());
        }
        validateReferences(dto);
        ProductModel model = productRequestMapper.toModel(dto);
        model.setCreatedAt(LocalDateTime.now());
        return repository.save(model);
    }

    @Override
    public ProductModel updateProduct(UUID id, ProductRequestDTO dto) {
        ProductModel existing = getProductById(id);
        validateReferences(dto);
        ProductRequestMapper.updateModelFromDto(existing, ProductDtoMapper.toUpdateDto(dto));
        return repository.save(existing);
    }

    @Override
    public ProductModel getProductById(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Override
    public ProductModel getProductByCode(String code) {
        return repository.findByCode(code)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with code: " + code));
    }

    @Override
    public void hardDeleteProduct(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public ProductModel enableProduct(UUID id) {
        ProductModel product = getProductById(id);
        return repository.save(product.withEnabled(true));
    }

    @Override
    public void disableProduct(UUID id) {
        ProductModel product = getProductById(id);
        repository.save(product.withEnabled(false));
    }

    @Override
    public ProductModel updateStock(UUID id, int stock) {
        ProductModel product = getProductById(id);
        return repository.save(product.withCurrentStock(stock));
    }

    @Override
    public Page<ProductModel> getAllProducts(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public Page<ProductModel> getAllProductsWithFilters(Pageable pageable, String code, String name, String description) {
        return repository.findAllWithFilters(pageable, code, name, description);
    }

    @Override
    public Page<ProductModel> getActiveProducts(Pageable pageable) {
        return repository.findByEnabled(true, pageable);
    }

    @Override
    public List<ProductModel> getInactiveProducts() {
        return repository.findByEnabled(false, Pageable.unpaged()).getContent();
    }

    @Override
    public List<ProductModel> getProductsByCategory(UUID categoryId) {
        return repository.findByCategoryId(categoryId);
    }

    @Override
    public List<ProductModel> getLowStockByCategory(UUID categoryId) {
        return repository.findByCategoryId(categoryId).stream()
                .filter(ProductModel::lowStock)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductModel> getLowStockProducts() {
        return repository.findAll().stream()
                .filter(ProductModel::lowStock)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductModel> getProductsByBrand(UUID brandId) {
        return repository.findByBrandId(brandId);
    }

    @Override
    public long countProducts() {
        return repository.findAll().size();
    }

    @Override
    public long countActiveProducts() {
        return repository.findByEnabled(true, Pageable.unpaged()).getTotalElements();
    }

    @Override
    public Optional<String> validateUnitCode(String code) {
        if (!productUnitPort.existsByCode(code)) {
            return Optional.empty();
        }
        return productUnitPort.findNameByCode(code);
    }

    private void validateReferences(ProductRequestDTO dto) {
        if (!productUnitPort.existsByCode(dto.getUnitCode())) {
            throw new ProductNotFoundException("Invalid unit code: " + dto.getUnitCode());
        }
        if (!productBrandPort.existsById(dto.getBrandId())) {
            throw new ProductNotFoundException("Invalid brand ID: " + dto.getBrandId());
        }
        if (!categoryPort.existsById(dto.getCategoryId())) {
            throw new ProductNotFoundException("Invalid category ID: " + dto.getCategoryId());
        }
    }
}