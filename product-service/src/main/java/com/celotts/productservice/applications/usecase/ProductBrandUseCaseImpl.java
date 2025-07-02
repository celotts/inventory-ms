package com.celotts.productservice.applications.usecase;

import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.domain.port.product_brand.ProductBrandRepositoryPort;
import com.celotts.productservice.domain.port.product_brand.ProductBrandUseCase;

import com.celotts.productservice.infrastructure.adapter.input.rest.exception.BrandNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ProductBrandUseCaseImpl implements ProductBrandUseCase {

    private final ProductBrandRepositoryPort repository;

    public ProductBrandUseCaseImpl(
            @Qualifier("productBrandAdapter") ProductBrandRepositoryPort repository
    ) {
        this.repository = repository;
    }

    @Override
    public ProductBrandModel save(ProductBrandModel brand) {
        if (repository.existsByName(brand.getName())) {
            throw new IllegalArgumentException("Brand already exists");
        }
        return repository.save(brand);
    }

    @Override
    public Optional<ProductBrandModel> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Optional<ProductBrandModel> findByName(String name) {
        return repository.findByName(name);
    }

    @Override
    public List<ProductBrandModel> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean existsByName(String name) {
        return repository.existsByName(name);
    }

    @Override
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(UUID id) {
        return repository.existsById(id);
    }

    @Override
    public Optional<String> findNameById(UUID id) {
        return repository.findById(id).map(ProductBrandModel::getName);
    }

    @Override
    public List<UUID> findAllIds() {
        return repository.findAll().stream()
                .map(ProductBrandModel::getId)
                .toList();
    }

    @Override
    public ProductBrandModel enableBrand(UUID id) {
        ProductBrandModel brand = repository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException(id));

        brand.activate();  // Aquí usas el método del modelo
        brand.setUpdatedAt(LocalDateTime.now());
        return repository.save(brand);
    }

    @Override
    public ProductBrandModel disableBrand(UUID id) {
        ProductBrandModel brand = repository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException(id));

        brand.deactivate();  // Aquí usas el método del modelo
        brand.setUpdatedAt(LocalDateTime.now());
        return repository.save(brand);
    }
}