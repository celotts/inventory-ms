package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.lot;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.lot.LotEntity;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LotJpaRepository extends JpaRepository<LotEntity, UUID> {
    Page<LotEntity> findByProductIdAndDeletedAtIsNull(UUID productId, Pageable pageable);
    Optional<LotEntity> findByIdAndDeletedAtIsNull(UUID id);
}
