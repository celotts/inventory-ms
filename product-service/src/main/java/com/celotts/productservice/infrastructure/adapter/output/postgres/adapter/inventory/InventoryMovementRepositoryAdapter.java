package com.celotts.productservice.infrastructure.adapter.output.postgres.adapter.movement;

import com.celotts.productservice.domain.model.movement.InventoryMovementModel;
import com.celotts.productservice.domain.port.output.inventory.InventoryMovementRepositoryPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.movement.InventoryMovementEntity;
import com.celotts.productservice.infrastructure.adapter.output.postgres.mapper.movement.InventoryMovementEntityMapper;
import com.celotts.productservice.infrastructure.adapter.output.postgres.repository.inventory.InventoryMovementJpaRepository;
import java.time.Instant;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryMovementRepositoryAdapter implements InventoryMovementRepositoryPort {

    private final InventoryMovementJpaRepository jpa;
    private final InventoryMovementEntityMapper mapper;

    @Override
    public InventoryMovementModel save(InventoryMovementModel model) {
        InventoryMovementEntity e = mapper.toEntity(model);
        if (e.getId() == null) e.setId(UUID.randomUUID());
        if (e.getCreatedAt() == null) e.setCreatedAt(Instant.now());
        return mapper.toModel(jpa.save(e));
    }
}