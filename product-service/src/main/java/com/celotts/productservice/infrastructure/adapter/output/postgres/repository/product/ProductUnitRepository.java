package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductUnitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repositorio JPA para la tabla product_unit.
 * Usa UUID como clave primaria.
 */
public interface ProductUnitRepository extends JpaRepository<ProductUnitEntity, UUID> {

    /* -------- lecturas “light” -------- */

    boolean existsByCode(String code);

    @Query("select pu.name from ProductUnitEntity pu where pu.code = :code")
    Optional<String> findNameByCode(@Param("code") String code);

    /** Devuelve únicamente la lista de códigos existentes */
    @Query("select pu.code from ProductUnitEntity pu")
    List<String> findAllCodes();
}