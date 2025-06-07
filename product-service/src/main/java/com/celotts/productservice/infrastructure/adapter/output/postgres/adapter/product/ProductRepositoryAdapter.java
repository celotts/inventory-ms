package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.domain.port.product.ProductRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product.ProductEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductRepositoryAdapter implements ProductRepositoryPort {

    private final ProductRepository productRepository;
    private final ProductEntityMapper productEntityMapper;

    @Override
    public boolean existsById(UUID id) {
        return productRepository.existsById(id);
    }

    @Override
    public boolean existsByCode(String code) {
        return productRepository.existsByCode(code);
    }

    @Override
    public Optional<ProductModel> findByCode(String code) {
        return productRepository.findByCode(code)
                .map(productEntityMapper::toModel);
    }

    // ✅ CAMBIAR: productTypeCode por categoryId
    @Override
    public List<ProductModel> findByCategoryId(UUID categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream()
                .map(productEntityMapper::toModel)
                .toList();
    }



    @Override
    public List<ProductModel> findByBrandId(UUID brandId) {
        return productRepository.findByBrandId(brandId).stream()
                .map(productEntityMapper::toModel)
                .toList();
    }


    @Override
    public Page<ProductModel> findByEnabled(Boolean enabled, Pageable pageable) {
        return productRepository.findByEnabled(enabled, pageable)
                .map(productEntityMapper::toModel);
    }

    @Override
    public ProductModel save(ProductModel product) {
        return productEntityMapper.toModel(
                productRepository.save(productEntityMapper.toEntity(product))
        );
    }

    @Override
    public List<ProductModel> findAll() {
        return productRepository.findAll()
                .stream()
                .map(productEntityMapper::toModel)
                .toList();
    }

    @Override
    public Page<ProductModel> findAll(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productEntityMapper::toModel);
    }

    @Override
    public Page<ProductModel> findAllWithFilters(Pageable pageable, String code, String name, String description) {
        // ✅ Cambiar solo esta línea (cambiar el orden):
        return productRepository.findAllWithFilters(pageable, code, name, description)
                .map(productEntityMapper::toModel);
        //                                         ↑ Mover pageable al inicio
    }

    @Override
    public Optional<ProductModel> findById(UUID id) {
        return productRepository.findById(id)
                .map(productEntityMapper::toModel);
    }

    @Override
    public void deleteById(UUID id) {
        productRepository.deleteById(id);
    }




}