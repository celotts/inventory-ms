package com.celotts.productservice.infrastructure.adapter.output.postgres.entity;

import com.celotts.productservice.domain.model.ProductModel;
import com.celotts.productservice.infrastructure.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product extends BaseEntity {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    private String description;

    @ManyToOne
    @JoinColumn(name = "product_type", referencedColumnName = "code")
    private ProductType productType;

    @ManyToOne
    @JoinColumn(name = "unit_code", referencedColumnName = "code")
    private ProductUnit productUnit;

    @ManyToOne
    @JoinColumn(name = "brand_id")
    private ProductBrand productBrand;

    @Column(name = "minimum_stock")
    private Integer minimumStock; // ✅ entero

    @Column(name = "current_stock")
    private Integer currentStock; // ✅ entero

    @Column(name = "unit_price", precision = 10, scale = 2)
    private BigDecimal unitPrince;

    private Boolean enabled = true;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    private String createdBy;
    private String updatedBy;

    // ✅ Conversión desde modelo de dominio
    public static Product fromModel(ProductModel model) {
        Product product = new Product();
        product.setId(model.getId());
        product.setCode(model.getCode());
        product.setDescription(model.getDescription());

        ProductType type = new ProductType();
        type.setCode(model.getProductTypeCode());
        product.setProductType(type);

        ProductUnit unit = new ProductUnit();
        unit.setCode(model.getUnitCode());
        product.setProductUnit(unit);

        ProductBrand brand = new ProductBrand();
        brand.setId(model.getBrandId());
        product.setProductBrand(brand);

        product.setMinimumStock(model.getMinimumStock());
        product.setCurrentStock(model.getCurrentStock());
        product.setUnitPrince(model.getUnitPrice());

        product.setEnabled(model.getEnabled());
        product.setCreatedAt(model.getCreatedAt());
        product.setUpdatedAt(model.getUpdatedAt());
        product.setCreatedBy(model.getCreatedBy());
        product.setUpdatedBy(model.getUpdatedBy());

        return product;
    }

    // ✅ Conversión al modelo de dominio
    public ProductModel toModel() {
        return ProductModel.builder()
                .id(this.getId())
                .code(this.getCode())
                .description(this.getDescription())
                .productTypeCode(this.productType != null ? this.productType.getCode() : null)
                .unitCode(this.productUnit != null ? this.productUnit.getCode() : null)
                .brandId(this.productBrand != null ? this.productBrand.getId() : null)
                .minimumStock(this.getMinimumStock())
                .currentStock(this.getCurrentStock())
                .unitPrice(this.getUnitPrince())
                .enabled(this.getEnabled())
                .createdAt(this.getCreatedAt())
                .updatedAt(this.getUpdatedAt())
                .createdBy(this.getCreatedBy())
                .updatedBy(this.getUpdatedBy())
                .build();
    }
}