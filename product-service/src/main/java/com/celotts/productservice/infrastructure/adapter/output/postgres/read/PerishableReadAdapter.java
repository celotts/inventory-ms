package com.celotts.productservice.infrastructure.adapter.output.postgres.read;

import com.celotts.productservice.domain.model.views.*;
import com.celotts.productservice.domain.port.output.read.perishable.PerishableReadPort;
import com.celotts.productservice.infrastructure.adapter.output.postgres.read.repository.PerishableReadRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PerishableReadAdapter implements PerishableReadPort {

    private final PerishableReadRepository repo;

    @Override
    public Page<ProductStockAvailableView> listProductStockAvailable(Pageable pageable, String q) {
        return repo.listProductStockAvailable(pageable, q);
    }

    @Override
    public Page<LotsExpiringSoonView> listLotsExpiringSoon(Pageable pageable) {
        return repo.listLotsExpiringSoon(pageable);
    }

    @Override
    public Page<LotAvailableView> listLotsAvailable(UUID productId, Pageable pageable) {
        return repo.listLotsAvailable(productId, pageable);
    }

    @Override
    public Page<LotExpiredView> listLotsExpired(UUID productId, Pageable pageable) {
        return repo.listLotsExpired(productId, pageable);
    }
}