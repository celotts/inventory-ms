package com.celotts.supplierservice.infrastructure.adapter.input.rest.dto.supplier;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class SupplierResponseDto {
    private UUID id;
    private String name;
    private String tax_id;
    private String email;
    private String phone;
    private String address;
    private String active;
    private String createdBy;
    private String updatedBy;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDate createdAt;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime updatedAt;
}
