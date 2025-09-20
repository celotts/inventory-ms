package com.celotts.productservice.infrastructure.adapter.output.postgres.read.repository;

import com.celotts.productservice.domain.model.views.LotAvailableView;
import com.celotts.productservice.domain.model.views.LotExpiredView;
import com.celotts.productservice.domain.model.views.LotsExpiringSoonView;
import com.celotts.productservice.domain.model.views.ProductStockAvailableView;
import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.lot.LotEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PerishableReadRepository extends JpaRepository<LotEntity, UUID> {

    // Stock disponible por producto (vista v_product_stock_available)
    @Query(
            value = """
            SELECT p.id AS productId,
                   p.code AS code,
                   p.name AS name,
                   v.available_stock AS availableStock
            FROM v_product_stock_available v
            JOIN product p ON p.id = v.product_id
            WHERE (:q IS NULL OR :q = '' OR p.code ILIKE CONCAT('%', :q, '%') OR p.name ILIKE CONCAT('%', :q, '%'))
            """,
            countQuery = """
            SELECT COUNT(1)
            FROM product p
            """,
            nativeQuery = true
    )
    Page<ProductStockAvailableView> listProductStockAvailable(Pageable pageable, @Param("q") String q);

    // Lotes que vencen pronto (vista v_lots_expiring_soon)
    @Query(
            value = """
            SELECT l.lot_id          AS lotId,
                   l.lot_code        AS lotCode,
                   l.expiration_date AS expirationDate,
                   l.quantity        AS quantity,
                   l.code            AS code,
                   l.name            AS name,
                   p.id              AS productId
            FROM v_lots_expiring_soon l
            JOIN product p ON p.code = l.code AND p.name = l.name
            """,
            countQuery = "SELECT COUNT(1) FROM v_lots_expiring_soon",
            nativeQuery = true
    )
    Page<LotsExpiringSoonView> listLotsExpiringSoon(Pageable pageable);

    // Lotes disponibles por producto (vista v_lot_available)
    @Query(
            value = """
            SELECT l.id              AS id,
                   l.product_id      AS productId,
                   l.lot_code        AS lotCode,
                   l.quantity        AS quantity,
                   l.expiration_date AS expirationDate,
                   l.stage           AS stage,
                   l.received_at     AS receivedAt
            FROM v_lot_available l
            WHERE l.product_id = :productId
            """,
            countQuery = """
            SELECT COUNT(1)
            FROM v_lot_available l
            WHERE l.product_id = :productId
            """,
            nativeQuery = true
    )
    Page<LotAvailableView> listLotsAvailable(@Param("productId") UUID productId, Pageable pageable);

    // Lotes vencidos por producto (vista v_lot_expired)
    @Query(
            value = """
            SELECT l.id              AS id,
                   l.product_id      AS productId,
                   l.lot_code        AS lotCode,
                   l.quantity        AS quantity,
                   l.expiration_date AS expirationDate,
                   l.stage           AS stage
            FROM v_lot_expired l
            WHERE l.product_id = :productId
            """,
            countQuery = """
            SELECT COUNT(1)
            FROM v_lot_expired l
            WHERE l.product_id = :productId
            """,
            nativeQuery = true
    )
    Page<LotExpiredView> listLotsExpired(@Param("productId") UUID productId, Pageable pageable);
}