package com.celotts.productservice.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

public record CategoryModel(
        UUID id,
        String name,
        String description,
        LocalDateTime createdAt
) {}