// com.celotts.productservice.infrastructure.adapter.output.postgres.repository.lot.LotJpaRepository
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

    // ✅ Nuevo: incluye vencidos y los que expiran hasta 'until', excluye borrados lógicos
    @Query("""
           select l
           from LotEntity l
           where l.deletedAt is null
             and (
                   l.expirationDate <= :now
                or (l.expirationDate > :now and l.expirationDate <= :until)
             )
           """)
    Page<LotEntity> findExpiredOrExpiring(@Param("now") LocalDate now,
                                          @Param("until") LocalDate until,
                                          Pageable pageable);
}