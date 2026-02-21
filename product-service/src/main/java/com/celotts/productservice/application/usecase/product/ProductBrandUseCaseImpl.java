package com.celotts.productservice.application.usecase.product;

import com.celotts.productservice.domain.exception.DomainException;
import com.celotts.productservice.domain.exception.brand.BrandNotFoundException;
import com.celotts.productservice.domain.exception.ResourceAlreadyExistsException;
import com.celotts.productservice.domain.model.product.ProductBrandModel;
import com.celotts.productservice.domain.port.input.product.ProductBrandUseCase;
import com.celotts.productservice.domain.port.output.product.ProductBrandRepositoryPort;
import com.celotts.productservice.infrastructure.common.error.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductBrandUseCaseImpl implements ProductBrandUseCase {

    private final ProductBrandRepositoryPort repository;

    private String norm(String s) { return s == null ? null : s.trim(); }

    @Override
    @Transactional
    public ProductBrandModel save(ProductBrandModel brand) {
        String name = norm(brand.getName());
        if (name == null || name.isBlank()) {
            throw new DomainException(ErrorCode.BAD_REQUEST, 400, "brand.name.required");
        }
        if (repository.existsByName(name)) {
            throw new ResourceAlreadyExistsException("brand.already-exists", name);
        }
        brand.setName(name);
        return repository.save(brand);
    }

    @Override
    @Transactional
    public ProductBrandModel update(UUID id, ProductBrandModel patch) {
        ProductBrandModel existing = repository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException(id));

        String newName = norm(patch.getName());
        if (newName != null) {
            if (!newName.equalsIgnoreCase(existing.getName()) && repository.existsByName(newName)) {
                throw new ResourceAlreadyExistsException("brand.already-exists", newName);
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
        return Optional.ofNullable(repository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException(id)));
    }

    @Override public Optional<ProductBrandModel> findByName(String name) { return repository.findByName(name); }
    @Override public List<ProductBrandModel> findAll() { return repository.findAll(); }
    @Override public boolean existsByName(String name) { return repository.existsByName(name); }

    @Override
    @Transactional
    public void deleteById(UUID id, String deletedBy, String reason) {
        ProductBrandModel brand = repository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException(id));
        if (brand.getDeletedAt() != null) return; // idempotente

        brand.deactivate();
        brand.setDeletedBy(deletedBy);
        brand.setDeletedReason(reason);
        brand.setDeletedAt(LocalDateTime.now());
        repository.save(brand);
    }

    @Override public boolean existsById(UUID id) { return repository.existsById(id); }
    @Override public Optional<String> findNameById(UUID id) { return repository.findById(id).map(ProductBrandModel::getName); }
    @Override public List<UUID> findAllIds() { return repository.findAll().stream().map(ProductBrandModel::getId).toList(); }

    @Override
    @Transactional
    public ProductBrandModel enableBrand(UUID id) {
        ProductBrandModel brand = repository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException(id));
        brand.activate();
        brand.setUpdatedAt(LocalDateTime.now());
        return repository.save(brand);
    }

    @Override
    @Transactional
    public ProductBrandModel disableBrand(UUID id) {
        ProductBrandModel brand = repository.findById(id)
                .orElseThrow(() -> new BrandNotFoundException(id));
        brand.deactivate();
        brand.setUpdatedAt(LocalDateTime.now());
        return repository.save(brand);
    }
}