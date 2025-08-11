package com.celotts.productservice.domain.model;

import org.junit.jupiter.api.Test;
import java.util.UUID;
import static org.assertj.core.api.Assertions.assertThat;

class ProductReferenceTest {
    private static class Impl implements ProductReference {
        Impl(String u, UUID b, UUID c){ this.u=u; this.b=b; this.c=c; }
        private final String u; private final UUID b; private final UUID c;
        public String getUnitCode(){ return u; }
        public UUID getBrandId(){ return b; }
        public UUID getCategoryId(){ return c; }
    }
    @Test
    void getters_shouldReturnValues(){
        UUID b = UUID.randomUUID(), c = UUID.randomUUID();
        ProductReference ref = new Impl("KG", b, c);
        assertThat(ref.getUnitCode()).isEqualTo("KG");
        assertThat(ref.getBrandId()).isEqualTo(b);
        assertThat(ref.getCategoryId()).isEqualTo(c);
    }
}