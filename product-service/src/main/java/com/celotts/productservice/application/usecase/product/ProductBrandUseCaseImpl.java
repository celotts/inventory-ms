package com.celotts.productservice.application.usecase.product;

import com.celotts.productservice.domain.port.output.product.ProductBrandRepositoryPort;
import com.celotts.productservice.domain.exception.BrandNotFoundException;
import com.celotts.productservice.domain.model.product.ProductBrandModel;
import com.celotts.productservice.domain.port.input.product.ProductBrandUseCase;

import org.springframework.transaction.annotation.Transactional;
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
    @Transactional
    public ProductBrandModel update(UUID id, ProductBrandModel patch) {
        ProductBrandModel existing = repository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException(id));

        // Si cambian el nombre, valida unicidad (excluyéndote a ti mismo)
        if (patch.getName() != null) {
            String newName = patch.getName().trim();
            if (!newName.equalsIgnoreCase(existing.getName())
                    && repository.existsByName(newName)) { // idealmente usa existsByNameExcludingId(id, newName)
                throw new IllegalArgumentException("Brand already exists");
            }
            existing.setName(newName);
        }

        if (patch.getDescription() != null) existing.setDescription(patch.getDescription());
        if (patch.getEnabled() != null)     existing.setEnabled(patch.getEnabled());
        if (patch.getUpdatedBy() != null)   existing.setUpdatedBy(patch.getUpdatedBy());

        existing.setUpdatedAt(LocalDateTime.now());
        return repository.save(existing);
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
    @Transactional
    public void deleteById(UUID id, String updatedBy, LocalDateTime updatedAt) {
        ProductBrandModel brand = repository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException(id));

        brand.deactivate();
        brand.setUpdatedAt(updatedAt != null ? updatedAt : LocalDateTime.now());
        brand.setUpdatedBy(updatedBy);

        repository.save(brand);
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