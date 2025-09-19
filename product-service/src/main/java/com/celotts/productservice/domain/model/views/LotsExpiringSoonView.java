package com.celotts.productservice.domain.model.views;

public record LotsExpiringSoonView(String code,  String name, UUID lotId, String lotCode, LocalDate expirationDate, BigDecimal quantity) { }
