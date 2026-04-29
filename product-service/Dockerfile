# ---------------------------------------------------------------------
# ETAPA 1: CONSTRUCTOR (Compila la aplicación)
# ---------------------------------------------------------------------
FROM eclipse-temurin:17-jdk-jammy AS builder
WORKDIR /app

# Copia configuración de Gradle
COPY gradle/ gradle/
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .
RUN chmod +x ./gradlew

# Instalar utilidades
RUN apt-get update && apt-get install -y dos2unix && rm -rf /var/lib/apt/lists/*

# Ajuste de memoria para entornos limitados (512MB-1GB)
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Xmx512m"

COPY product-service/ /app/product-service/
RUN dos2unix /app/product-service/entrypoint.sh /app/product-service/wait-for-it.sh && \
    chmod +x /app/product-service/entrypoint.sh /app/product-service/wait-for-it.sh

# Compilación optimizada
RUN ./gradlew :product-service:bootJar -x test --no-daemon

# ---------------------------------------------------------------------
# ETAPA 2: EJECUCIÓN
# ---------------------------------------------------------------------
FROM eclipse-temurin:17-jre-jammy AS runner
EXPOSE 9090
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring
WORKDIR /app

COPY --from=builder /app/product-service/build/libs/product-service-*.jar /app/app.jar
COPY --from=builder /app/product-service/entrypoint.sh /app/entrypoint.sh
COPY --from=builder /app/product-service/wait-for-it.sh /app/wait-for-it.sh

ENTRYPOINT ["/app/entrypoint.sh"]