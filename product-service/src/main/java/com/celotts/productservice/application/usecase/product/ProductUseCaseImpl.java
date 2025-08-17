package com.celotts.productservice.application.usecase.product;

import com.celotts.productservice.domain.exception.ResourceAlreadyExistsException;
import com.celotts.productservice.domain.exception.ResourceNotFoundException;
import com.celotts.productservice.domain.model.product.ProductModel;
import com.celotts.productservice.domain.model.product.ProductReference;
import com.celotts.productservice.domain.port.input.product.ProductUseCase;
import com.celotts.productservice.domain.port.output.category.CategoryRepositoryPort;
import com.celotts.productservice.domain.port.output.product.ProductBrandRepositoryPort;
import com.celotts.productservice.domain.port.output.product.ProductRepositoryPort;
import com.celotts.productservice.domain.port.output.product.ProductUnitRepositoryPort;

import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.product.ProductRequestMapper;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
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
    private final ProductBrandRepositoryPort productBrandPort;   // ← puerto correcto
    private final CategoryRepositoryPort categoryRepositoryPort;
    private final ProductRequestMapper productRequestMapper;

    public ProductUseCaseImpl(
            ProductRepositoryPort productRepositoryPort,
            ProductUnitRepositoryPort productUnitPort,
            @Qualifier("productBrandAdapter") ProductBrandRepositoryPort productBrandPort,
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
    public ProductModel createProduct(ProductReference cmd) {
        if (productRepositoryPort.findByCode(cmd.getCode()).isPresent()) {
            throw new ResourceAlreadyExistsException("Product", cmd.getCode());
        }
        validateReferences(cmd);
        ProductModel toSave = productRequestMapper.toModel(cmd);
        return productRepositoryPort.save(toSave);
    }

    @Override
    public ProductModel updateProduct(UUID id, ProductReference cmd) {
        ProductModel existing = productRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", id));
        validateReferences(cmd);
        ProductModel incoming = productRequestMapper.toModel(cmd);
        ProductModel updated = existing.toBuilder()
                .code(incoming.getCode() != null ? incoming.getCode() : existing.getCode())
                .name(incoming.getName() != null ? incoming.getName() : existing.getName())
                .description(incoming.getDescription() != null ? incoming.getDescription() : existing.getDescription())
                .categoryId(incoming.getCategoryId() != null ? incoming.getCategoryId() : existing.getCategoryId())
                .unitCode(incoming.getUnitCode() != null ? incoming.getUnitCode() : existing.getUnitCode())
                .brandId(incoming.getBrandId() != null ? incoming.getBrandId() : existing.getBrandId())
                .minimumStock(incoming.getMinimumStock() != null ? incoming.getMinimumStock() : existing.getMinimumStock())
                .currentStock(incoming.getCurrentStock() != null ? incoming.getCurrentStock() : existing.getCurrentStock())
                .unitPrice(incoming.getUnitPrice() != null ? incoming.getUnitPrice() : existing.getUnitPrice())
                .enabled(incoming.getEnabled() != null ? incoming.getEnabled() : existing.getEnabled())
                .updatedBy(incoming.getUpdatedBy() != null ? incoming.getUpdatedBy() : existing.getUpdatedBy())
                .build();
        return productRepositoryPort.save(updated);
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
        if (Boolean.TRUE.equals(product.getEnabled())) {
            return product; // idempotente
        }
        ProductModel updated = product.toBuilder()
                .enabled(true)
                .build();
        return productRepositoryPort.save(updated);
    }

    @Override
    public ProductModel updateStock(UUID id, int stock) {
        // Si tu puerto tiene updateStock, úsalo:
        return productRepositoryPort.updateStock(id, stock);
        // Si NO lo tienes, descomenta esto y borra la línea de arriba:
        // ProductModel current = getProductById(id);
        // ProductModel updated = current.toBuilder().currentStock(stock).build();
        // return productRepositoryPort.save(updated);
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
        return productRepositoryPort.findActive(pageable);
    }

    @Override
    public List<ProductModel> getInactiveProducts() {
        return productRepositoryPort.findInactive(Pageable.unpaged()).getContent();
    }

    @Override
    public List<ProductModel> getProductsByCategory(UUID categoryId) {
        return productRepositoryPort.findByCategory(categoryId, Pageable.unpaged()).getContent();
    }

    @Override
    public List<ProductModel> getLowStockByCategory(UUID categoryId) {
        return getProductsByCategory(categoryId).stream()
                .filter(ProductModel::lowStock)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductModel> getLowStockProducts() {
        return productRepositoryPort.findAll(Pageable.unpaged()).getContent().stream()
                .filter(ProductModel::lowStock)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductModel> getProductsByBrand(UUID brandId) {
        return productRepositoryPort.findByBrand(brandId, Pageable.unpaged()).getContent();
    }

    @Override
    public long countProducts() {
        return productRepositoryPort.countAll();
    }

    @Override
    public long countActiveProducts() {
        return productRepositoryPort.countActive();
    }

    @Override
    public Optional<String> validateUnitCode(String code) {
        if (!productUnitPort.existsByCode(code)) return Optional.empty();
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
        return productRepositoryPort.existsById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return productRepositoryPort.existsByCode(code);
    }

    @Override
    public List<ProductModel> getAll() {
        return productRepositoryPort.findAll(Pageable.unpaged()).getContent();
    }

    @Override
    public ProductModel disableProduct(UUID id) {
        ProductModel product = productRepositoryPort.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product", "No se encontró el producto con ID: " + id));
        ProductModel updated = product.toBuilder().enabled(false).build();
        return productRepositoryPort.save(updated);
    }
}