package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.model.product.ProductTagAssignmentModel;
import com.celotts.productservice.domain.port.output.product.ProductTagAssignmentRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTagAssignmentEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product.ProductTagAssignmentEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductTagAssignmentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProductTagAssignmentRepositoryAdapter implements ProductTagAssignmentRepositoryPort {

    private final ProductTagAssignmentJpaRepository jpa;
    private final ProductTagAssignmentEntityMapper mapper;

    @Override
    public ProductTagAssignmentModel save(ProductTagAssignmentModel model) {
        ProductTagAssignmentEntity saved = jpa.save(mapper.toEntity(model));
        return mapper.toModel(saved);
    }

    @Override
    public Optional<ProductTagAssignmentModel> findById(UUID id) {
        return jpa.findById(id).map(mapper::toModel);
    }

    // ⚠️ En assignment no hay "name". Implementación neutra para que compile.
    @Override
    public Optional<ProductTagAssignmentModel> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public boolean existByName(String name) {
        return false;
    }

    @Override
    public Page<ProductTagAssignmentModel> findAll(Pageable pageable) {
        return jpa.findAll(pageable).map(mapper::toModel);
    }

    @Override
    public List<ProductTagAssignmentModel> findByEnabled(boolean enabled) {
        return jpa.findByEnabled(enabled).stream()
                .map(mapper::toModel)
                .toList();
    }

    @Override
    public long countByEnabled(boolean enabled) {
        return jpa.countByEnabled(enabled);
    }

    @Override
    public ProductTagAssignmentModel enable(UUID id) {
        ProductTagAssignmentEntity e = jpa.findById(id).orElseThrow();
        e.setEnabled(true);
        return mapper.toModel(jpa.save(e));
    }

    @Override
    public ProductTagAssignmentModel disable(UUID id) {
        ProductTagAssignmentEntity e = jpa.findById(id).orElseThrow();
        e.setEnabled(false);
        return mapper.toModel(jpa.save(e));
    }

    @Override
    public void deleteById(UUID id) {
        jpa.deleteById(id);
    }
}