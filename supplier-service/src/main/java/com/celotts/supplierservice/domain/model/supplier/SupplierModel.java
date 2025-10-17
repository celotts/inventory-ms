package com.celotts.supplierservice.domain.model.supplier;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class SupplierModel {
    private UUID id;
    private String code;
    private String name;
    private String taxId;
    private String email;
    private String phone;
    private String address;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private String createdBy;
    private LocalDateTime updatedAt;
    private String updatedBy;

    public void normalize() {
        if (name != null) name = name.trim();
        if (email != null) email = email.trim().toLowerCase();
        if (code != null) code = code.trim().toUpperCase();
        if (taxId != null) taxId = taxId.trim().toUpperCase();
        if (phone != null) phone = phone.trim();
        if (address != null) address = address.trim();
    }
}
