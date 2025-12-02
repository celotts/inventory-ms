# ---------------------------------------------------------------------
# ETAPA 1: CONSTRUCTOR (Compila la aplicación)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jdk-jammy AS builder
LABEL maintainer="Inventory MS Team"
WORKDIR /app

COPY gradle/ gradle/
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .

COPY supplier-service/build.gradle supplier-service/build.gradle
COPY supplier-service/src supplier-service/src

RUN chmod +x ./gradlew

# Resolver dependencias (con reintentos en caso de error de red)
RUN ./gradlew :supplier-service:dependencies \
    -x test \
    --console=plain \
    --no-daemon \
    --stacktrace \
    || (echo "Reintentando descarga de dependencias..." && \
        sleep 5 && \
        ./gradlew :supplier-service:dependencies \
        -x test \
        --console=plain \
        --no-daemon)

# Compilar y generar el JAR
RUN ./gradlew :supplier-service:bootJar \
    -x test \
    --console=plain \
    --no-daemon \
    --stacktrace

# Verificar que el JAR se generó correctamente
RUN ls -lh supplier-service/build/libs/ && \
    test -f supplier-service/build/libs/supplier-service-*.jar || \
    (echo "ERROR: JAR no fue generado" && exit 1)

# ---------------------------------------------------------------------
# ETAPA 2: EJECUCIÓN (Imagen final mínima)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS runner
LABEL maintainer="Inventory MS Team"

EXPOSE 9091

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

WORKDIR /app

# Copiar JAR compilado desde builder
COPY --from=builder /app/supplier-service/build/libs/supplier-service-*.jar /app/app.jar

# Copiar scripts de startup
COPY supplier-service/entrypoint.sh ./
COPY wait-for-it.sh ./

# Hacer ejecutables
RUN chmod +x entrypoint.sh wait-for-it.sh

# Punto de entrada
ENTRYPOINT ["/app/entrypoint.sh"]