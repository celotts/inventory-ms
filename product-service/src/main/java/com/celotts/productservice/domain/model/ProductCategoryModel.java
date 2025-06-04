package com.celotts.productservice.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProductCategoryModel(
        UUID id,
        UUID productId,
        UUID categoryId,
        LocalDateTime createdAt
) {}