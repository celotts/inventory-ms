
package com.celotts.productservice.applications.service;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.ProductDtoMapper;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.ProductBrandPort;
import com.celotts.productservice.domain.port.ProductRepositoryPort;
import com.celotts.productservice.domain.port.ProductTypePort;
import com.celotts.productservice.domain.port.ProductUnitPort;
import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductRequestDTO;

import com.celotts.productservice.infrastructure.adapter.input.rest.dto.ProductUpdateDTO;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductAlreadyExistsException;
import com.celotts.productservice.infrastructure.adapter.input.rest.exception.ProductNotFoundException;
import com.celotts.productservice.infrastructure.adapter.input.rest.mapper.ProductRequestMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ProductService {

    @Qualifier("productAdapter")
    private final ProductRepositoryPort repository;
    private final ProductTypePort productTypePort;
    private final ProductUnitPort productUnitPort;
    private final ProductBrandPort productBrandPort;

    public ProductModel createProduct(ProductRequestDTO dto) {
        if (repository.findByCode(dto.getCode()).isPresent()) {
            throw new ProductAlreadyExistsException("Product code already exists: " + dto.getCode());
        }

        validateReferences(dto);

        ProductModel model = ProductModel.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .description(dto.getDescription())
                .productTypeCode(dto.getProductTypeCode())
                .unitCode(dto.getUnitCode())
                .brandId(dto.getBrandId())
                .minimumStock(dto.getMinimumStock())
                .currentStock(dto.getCurrentStock())
                .unitPrice(dto.getUnitPrice())
                .enabled(dto.getEnabled())
                .createdAt(LocalDateTime.now())
                .createdBy(dto.getCreatedBy())
                .withEnabled(true)
                .build();

        return repository.save(model);
    }

    public ProductModel updateProduct(UUID id, ProductRequestDTO dto) {
        ProductModel existing = getProductById(id);

        validateReferences(dto);

        // ⛔ Esto no funcionaba antes porque `dto` era `ProductRequestDTO`
        // ProductRequestMapper.updateModelFromDto(existing, dto);

        // ✅ Solución: Convertimos primero a ProductUpdateDTO
        ProductUpdateDTO updateDto = ProductDtoMapper.toUpdateDto(dto);
        ProductRequestMapper.updateModelFromDto(existing, updateDto);

        return repository.save(existing);

    }

    public ProductModel getProductById(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new ProductNotFoundException(id));
    }

    public ProductModel getProductByCode(String code) {
        return repository.findByCode(code).orElseThrow(() ->
                new ProductNotFoundException("Product not found with code: " + code));
    }

    public List<ProductModel> getAllProducts() {
        return repository.findAll();
    }

    public Page<ProductModel> getAllProducts(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Page<ProductModel> getAllProductsWithFilters(Pageable pageable, String code, String name, String description) {
        return repository.findAllWithFilters(pageable, code, name, description);
    }

    public void deleteProduct(UUID id) {
        repository.findById(id).orElseThrow(() ->
                new ProductNotFoundException(id));
    }

    public void hardDeleteProduct(UUID id) {
        repository.deleteById(id);
    }

    public ProductModel enableProduct(UUID id) {
        ProductModel product = getProductById(id);
        return repository.save(product.withEnabled(true));
    }

    public ProductModel disableProduct(UUID id) {
        ProductModel product = getProductById(id);
        return repository.save(product.withEnabled(false));
    }

    public ProductModel updateStock(UUID id, int stock) {
        ProductModel product = getProductById(id);
        return repository.save(product.withCurrentStock(stock));
    }

    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    public Page<ProductModel> getActiveProducts(Pageable pageable) {
        return repository.findByEnabled(true, pageable);
    }

    public List<ProductModel> getInactiveProducts() {
        // Usamos paginación con un límite grande (si quieres traer todo)
        Pageable pageable = Pageable.unpaged(); // o: PageRequest.of(0, Integer.MAX_VALUE)
        return repository.findByEnabled(false, pageable)
                .getContent(); // Extraemos los resultados
    }

    public List<ProductModel> getLowStockProducts() {
        return repository.findAll().stream()
                .filter(ProductModel::lowStock)
                .toList();
    }

    public List<ProductModel> getProductsByType(String typeCode) {
        return repository.findByProductTypeCode(typeCode);
    }

    public List<ProductModel> getProductsByBrand(UUID brandId) {
        return repository.findByBrandId(brandId);
    }

    public long countProducts() {
        return repository.findAll().size();
    }

    public long countActiveProducts() {
        return repository.findByEnabled(true, Pageable.unpaged()).getTotalElements();
    }

    private void validateReferences(ProductRequestDTO dto) {
        if (!productTypePort.existsByCode(dto.getProductTypeCode())) {
            throw new IllegalArgumentException("Invalid product type code: " + dto.getProductTypeCode());
        }

        if (!productUnitPort.existsByCode(dto.getUnitCode())) {
            throw new IllegalArgumentException("Invalid unit code: " + dto.getUnitCode());
        }

        if (!productBrandPort.existsById(dto.getBrandId())) {
            throw new IllegalArgumentException("Invalid brand ID: " + dto.getBrandId());
        }
    }



}
