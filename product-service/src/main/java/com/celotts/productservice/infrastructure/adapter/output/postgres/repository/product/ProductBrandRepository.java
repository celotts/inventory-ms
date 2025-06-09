
package com.celotts.productservice.infrastructure.adapter.output.postgres.repository.product;

import com.celotts.productservice.infrastructure.adapter.output.postgres.entity.product.ProductBrand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductBrandRepository extends JpaRepository<ProductBrand, UUID> {

    boolean existsById(UUID id);

    Optional<ProductBrand> findById(UUID id);

    @Query("SELECT pb.name FROM ProductBrand pb WHERE pb.id = :id")
    Optional<String> findNameById(@Param("id") UUID id);
}