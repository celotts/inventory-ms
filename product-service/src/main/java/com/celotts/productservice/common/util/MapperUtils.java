package com.celotts.productservice.common.util;

import java.util.function.Consumer;

public class MapperUtils {
    private MapperUtils() {} // Evita instanciación

    public static <T> void updateFieldIfNotNull(T value, Consumer<T> setter) {
        if (value != null) {
            setter.accept(value);
        }
    }
}