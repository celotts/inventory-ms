package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.model.product.ProductBrandModel;
import com.celotts.productservice.domain.port.output.product.ProductBrandRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductBrandEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product.ProductBrandEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductBrandJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component("productBrandAdapter")
@RequiredArgsConstructor
public class ProductBrandRepositoryAdapter implements ProductBrandRepositoryPort {

    private final ProductBrandJpaRepository repository;
    private final ProductBrandEntityMapper mapper;

    @Override
    public ProductBrandModel save(ProductBrandModel model) {
        ProductBrandEntity saved = repository.save(mapper.toEntity(model));
        return mapper.toModel(saved);
    }

    // ===== Lecturas SOLO ACTIVOS =====

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductBrandModel> findById(UUID id) {
        return repository.findActiveById(id).map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ProductBrandModel> findByName(String name) {
        return repository.findActiveByNameIgnoreCase(name).map(mapper::toModel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProductBrandModel> findAll() {
        return repository.findAllActive().stream().map(mapper::toModel).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByName(String name) {
        return repository.existsActiveByNameIgnoreCase(name);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsById(UUID id) {
        // si quieres estricto por activos:
        return repository.findActiveById(id).isPresent();
        // o bien: return repository.existsById(id); (incluye borrados)
    }

    // ===== Soft delete =====

    @Override
    @Transactional
    public int softDelete(UUID id, String deletedBy, String reason) {
        return repository.softDelete(id, deletedBy, reason);
    }

    // ===== Extras SOLO ACTIVOS =====

    @Override
    @Transactional(readOnly = true)
    public Optional<String> findNameById(UUID id) {
        return repository.findActiveNameById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<UUID> findAllIds() {
        return repository.findAllActiveIds();
    }

    // ===== enable/disable restringidos a activos =====

    @Override
    @Transactional
    public ProductBrandModel enable(UUID id) {
        int updated = repository.enableBrandById(id);
        if (updated == 0) throw new EntityNotFoundException("ProductBrand not found or deleted: " + id);

        ProductBrandEntity entity = repository.findActiveById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductBrand not found: " + id));
        return mapper.toModel(entity);
    }

    @Override
    @Transactional
    public ProductBrandModel disable(UUID id) {
        int updated = repository.disableBrandById(id);
        if (updated == 0) throw new EntityNotFoundException("ProductBrand not found or deleted: " + id);

        ProductBrandEntity entity = repository.findActiveById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductBrand not found: " + id));
        return mapper.toModel(entity);
    }

    @Override
    @Transactional
    public void deleteById(UUID id){
       repository.softDelete(id, null, null);

    }
}