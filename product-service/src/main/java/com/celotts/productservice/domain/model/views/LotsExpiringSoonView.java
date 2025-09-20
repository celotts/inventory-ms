package com.celotts.productservice.domain.model.views;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public record LotsExpiringSoonView(String code,  String name, UUID lotId, String lotCode, LocalDate expirationDate, BigDecimal quantity) { }
