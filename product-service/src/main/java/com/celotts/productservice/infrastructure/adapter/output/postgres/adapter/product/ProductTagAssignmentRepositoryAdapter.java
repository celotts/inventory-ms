package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.product;

import com.celotts.productservice.domain.exception.product.ProductTagAssignmentNotFoundException;
import com.celotts.productservice.domain.model.product.ProductTagAssignmentModel;
import com.celotts.productservice.domain.port.output.product.ProductTagAssignmentRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTagAssignmentEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product.ProductTagAssignmentEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductTagAssignmentJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductTagAssignmentRepositoryAdapter implements ProductTagAssignmentRepositoryPort {

    private final ProductTagAssignmentJpaRepository repository;
    private final ProductTagAssignmentEntityMapper mapper;

    @Override
    @Transactional
    public ProductTagAssignmentModel save(ProductTagAssignmentModel model) {
        ProductTagAssignmentEntity saved = repository.save(mapper.toEntity(model));
        return mapper.toModel(saved);
    }

    @Override
    public Optional<ProductTagAssignmentModel> findById(UUID id) {
        return repository.findById(id).map(mapper::toModel);
    }

    @Override
    public Page<ProductTagAssignmentModel> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toModel);
    }

    @Override
    public Page<ProductTagAssignmentModel> findByEnabled(boolean enabled, Pageable pageable) {
        return repository.findByEnabled(enabled, pageable)
                .map(mapper::toModel);
    }

    @Override
    public long countByEnabled(boolean enabled) {
        return repository.countByEnabled(enabled);
    }

    @Override
    @Transactional
    public ProductTagAssignmentModel enable(UUID id) {
        ProductTagAssignmentEntity e = repository.findById(id)
                .orElseThrow(() -> new ProductTagAssignmentNotFoundException(id));
        e.setEnabled(true);
        return mapper.toModel(repository.save(e));
    }

    @Override
    @Transactional
    public ProductTagAssignmentModel disable(UUID id) {
        ProductTagAssignmentEntity e = repository.findById(id)
                .orElseThrow(() -> new ProductTagAssignmentNotFoundException(id));
        e.setEnabled(false);
        return mapper.toModel(repository.save(e));
    }

    @Override
    @Transactional
    public void deleteById(UUID id) {
        repository.deleteById(id);
    }
}