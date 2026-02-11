package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.lot;

import com.celotts.productservice.domain.model.lot.LotModel;
import com.celotts.productservice.domain.port.output.lot.LotRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.lot.LotEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.lot.LotEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.lot.LotJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LotRepositoryAdapter implements LotRepositoryPort {

    private final LotJpaRepository jpa;
    private final LotEntityMapper mapper;

    @Override
    public LotModel save(LotModel model) {
        LotEntity e = mapper.toEntity(model);
        if (e.getId() == null) e.setId(UUID.randomUUID());
        if (e.getCreatedAt() == null) e.setCreatedAt(Instant.now());
        return mapper.toModel(jpa.save(e));
    }

    @Override
    public Optional<LotModel> findById(UUID id) {
        return jpa.findByIdAndDeletedAtIsNull(id).map(mapper::toModel);
    }

    @Override
    public Page<LotModel> findByProductId(UUID productId, Pageable pageable) {
        return jpa.findByProductIdAndDeletedAtIsNull(productId, pageable).map(mapper::toModel);
    }

    @Override
    public void softDelete(UUID id, String user, String reason) {
        jpa.findById(id).ifPresent(e -> {
            e.setDeletedAt(Instant.now());
            e.setDeletedBy(user);
            e.setDeletedReason(reason);
            jpa.save(e);
        });
    }

    @Override
    public Page<LotModel> findExpiredOrExpiring(LocalDate now, LocalDate until, Pageable pageable) {
        return jpa.findExpiredOrExpiring(until, pageable)
                .map(mapper::toModel);
    }
}
