# ---------------------------------------------------------------------
# ETAPA 1: CONSTRUCTOR (Compila la aplicación)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jdk-jammy AS builder
LABEL maintainer="Inventory MS Team"

ARG DOCKER_USERNAME
ARG DOCKER_PASSWORD

WORKDIR /app

# Copias iniciales de archivos de Gradle del proyecto raíz
COPY gradle/ gradle/
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .

# Copia específica del build.gradle y el código fuente del módulo
COPY api-gateway/build.gradle api-gateway/build.gradle
COPY api-gateway/src api-gateway/src

# Permisos de ejecución
RUN chmod +x ./gradlew

# =====================================================================
# CORRECCIÓN DE MEMORIA DE GRADLE: Limita la RAM para evitar OOM Kill
# =====================================================================
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Xmx512m -Xms256m"

# Paso 1: Resolver y cachear dependencias
# Se elimina --no-daemon
RUN ./gradlew :api-gateway:dependencies -x test --console=plain

# Paso 2: Ejecutar la compilación y generar el JAR
# Se elimina --no-daemon
RUN ./gradlew :api-gateway:bootJar -x test --console=plain

# Verificar que el JAR se generó correctamente
RUN ls -lh api-gateway/build/libs/ && \
    test -f api-gateway/build/libs/api-gateway-*.jar || \
    (echo "ERROR: JAR no fue generado" && exit 1)

# ---------------------------------------------------------------------
# ETAPA 2: EJECUCIÓN (Crea la imagen final mínima)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS runner
LABEL maintainer="Inventory MS Team"

# ⭐ PUERTO DE API GATEWAY (Diferente a Eureka)
EXPOSE 8090

# Instalar dos2unix como root para solucionar el problema de formato CRLF/LF.
RUN apt-get update && apt-get install -y dos2unix && rm -rf /var/lib/apt/lists/*

RUN addgroup --system spring && adduser --system --ingroup spring spring
# USER spring:spring (Movido abajo, después de las operaciones de root)

WORKDIR /app

# Copiar el JAR compilado desde la etapa 'builder'
COPY --from=builder /app/api-gateway/build/libs/api-gateway-*.jar /app/app.jar

# ⭐ COPIAR LOS SCRIPTS DE ARRANQUE
# Si 'wait-for-it.sh' está en 'infra', la línea correcta debe ser 'COPY infra/wait-for-it.sh ./'
COPY api-gateway/entrypoint.sh ./
COPY wait-for-it.sh ./

# =====================================================================
# ⭐ CORRECCIÓN DE PERMISOS (SOLUCIÓN FINAL CON DOS2UNIX) ⭐
# =====================================================================
# PASO CRÍTICO: Sanitizar el formato de línea (CRLF -> LF) con dos2unix.
RUN dos2unix ./entrypoint.sh ./wait-for-it.sh

# Hacerlos ejecutables
RUN chmod +x entrypoint.sh wait-for-it.sh

# Cambiar al usuario spring después de las operaciones de instalación y corrección
USER spring:spring


# ⭐ ESTABLECER EL PUNTO DE ENTRADA
ENTRYPOINT ["/app/entrypoint.sh"]