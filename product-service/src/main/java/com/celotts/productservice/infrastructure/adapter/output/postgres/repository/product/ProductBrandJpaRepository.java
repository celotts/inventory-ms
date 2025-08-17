package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductBrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProductBrandJpaRepository extends JpaRepository<ProductBrandEntity, UUID> {

    boolean existsByNameIgnoreCase(String name);
    Optional<ProductBrandEntity> findByNameIgnoreCase(String name);

    @Query("select b.name from ProductBrandEntity b where b.id = :id")
    Optional<String> findNameById(@Param("id") UUID id);

    @Query("select b.id from ProductBrandEntity b")
    List<UUID> findAllIds();

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update ProductBrandEntity b set b.enabled = true where b.id = :id")
    void enableBrandById(@Param("id") UUID id);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("update ProductBrandEntity b set b.enabled = false where b.id = :id")
    void disableBrandById(@Param("id") UUID id);
}