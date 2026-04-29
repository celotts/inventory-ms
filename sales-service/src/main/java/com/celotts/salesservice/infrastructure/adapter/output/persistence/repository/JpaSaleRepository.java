package com.celotts.salesservice.infrastructure.adapter.output.persistence.repository;

import com.celotts.salesservice.infrastructure.adapter.output.persistence.entity.SaleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface JpaSaleRepository extends JpaRepository<SaleEntity, UUID> {
    // Aquí puedes añadir búsquedas personalizadas, por ejemplo, por fecha o por origen (Rappi, etc.)
}