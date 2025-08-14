package com.celotts.productserviceOld.infrastructure.adapter.output.postgres.adapter.productTag;

import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.repository.product.ProductTagRepository;
import com.celotts.productserviceOld.domain.model.ProductTagModel;
import com.celotts.productserviceOld.domain.port.product.tag.output.ProductTagRepositoryPort;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductTagEntity;
import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.mapper.product.ProductTagEntityMapper;
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

    private final ProductTagRepository jpa;
    private final ProductTagEntityMapper mapper;

    @Override
    public ProductTagModel save(ProductTagModel model) {
        ProductTagEntity saved = jpa.save(mapper.toEntity(model));
        return mapper.toDomain(saved);
    }

    @Override public void deleteById(UUID id) { jpa.deleteById(id); }
    @Override public Optional<ProductTagModel> findById(UUID id) { return jpa.findById(id).map(mapper::toDomain); }
    @Override public Optional<ProductTagModel> findByName(String name){ return jpa.findByName(name).map(mapper::toDomain); }
    @Override public boolean existsByName(String name){ return jpa.existsByName(name); }

    @Override public Page<ProductTagModel> findAll(Pageable pageable){ return jpa.findAll(pageable).map(mapper::toDomain); }
    @Override public List<ProductTagModel> findByEnabled(boolean enabled){ return jpa.findByEnabled(enabled).stream().map(mapper::toDomain).toList(); }
    @Override public long countByEnabled(boolean enabled){ return jpa.countByEnabled(enabled); }
}