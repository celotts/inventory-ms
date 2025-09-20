package com.celotts.productservice.infrastructure.adapter.output.postgres.read.repository;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.lot.LotEntity;
import com.celotts.productservice.domain.model.views.ProductStockAvailableView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

public interface StockReadRepository extends JpaRepository<LotEntity, java.util.UUID> {

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
            SELECT COUNT(1) FROM product p
        """,
            nativeQuery = true
    )
    Page<ProductStockAvailableView> listProductStockAvailable(Pageable pageable, @Param("q") String q);
}