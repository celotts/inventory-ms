package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productserviceOld.infrastructure.adapter.output.postgres.entity.product.ProductBrandEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductBrandRepository extends JpaRepository<ProductBrandEntity, UUID> {

    boolean existsByName(String name);
    Optional<ProductBrandEntity> findByName(String name);

    @Query("SELECT pb.name FROM ProductBrandEntity pb WHERE pb.id = :id")
    Optional<String> findNameById(@Param("id") UUID id);

    @Query("SELECT pb.id FROM ProductBrandEntity pb")
    List<UUID> findAllIds();

    @Modifying
    @Query("UPDATE ProductBrandEntity p SET p.enabled = false WHERE p.id = :id")
    void disableBrandById(@Param("id") UUID id);

    @Modifying
    @Query("UPDATE ProductBrandEntity p SET p.enabled = true WHERE p.id = :id")
    void enableBrandById(@Param("id") UUID id);

    @Modifying
    @Query("DELETE FROM ProductBrandEntity p WHERE p.id = :id")
    void hardDeleteById(@Param("id") UUID id);
}