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

    // ---- Lookups por name
    Optional<SupplierEntity> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);
    List<SupplierEntity> findByNameContainingIgnoreCase(String name);
    Page<SupplierEntity> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // ---- Lookups por code (recomendado como único)
    Optional<SupplierEntity> findByCodeIgnoreCase(String code);
    boolean existsByCodeIgnoreCase(String code);

    // ---- Enabled / Active
    List<SupplierEntity> findByEnabled(Boolean enabled);
    Page<SupplierEntity> findByEnabled(Boolean enabled, Pageable pageable);

    // ---- Búsqueda amplia (name, code, email, address) sin acentos
    // Requiere extensiones: unaccent (y opcionalmente pg_trgm para performance)
    @Query(value = """
        select *
        from supplier s
        where (:q is not null and trim(:q) <> '')
          and (
            unaccent(lower(s.name))    like unaccent(lower(concat('%', :q, '%')))
         or unaccent(lower(s.code))    like unaccent(lower(concat('%', :q, '%')))
         or unaccent(lower(coalesce(s.email, '')))   like unaccent(lower(concat('%', :q, '%')))
         or unaccent(lower(coalesce(s.address, ''))) like unaccent(lower(concat('%', :q, '%')))
          )
        order by s.name asc
        """,
            countQuery = """
        select count(*)
        from supplier s
        where (:q is not null and trim(:q) <> '')
          and (
            unaccent(lower(s.name))    like unaccent(lower(concat('%', :q, '%')))
         or unaccent(lower(s.code))    like unaccent(lower(concat('%', :q, '%')))
         or unaccent(lower(coalesce(s.email, '')))   like unaccent(lower(concat('%', :q, '%')))
         or unaccent(lower(coalesce(s.address, ''))) like unaccent(lower(concat('%', :q, '%')))
          )
        """,
            nativeQuery = true)
    Page<SupplierEntity> searchByLooseQuery(@Param("q") String q, Pageable pageable);
}