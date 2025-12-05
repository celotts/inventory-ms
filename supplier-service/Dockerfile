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
RUN chmod +x ./gradlew

COPY supplier-service/build.gradle supplier-service/build.gradle
COPY supplier-service/src supplier-service/src

# =====================================================================
# CORRECCIÓN DE MEMORIA DE GRADLE: Limita la RAM para evitar OOM Kill
# =====================================================================
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Xmx512m -Xms256m"

# Resolver dependencias (con reintentos en caso de error de red)
# El flag '--no-daemon' se elimina porque se definió en GRADLE_OPTS
RUN ./gradlew :supplier-service:dependencies \
    -x test \
    --console=plain \
    || (echo "Reintentando descarga de dependencias..." && \
        sleep 5 && \
        ./gradlew :supplier-service:dependencies \
        -x test \
        --console=plain)

# Compilar y generar el JAR
# El flag '--no-daemon' se elimina
RUN ./gradlew :supplier-service:bootJar \
    -x test \
    --console=plain

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

# Instalar dos2unix como root para solucionar el problema de formato CRLF/LF.
RUN apt-get update && apt-get install -y dos2unix && rm -rf /var/lib/apt/lists/*

RUN addgroup --system spring && adduser --system --ingroup spring spring
# USER spring:spring (Se mueve abajo, después de las operaciones de root)

WORKDIR /app

# Copiar JAR compilado desde builder
COPY --from=builder /app/supplier-service/build/libs/supplier-service-*.jar /app/app.jar

# Copiar scripts de startup
COPY supplier-service/entrypoint.sh ./
COPY wait-for-it.sh ./

# =====================================================================
# ⭐ CORRECCIÓN DE PERMISOS (SOLUCIÓN FINAL CON DOS2UNIX) ⭐
# =====================================================================
# PASO CRÍTICO 1: Sanitizar el formato de línea (CRLF -> LF) con dos2unix.
RUN dos2unix ./entrypoint.sh ./wait-for-it.sh

# PASO CRÍTICO 2: Hacerlos ejecutables
RUN chmod +x entrypoint.sh wait-for-it.sh

# Cambiar al usuario spring después de las operaciones de instalación y corrección
USER spring:spring

# Aquí debería ir el ENTRYPOINT
ENTRYPOINT ["/app/entrypoint.sh"]