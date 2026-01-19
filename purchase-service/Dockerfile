ARG DOCKER_USERNAME
ARG DOCKER_PASSWORD

# ---------------------------------------------------------------------
# ETAPA 1: CONSTRUCTOR
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /app

COPY gradle/ gradle/
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .

RUN chmod +x ./gradlew
RUN apt-get update && apt-get install -y dos2unix && rm -rf /var/lib/apt/lists/*

COPY purchase-service/ /app/purchase-service/

# Limpiamos formatos de archivos
RUN dos2unix /app/purchase-service/entrypoint.sh && \
    chmod +x /app/purchase-service/entrypoint.sh

# Construimos el JAR ejecutable
RUN ./gradlew :purchase-service:clean :purchase-service:bootJar --no-daemon

# ---------------------------------------------------------------------
# ETAPA 2: EJECUCIÓN
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS runner
WORKDIR /app
EXPOSE 9094

# Crear usuario de sistema para mayor seguridad
RUN addgroup --system spring && adduser --system --ingroup spring spring

# COPIA DIRECTA: Copiamos el JAR que Gradle generó con el nombre fijo
COPY --from=builder /app/purchase-service/build/libs/app.jar /app/app.jar
COPY --from=builder /app/purchase-service/entrypoint.sh /app/entrypoint.sh

# Ajustar permisos como root
USER root
RUN chmod +x /app/entrypoint.sh && \
    chown -R spring:spring /app

# Ejecutar como usuario limitado
USER spring:spring
ENTRYPOINT ["/app/entrypoint.sh"]