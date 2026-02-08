# Getting Started

### Reference Documentation

For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.5/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.5/gradle-plugin/packaging-oci-image.html)
* [Config Client](https://docs.spring.io/spring-cloud-config/reference/client.html)
* [Eureka Discovery Client](https://docs.spring.io/spring-cloud-netflix/reference/spring-cloud-netflix.html#_service_discovery_eureka_clients)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.4.5/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.4.5/reference/using/devtools.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.5/reference/web/servlet.html)

### Guides

The following guides illustrate how to use some features concretely:

* [Service Registration and Discovery with Eureka and Spring Cloud](https://spring.io/guides/gs/service-registration-and-discovery/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)
* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Additional Links

These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

### Test Execution and Coverage Report

You can execute the tests and generate the Jacoco coverage report using the following Gradle commands:

```bash
# Run all tests and generate the Jacoco report
./gradlew clean test jacocoTestReport

# Verify coverage threshold (adjust the limit in build.gradle if needed)
./gradlew jacocoTestCoverageVerification
```

The generated report will be available at:

```
build/reports/jacoco/test/html/index.html
```

To open the report:

* On macOS/Linux:

```bash
open build/reports/jacoco/test/html/index.html
```

* On Windows:

```bash
start build/reports/jacoco/test/html/index.html
```

If the file does not exist, make sure the tests ran successfully and the `jacocoTestReport` task completed without errors.

## Puertos recomendados para microservicios

| Microservicio          | Puerto en Docker (`docker-compose`) | Puerto local (IntelliJ o CLI) |
| ---------------------- |-------------------------------------|-------------------------------|
| `api-gateway`          | `8081:8081`                         | `8081`                        |
| `discovery-service`    | `8761:8761`                         | `8761`                        |
| `config-service`       | `7777:7777`                         | `7777`                        |
| `product-service`      | `9090:9090`                         | `9090`                        |
| `inventory-service`    | `9091:9091`                         | `9091`                        |
| `order-service`        | `9092:9092`                         | `9092`                        |
| `supplier-service`     | `9093:9093`                         | `9093`                        |
| `notification-service` | `9094:9094`                         | `9094`                        |

# Modelo Relacional de Datos

```plantuml
@startuml
!define table(x) class x << (T,#FFAAAA) >>
!define primaryKey(x) <u>x</u>
!define foreignKey(x) <i>x</i>

title Diagrama Relacional - product-init.sql

' --- Tablas base ---
table(product_type) {
primaryKey(code): VARCHAR(50)
name: VARCHAR(100)
description: TEXT
}

table(product_unit) {
primaryKey(code): VARCHAR(30)
name: VARCHAR(100)
symbol: VARCHAR(10)
}

table(product_brand) {
primaryKey(id): UUID
name: VARCHAR(100)
description: TEXT
}

table(product_tag) {
primaryKey(id): UUID
name: VARCHAR(50)
description: TEXT
}

table(category) {
primaryKey(id): UUID
name: VARCHAR(100)
description: TEXT
created_at: TIMESTAMP
}

' --- Tabla principal ---
table(product) {
primaryKey(id): UUID
code: VARCHAR(50)
name: VARCHAR(100)
description: TEXT
foreignKey(product_type): VARCHAR(50)
foreignKey(unit_code): VARCHAR(30)
foreignKey(brand_id): UUID
minimum_stock: DECIMAL(10,2)
current_stock: DECIMAL(10,2)
unit_price: DECIMAL(10,2)
enabled: BOOLEAN
created_at: TIMESTAMP
created_by: VARCHAR(100)
updated_by: VARCHAR(100)
updated_at: TIMESTAMP
}

' --- Relaciones ---
table(product_category) {
primaryKey(id): UUID
foreignKey(product_id): UUID
foreignKey(category_id): UUID
assigned_at: TIMESTAMP
}

table(product_price_history) {
primaryKey(id): UUID
foreignKey(product_id): UUID
price: DECIMAL(10,2)
changed_at: TIMESTAMP
}

table(product_image) {
primaryKey(id): UUID
foreignKey(product_id): UUID
url: TEXT
uploaded_at: TIMESTAMP
}

table(product_tag_assignment) {
primaryKey(id): UUID
foreignKey(product_id): UUID
foreignKey(tag_id): UUID
assigned_at: TIMESTAMP
}

' --- Relaciones entre tablas ---
product::product_type --> product_type::code
product::unit_code --> product_unit::code
product::brand_id --> product_brand::id
product_category::product_id --> product::id
product_category::category_id --> category::id
product_price_history::product_id --> product::id
product_image::product_id --> product::id
product_tag_assignment::product_id --> product::id
product_tag_assignment::tag_id --> product_tag::id

@enduml
```

# Modelo Relacional de Datos

![diagrama\_relacional\_product-service.png](diagrama_relacional_product-service.png)

![create-product-secuence.puml](create-product-secuence.png)
