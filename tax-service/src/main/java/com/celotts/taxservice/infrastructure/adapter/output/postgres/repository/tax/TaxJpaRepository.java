package com.celotts.taxservice.infrastructure.adapter.output.postgres.repository.tax;

import com.celotts.taxservice.infrastructure.adapter.output.postgres.entity.TaxEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TaxJpaRepository extends JpaRepository<TaxEntity, UUID> {

    // ---------- Búsqueda por código ----------
    Optional<TaxEntity> findByCode(String code);

    boolean existsByCode(String code);

    // ---------- Búsqueda por nombre ----------
    Optional<TaxEntity> findByName(String name);

    boolean existsByName(String name);

    // ---------- Filtrado por estado (activo/inactivo) ----------
    Page<TaxEntity> findByIsActive(Boolean isActive, Pageable pageable);

    long countByIsActive(Boolean isActive);

    // ---------- Búsqueda case-insensitive por código ----------
    @Query("""
        SELECT t FROM TaxEntity t
        WHERE LOWER(t.code) LIKE LOWER(CONCAT('%', :code, '%'))
        ORDER BY t.createdAt DESC
    """)
    Page<TaxEntity> findByCodeIgnoreCase(@Param("code") String code, Pageable pageable);

    // ---------- Búsqueda case-insensitive por nombre ----------
    @Query("""
        SELECT t FROM TaxEntity t
        WHERE LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%'))
        ORDER BY t.createdAt DESC
    """)
    Page<TaxEntity> findByNameIgnoreCase(@Param("name") String name, Pageable pageable);

    // ---------- Rango de tasas ----------
    Page<TaxEntity> findByRateBetween(BigDecimal minRate, BigDecimal maxRate, Pageable pageable);

    // ---------- Vigencia en rango de fechas ----------
    @Query("""
        SELECT t FROM TaxEntity t
        WHERE t.validFrom <= :date
          AND (t.validTo IS NULL OR t.validTo >= :date)
    """)
    List<TaxEntity> findValidAtDate(@Param("date") LocalDate date);

    // ---------- Impuestos vigentes (válidos hoy) ----------
    @Query("""
        SELECT t FROM TaxEntity t
        WHERE t.validFrom <= CURRENT_DATE
          AND (t.validTo IS NULL OR t.validTo >= CURRENT_DATE)
          AND t.isActive = TRUE
        ORDER BY t.rate DESC
    """)
    Page<TaxEntity> findCurrentlyActive(Pageable pageable);

    // ---------- Búsqueda avanzada con filtros opcionales ----------
    @Query(value = """
        SELECT t FROM TaxEntity t
        WHERE (:code IS NULL OR LOWER(t.code) LIKE LOWER(CONCAT('%', :code, '%')))
          AND (:name IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:isActive IS NULL OR t.isActive = :isActive)
          AND (:minRate IS NULL OR t.rate >= :minRate)
          AND (:maxRate IS NULL OR t.rate <= :maxRate)
        ORDER BY t.rate DESC, t.code ASC
    """,
            countQuery = """
        SELECT COUNT(t) FROM TaxEntity t
        WHERE (:code IS NULL OR LOWER(t.code) LIKE LOWER(CONCAT('%', :code, '%')))
          AND (:name IS NULL OR LOWER(t.name) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:isActive IS NULL OR t.isActive = :isActive)
          AND (:minRate IS NULL OR t.rate >= :minRate)
          AND (:maxRate IS NULL OR t.rate <= :maxRate)
    """)
    Page<TaxEntity> findAllWithFilters(
            @Param("code") String code,
            @Param("name") String name,
            @Param("isActive") Boolean isActive,
            @Param("minRate") BigDecimal minRate,
            @Param("maxRate") BigDecimal maxRate,
            Pageable pageable);

    // ---------- Listar todos los activos (sin paginación, para combos) ----------
    @Query("""
        SELECT t FROM TaxEntity t
        WHERE t.isActive = TRUE
        ORDER BY t.rate DESC
    """)
    List<TaxEntity> findAllActive();

    // ---------- Impuestos por expiración próxima ----------
    @Query("""
        SELECT t FROM TaxEntity t
        WHERE t.validTo IS NOT NULL
          AND t.validTo BETWEEN :startDate AND :endDate
        ORDER BY t.validTo ASC
    """)
    Page<TaxEntity> findExpiringBetween(@Param("startDate") LocalDate startDate,
                                        @Param("endDate") LocalDate endDate,
                                        Pageable pageable);
}