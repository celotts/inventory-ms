package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.inventory;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.movement.InventoryMovementEntity;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryMovementJpaRepository extends JpaRepository<InventoryMovementEntity, UUID> {}
