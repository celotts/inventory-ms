# ---------------------------------------------------------------------
# ETAPA 1: CONSTRUCTOR (Compila la aplicación) - OPTIMIZADO
# ---------------------------------------------------------------------
ARG DOCKER_USERNAME
ARG DOCKER_PASSWORD

FROM eclipse-temurin:21-jdk-jammy AS builder
LABEL maintainer="Inventory MS Team"
WORKDIR /app

# 1. Copiar archivos de Gradle para resolución de dependencias (capa de baja frecuencia de cambio)
COPY gradle/ gradle/
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .
COPY supplier-service/build.gradle supplier-service/build.gradle

# 2. Establecer permisos de ejecución
RUN chmod +x ./gradlew

# 3. Resolver dependencias (con reintentos en caso de error de red)
# Esta capa solo se invalida si cambian los archivos build.gradle o settings.gradle
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

# 4. Copiar el código fuente (capa de alta frecuencia de cambio)
# Esta es la única capa que se invalida al cambiar el código, pero la de dependencias ya está en caché.
COPY supplier-service/src supplier-service/src

# 5. Compilar y generar el JAR
RUN ./gradlew :supplier-service:bootJar \
    -x test \
    --console=plain \
    --no-daemon \
    --stacktrace

# 6. Verificar que el JAR se generó correctamente
RUN ls -lh supplier-service/build/libs/ && \
    test -f supplier-service/build/libs/supplier-service-*.jar || \
    (echo "ERROR: JAR no fue generado" && exit 1)

# ---------------------------------------------------------------------
# ETAPA 2: EJECUCIÓN (Imagen final mínima)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS runner
LABEL maintainer="Inventory MS Team"

EXPOSE 9091
WORKDIR /app

# 1. Instalar dependencias necesarias (curl/wget para healthchecks)
# Se hace como root por defecto
RUN apt-get update && apt-get install -y wget && rm -rf /var/lib/apt/lists/*

# 2. Copiar archivos (Asegúrate de que wait-for-it.sh esté en el contexto de construcción)
COPY --from=builder /app/supplier-service/build/libs/supplier-service-*.jar /app/app.jar
COPY supplier-service/entrypoint.sh ./
COPY wait-for-it.sh ./

# 3. Dar permisos como root
RUN chmod +x entrypoint.sh wait-for-it.sh

# 4. Crear usuario y cambiar dueño de la carpeta /app
RUN addgroup --system spring && adduser --system --ingroup spring spring && \
    chown -R spring:spring /app

# 5. Cambiar al usuario seguro
USER spring:spring

# 6. Punto de entrada pasando "app.jar" como el argumento $1
ENTRYPOINT ["/app/entrypoint.sh"]
CMD ["app.jar"]