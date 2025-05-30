// ProductService - Servicio mejorado con validaciones y manejo de excepciones
package com.celotts.productservice.applications.service;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.ProductRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductRequestDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductAlreadyExistsException;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductNotFoundException;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.ProductRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {

    private final ProductRepositoryPort productRepositoryPort;

    @Transactional(readOnly = true)
    public List<ProductModel> getAllProducts() {
        log.debug("Fetching all products");
        return productRepositoryPort.findAll();
    }

    @Transactional(readOnly = true)
    public Page<ProductModel> getAllProducts(Pageable pageable) {
        log.debug("Fetching products with pagination: {}", pageable);
        return productRepositoryPort.findAll(pageable);
    }

    @Transactional(readOnly = true)
    public ProductModel getProductById(UUID id) {
        log.debug("Fetching product with id: {}", id);
        return productRepositoryPort.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    @Transactional(readOnly = true)
    public Optional<ProductModel> findProductById(UUID id) {
        return productRepositoryPort.findById(id);
    }

    @Transactional(readOnly = true)
    public ProductModel getProductByCode(String code) {
        log.debug("Fetching product with code: {}", code);
        return productRepositoryPort.findByCode(code)
                .orElseThrow(() -> new ProductNotFoundException("Product not found with code: " + code));
    }

    public ProductModel createProduct(ProductRequestDTO requestDTO) {
        log.info("Creating new product with code: {}", requestDTO.getCode());

        // Validar que no existe un producto con el mismo código
        if (productRepositoryPort.findByCode(requestDTO.getCode()).isPresent()) {
            throw new ProductAlreadyExistsException("Product already exists with code: " + requestDTO.getCode());
        }

        // Usar el mapper existente en lugar de builder manual
        ProductModel productModel = ProductRequestMapper.toModel(requestDTO);

        ProductModel savedProduct = productRepositoryPort.save(productModel);
        log.info("Product created successfully with id: {}", savedProduct.getId());

        return savedProduct;
    }

    public ProductModel updateProduct(UUID id, ProductRequestDTO requestDTO) {
        log.info("Updating product with id: {}", id);

        ProductModel existingProduct = getProductById(id);

        // Validar que no existe otro producto con el mismo código
        Optional<ProductModel> productWithSameCode = productRepositoryPort.findByCode(requestDTO.getCode());
        if (productWithSameCode.isPresent() && !productWithSameCode.get().getId().equals(id)) {
            throw new ProductAlreadyExistsException(requestDTO.getCode());
        }

        // Crear nuevo modelo actualizado usando el mapper existente
        ProductModel updatedModel = ProductRequestMapper.toModel(requestDTO);

        // Preservar campos de auditoría del producto existente
        updatedModel.setId(existingProduct.getId());
        updatedModel.setCreatedAt(existingProduct.getCreatedAt());
        updatedModel.setCreatedBy(existingProduct.getCreatedBy());

        // Campos de actualización
        updatedModel.setUpdatedAt(LocalDateTime.now());
        updatedModel.setUpdatedBy(requestDTO.getUpdatedBy() != null ? requestDTO.getUpdatedBy() : "system");

        ProductModel savedProduct = productRepositoryPort.save(updatedModel);
        log.info("Product updated successfully with id: {}", savedProduct.getId());

        return savedProduct;
    }

    public void deleteProduct(UUID id) {
        log.info("Soft deleting product with id: {}", id);

        ProductModel existingProduct = getProductById(id);

        // Soft delete - solo deshabilitamos el producto
        existingProduct.setEnabled(false);
        existingProduct.setUpdatedAt(LocalDateTime.now());
        existingProduct.setUpdatedBy("system");

        productRepositoryPort.save(existingProduct);
        log.info("Product soft deleted successfully with id: {}", id);
    }

    public void hardDeleteProduct(UUID id) {
        log.warn("Hard deleting product with id: {}", id);

        if (!productRepositoryPort.existsById(id)) {
            throw new ProductNotFoundException(id);
        }

        productRepositoryPort.deleteById(id);
        log.warn("Product hard deleted successfully with id: {}", id);
    }

    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        return productRepositoryPort.existsById(id);
    }

    @Transactional(readOnly = true)
    public List<ProductModel> getProductsByType(String productTypeCode) {
        log.debug("Fetching products by type: {}", productTypeCode);
        return productRepositoryPort.findByProductTypeCode(productTypeCode);
    }

    @Transactional(readOnly = true)
    public List<ProductModel> getProductsByBrand(UUID brandId) {
        log.debug("Fetching products by brand: {}", brandId);
        return productRepositoryPort.findByBrandId(brandId);
    }

    @Transactional(readOnly = true)
    public List<ProductModel> getActiveProducts() {
        log.debug("Fetching active products");
        return productRepositoryPort.findByEnabled(true);
    }

    @Transactional(readOnly = true)
    public List<ProductModel> getInactiveProducts() {
        log.debug("Fetching inactive products");
        return productRepositoryPort.findByEnabled(false);
    }

    @Transactional(readOnly = true)
    public List<ProductModel> getLowStockProducts() {
        log.debug("Fetching products with low stock");
        return productRepositoryPort.findByCurrentStockLessThanMinimumStock();
    }

    @Transactional(readOnly = true)
    public List<ProductModel> getProductsWithStockLessThan(Integer stock) {
        log.debug("Fetching products with stock less than: {}", stock);
        return productRepositoryPort.findByCurrentStockLessThan(stock);
    }

    @Transactional(readOnly = true)
    public long countProducts() {
        return productRepositoryPort.count();
    }

    @Transactional(readOnly = true)
    public long countActiveProducts() {
        return productRepositoryPort.countByEnabled(true);
    }

    public ProductModel updateStock(UUID id, Integer newStock) {
        log.info("Updating stock for product id: {} to: {}", id, newStock);

        ProductModel product = getProductById(id);
        product.setCurrentStock(newStock);
        product.setUpdatedAt(LocalDateTime.now());
        product.setUpdatedBy("stock-system");

        return productRepositoryPort.save(product);
    }

    public ProductModel enableProduct(UUID id) {
        log.info("Enabling product with id: {}", id);

        ProductModel product = getProductById(id);
        product.setEnabled(true);
        product.setUpdatedAt(LocalDateTime.now());
        product.setUpdatedBy("system");

        return productRepositoryPort.save(product);
    }

    public ProductModel disableProduct(UUID id) {
        log.info("Disabling product with id: {}", id);

        ProductModel product = getProductById(id);
        product.setEnabled(false);
        product.setUpdatedAt(LocalDateTime.now());
        product.setUpdatedBy("system");

        return productRepositoryPort.save(product);
    }
}
