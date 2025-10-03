package com.celotts.productservice.domain.port.output.lot;


import com.celotts.productservice.domain.model.lot.LotModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface LotRepositoryPort {

    LotModel save(LotModel model);

    Optional<LotModel> findById(UUID id);

    Page<LotModel> findByProductId(UUID productId, Pageable pageable);

    void softDelete(UUID id, String user, String reason);

    // ✅ Nuevo: vencidos o que expiran en la ventana [now, until]
    Page<LotModel> findExpiredOrExpiring(LocalDate now, LocalDate until, Pageable pageable);
}
