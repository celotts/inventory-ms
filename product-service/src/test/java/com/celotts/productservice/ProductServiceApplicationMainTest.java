package com.celotts.productservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.amqp.RabbitAutoConfiguration;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.boot.autoconfigure.liquibase.LiquibaseAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        classes = MinimalTestApplication.class,
        properties = {
                "spring.profiles.active=test",
                "spring.cloud.config.enabled=false",
                "eureka.client.enabled=false",
                "spring.main.web-application-type=none",
                "spring.main.lazy-initialization=true"
        },
        webEnvironment = SpringBootTest.WebEnvironment.NONE
)
class ProductServiceApplicationMainTest {

    @Test
    void contextLoads() {
        // Si llegamos aquí, el contexto mínimo de Spring levantó sin excepciones.
    }
}

/**
 * Contexto mínimo de prueba SIN escanear tus @Component/@Service propios
 * y con auto-configs pesadas excluidas.
 */
@SpringBootConfiguration
@EnableAutoConfiguration(exclude = {
        DataSourceAutoConfiguration.class,
        DataSourceTransactionManagerAutoConfiguration.class,
        HibernateJpaAutoConfiguration.class,
        LiquibaseAutoConfiguration.class,
        KafkaAutoConfiguration.class,
        RabbitAutoConfiguration.class,
        RedisAutoConfiguration.class,
        SecurityAutoConfiguration.class
})
class MinimalTestApplication {}