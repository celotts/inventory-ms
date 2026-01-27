package com.celotts.purchaseservice.infrastructure.adapter.output.postgres.repository;

import com.celotts.purchaseservice.infrastructure.adapter.output.postgres.entity.PurchaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurchaseJpaRepository extends JpaRepository<PurchaseEntity, UUID> {

    Optional<PurchaseEntity> findByOrderNumberIgnoreCase(String orderNumber);

    boolean existsByOrderNumberIgnoreCase(String orderNumber);

    Page<PurchaseEntity> findBySupplierId(UUID supplierId, Pageable pageable);

    Page<PurchaseEntity> findByStatus(String status, Pageable pageable);

    @Query("SELECT p FROM PurchaseEntity p WHERE " +
            "LOWER(p.orderNumber) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.notes) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<PurchaseEntity> searchByLooseQuery(@Param("query") String query, Pageable pageable);
}