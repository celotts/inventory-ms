package com.celotts.purchaseservice.infrastructure.adapter.output.postgres.repository;

import com.celotts.purchaseservice.infrastructure.adapter.output.postgres.entity.PurchaseEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PurchaseJpaRepository extends JpaRepository<PurchaseEntity, UUID> {

    Optional<PurchaseEntity> findByCodeIgnoreCase(String code);

    boolean existsByCodeIgnoreCase(String code);

    boolean existsByNameIgnoreCase(String name);

    // Soporte para paginación en búsqueda por nombre
    Page<PurchaseEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    List<PurchaseEntity> findByEnabled(Boolean enabled);

    // Búsqueda personalizada para sugerencias/autocomplete
    @Query("SELECT p FROM PurchaseEntity p WHERE " +
            "LOWER(p.name) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(p.code) LIKE LOWER(CONCAT('%', :query, '%'))")
    Page<PurchaseEntity> searchByLooseQuery(@Param("query") String query, Pageable pageable);
}