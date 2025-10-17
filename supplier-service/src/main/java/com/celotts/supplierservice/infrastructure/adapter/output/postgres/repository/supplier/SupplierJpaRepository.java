package com.celotts.supplierservice.infrastructure.adapter.output.postgres.repository.supplier;

import com.celotts.supplierservice.infrastructure.adapter.output.postgres.entity.supplier.SupplierEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface SupplierJpaRepository extends JpaRepository<SupplierEntity, UUID> {

    Optional<SupplierEntity> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);

    // Listados simples
    List<SupplierEntity> findByNameContainingIgnoreCase(String name);

    // Paginado
    Page<SupplierEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Activo/Enabled
    List<SupplierEntity> findByEnabled(Boolean enabled);

    // Búsqueda “amplia” en name/code/email/address
    @Query("""
           select s
           from SupplierEntity s
           where lower(s.name) like lower(concat('%', :q, '%'))
              or lower(s.code) like lower(concat('%', :q, '%'))
              or lower(coalesce(s.email, '')) like lower(concat('%', :q, '%'))
              or lower(coalesce(s.address, '')) like lower(concat('%', :q, '%'))
           order by s.name asc
           """)
    Page<SupplierEntity> searchByLooseQuery(@Param("q") String query, Pageable pageable);
}