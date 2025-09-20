package com.celotts.productservice.application.usecase.movement;

import com.celotts.productservice.domain.model.movement.InventoryMovementModel;
import com.celotts.productservice.domain.model.movement.MovementPurpose;
import com.celotts.productservice.domain.model.movement.MovementType;
import com.celotts.productservice.domain.port.input.inventory.MovementUseCase;
import com.celotts.productservice.domain.port.output.inventory.InventoryMovementRepositoryPort;
import java.math.BigDecimal;
import java.sql.ResultSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MovementUseCaseImpl implements MovementUseCase {

    private final InventoryMovementRepositoryPort movRepo;
    private final JdbcTemplate jdbc;

    @Override
    public InventoryMovementModel registerIn(UUID productId, UUID lotId, BigDecimal qty, String reference, String user) {
        InventoryMovementModel m = new InventoryMovementModel(
                null, productId, lotId, MovementType.IN, MovementPurpose.PURCHASE,
                qty, reference, "Entrada manual", null, null
        );
        return movRepo.save(m);
    }

    @Override
    public InventoryMovementModel registerAdjust(UUID productId, UUID lotId, BigDecimal qty, String reason, String user) {
        InventoryMovementModel m = new InventoryMovementModel(
                null, productId, lotId, MovementType.ADJUST, MovementPurpose.COUNT,
                qty, null, reason, null, null
        );
        return movRepo.save(m);
    }

    @Override
    public Stream<LotTake> consumeToProduction(UUID productId, BigDecimal qty, String ref, String user) {
        List<LotTake> list = jdbc.query(
                "SELECT lot_id, taken FROM sp_consume_to_production(?, ?, ?, ?)",
                (ResultSet rs, int i) -> new LotTake(UUID.fromString(rs.getString("lot_id")), rs.getBigDecimal("taken")),
                productId, qty, ref, user
        );
        return list.stream();
    }
}