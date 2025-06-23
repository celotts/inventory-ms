package com.celotts.productservice.applications.usecase;

import com.celotts.productservice.domain.model.ProductBrandModel;
import com.celotts.productservice.domain.port.prodcut_brand.ProductBrandRepositoryPort;
import com.celotts.productservice.domain.port.prodcut_brand.ProductBrandUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductBrandUseCaseImpl implements ProductBrandUseCase {

    private final ProductBrandRepositoryPort repository;

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
}