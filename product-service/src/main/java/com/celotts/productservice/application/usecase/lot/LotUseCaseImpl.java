package com.celotts.productservice.application.usecase.lot;

import com.celotts.productservice.domain.model.lot.LotModel;
import com.celotts.productservice.domain.model.views.*;
import com.celotts.productservice.domain.port.input.inventory.LotUseCase;
import com.celotts.productservice.domain.port.output.lot.LotRepositoryPort;
import com.celotts.productservice.domain.port.output.read.perishable.PerishableReadPort;
import java.util.UUID;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

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

    @Override
    public void dispose(UUID lotId, String reference, String reason, String user) {
        jdbc.update("SELECT sp_dispose_lot(?, ?, ?, ?)", lotId, reference, reason, user);
    }

    @Override
    public Stream<LotsExpiringSoonView> markExpiredAndList(int horizonDays) {
        jdbc.queryForObject("SELECT sp_mark_expired_lots(?)", Integer.class, "system");
        return readPort
                .listLotsExpiringSoon(PageRequest.of(0, 200))
                .getContent()
                .stream();
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
