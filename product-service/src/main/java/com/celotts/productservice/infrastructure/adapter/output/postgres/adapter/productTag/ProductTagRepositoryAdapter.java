package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.productTag;

import com.celotts.productservice.domain.model.ProductTagModel;
import com.celotts.productservice.domain.port.output.product.ProductTagRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductTagEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.product.ProductTagEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product.ProductTagJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class ProductTagRepositoryAdapter implements ProductTagRepositoryPort {

    private final ProductTagJpaRepository jpa;
    private final ProductTagEntityMapper mapper;

    @Override
    public ProductTagModel save(ProductTagModel model) {
        ProductTagEntity saved = jpa.save(mapper.toEntity(model));
        return mapper.toModel(saved); // o toDomain si tu mapper se llama así
    }

    @Override public void deleteById(UUID id) { jpa.deleteById(id); }

    @Override public Optional<ProductTagModel> findById(UUID id) {
        return jpa.findById(id).map(mapper::toModel);
    }

    @Override public Optional<ProductTagModel> findByName(String name) {
        return jpa.findByName(name).map(mapper::toModel);
    }

    @Override public boolean existsByName(String name) { return jpa.existsByName(name); }

    @Override public Page<ProductTagModel> findAll(Pageable pageable) {
        return jpa.findAll(pageable).map(mapper::toModel);
    }

    @Override public List<ProductTagModel> findByEnabled(boolean enabled) {
        return jpa.findByEnabled(enabled).stream().map(mapper::toModel).toList();
    }

    @Override public long countByEnabled(boolean enabled) { return jpa.countByEnabled(enabled); }

    @Override
    @Transactional
    public ProductTagModel enable(UUID id) {
        ProductTagEntity entity = jpa.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductTag not found: " + id));
        entity.setEnabled(true);
        return mapper.toModel(jpa.save(entity));
    }

    @Override
    @Transactional
    public ProductTagModel disable(UUID id) {
        ProductTagEntity entity = jpa.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("ProductTag not found: " + id));
        entity.setEnabled(false);
        return mapper.toModel(jpa.save(entity));
    }
}