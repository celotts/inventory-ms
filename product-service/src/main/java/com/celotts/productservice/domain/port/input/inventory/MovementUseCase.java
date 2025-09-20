package com.celotts.productservice.domain.port.input.inventory;

import com.celotts.productservice.domain.model.movement.InventoryMovementModel;
import java.math.BigDecimal;
import java.util.UUID;
import java.util.stream.Stream;

public interface MovementUseCase {
    InventoryMovementModel registerIn(UUID productId, UUID lotId, BigDecimal qty, String reference, String user);
    InventoryMovementModel registerAdjust(UUID productId, UUID lotId, BigDecimal qty, String reason, String user);
    Stream<LotTake> consumeToProduction(UUID productId, BigDecimal qty, String ref, String user);
    record LotTake(UUID lotId, java.math.BigDecimal taken) {}
}
