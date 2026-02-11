package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.lot;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.lot.LotEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface LotJpaRepository extends JpaRepository<LotEntity, UUID> {

    Optional<LotEntity> findByIdAndDeletedAtIsNull(UUID id);

    Page<LotEntity> findByProductIdAndDeletedAtIsNull(UUID productId, Pageable pageable);

    @Query(value = """
           SELECT l
           FROM LotEntity l
           WHERE l.deletedAt IS NULL
             AND (l.expirationDate <= :until)
           """,
            countQuery = """
           SELECT COUNT(l)
           FROM LotEntity l
           WHERE l.deletedAt IS NULL
             AND (l.expirationDate <= :until)
           """)
    Page<LotEntity> findExpiredOrExpiring(@Param("until") LocalDate until, Pageable pageable);
}
