package com.celotts.supplierservice.infrastructure.common.validation;

public final class ValidationGroups {

    // Constructor privado para evitar instanciación
    private ValidationGroups() {}

    // Grupo de validación usado al crear entidades (POST)
    public interface Create {}

    // Grupo de validación usado al actualizar entidades (PUT/PATCH)
    public interface Update {}
}