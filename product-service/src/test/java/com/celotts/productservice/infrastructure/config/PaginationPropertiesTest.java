package com.celotts.productservice.infrastructure.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;

class PaginationPropertiesTest {

    @Test
    void testGettersAndSetters() {
        PaginationProperties props = new PaginationProperties();

        props.setDefaultPage(1);
        props.setDefaultSize(20);
        props.setMaxSize(100);
        props.setDefaultSort("name");
        props.setDefaultDirection("asc");

        assertThat(props.getDefaultPage()).isEqualTo(1);
        assertThat(props.getDefaultSize()).isEqualTo(20);
        assertThat(props.getMaxSize()).isEqualTo(100);
        assertThat(props.getDefaultSort()).isEqualTo("name");
        assertThat(props.getDefaultDirection()).isEqualTo("asc");
    }

    @Test
    void testBindingFromProperties() {
        new ApplicationContextRunner()
                .withConfiguration(org.springframework.boot.autoconfigure.AutoConfigurations.of(
                        org.springframework.boot.autoconfigure.context.ConfigurationPropertiesAutoConfiguration.class
                ))
                .withBean(PaginationProperties.class)
                .withPropertyValues(
                        "app.pagination.default-page=2",
                        "app.pagination.default-size=50",
                        "app.pagination.max-size=200",
                        "app.pagination.default-sort=createdDate",
                        "app.pagination.default-direction=desc"
                )
                .run(context -> {
                    assertThat(context).hasSingleBean(PaginationProperties.class);
                    PaginationProperties props = context.getBean(PaginationProperties.class);

                    assertThat(props.getDefaultPage()).isEqualTo(2);
                    assertThat(props.getDefaultSize()).isEqualTo(50);
                    assertThat(props.getMaxSize()).isEqualTo(200);
                    assertThat(props.getDefaultSort()).isEqualTo("createdDate");
                    assertThat(props.getDefaultDirection()).isEqualTo("desc");
                });
    }
}