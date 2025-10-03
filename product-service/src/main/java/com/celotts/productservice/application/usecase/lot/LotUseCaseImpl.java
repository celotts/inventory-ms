package com.celotts.productservice.application.usecase.lot;

import com.celotts.productservice.domain.model.lot.LotModel;
import com.celotts.productservice.domain.model.views.LotAvailableView;
import com.celotts.productservice.domain.model.views.LotExpiredView;
import com.celotts.productservice.domain.port.input.inventory.LotUseCase;
import com.celotts.productservice.domain.port.output.lot.LotRepositoryPort;
import com.celotts.productservice.domain.port.output.read.perishable.PerishableReadPort;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LotUseCaseImpl implements LotUseCase {

    private final LotRepositoryPort lotRepo;
    private final PerishableReadPort readPort;
    private final JdbcTemplate jdbc;

    @Override
    public LotModel create(LotModel model) {
        return lotRepo.save(model);
    }

    @Override
    public Page<LotModel> listByProduct(UUID productId, Pageable pageable) {
        return lotRepo.findByProductId(productId, pageable);
    }

    // com.celotts.productservice.application.usecase.lot.LotUseCaseImpl

    @Override
    public Instant dispose(UUID lotId, String reference, String reason, String user) {
        Instant deletedAt = Instant.now();
        jdbc.update("SELECT sp_dispose_lot(?, ?, ?, ?)", lotId, reference, reason, user);
        return deletedAt;
    }

    @Override
    public Page<LotModel> markExpiredAndList(int horizonDays, Pageable pageable) {
        // Marca vencida con horizonte
        jdbc.queryForObject("SELECT sp_mark_expired_lots(?)", Integer.class, horizonDays);

        var now   = java.time.LocalDate.now();
        var until = now.plusDays(horizonDays);

        return lotRepo.findExpiredOrExpiring(now, until, pageable);
    }

    @Override
    public Page<LotAvailableView> listLotsAvailable(UUID productId, Pageable pageable) {
        return readPort.listLotsAvailable(productId, pageable);
    }

    @Override
    public Page<LotExpiredView> listLotsExpired(UUID productId, Pageable pageable) {
        return readPort.listLotsExpired(productId, pageable);
    }
}
