package com.celotts.productservice.domain.port.output.inventory;

import com.celotts.productservice.domain.model.movement.InventoryMovementModel;

public interface InventoryMovementRepositoryPort {
    InventoryMovementModel save(InventoryMovementModel model);
}
