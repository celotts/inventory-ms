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

# Copia específica del build.gradle y el código fuente del módulo (!!! CAMBIO A PURCHASE-SERVICE !!!)
COPY purchase-service/build.gradle purchase-service/build.gradle
COPY purchase-service/src purchase-service/src

# Permisos de ejecución
RUN chmod +x ./gradlew

# =====================================================================
# CORRECCIÓN DE MEMORIA DE GRADLE: AUMENTO DEL HEAP A 1GB
# Esto corrige el error "Gradle build daemon disappeared unexpectedly" (OOM Kill).
# =====================================================================
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Xmx2048m -Xms1024m -XX:MaxMetaspaceSize=1024m -Dorg.gradle.internal.http.connectionTimeout=600000 -Dorg.gradle.internal.http.socketTimeout=600000"

# Paso 1: Resolver y cachear dependencias
# Se elimina --no-daemon
RUN ./gradlew :purchase-service:dependencies -x test --console=plain

# Paso 2: Ejecutar la compilación y generar el JAR
# Se elimina --no-daemon
RUN ./gradlew :purchase-service:bootJar -x test --console=plain

# Verificar que el JAR se generó correctamente
RUN ls -lh purchase-service/build/libs/ && \
    test -f purchase-service/build/libs/purchase-service-*.jar || \
    (echo "ERROR: JAR no fue generado" && exit 1)

# ---------------------------------------------------------------------
# ETAPA 2: EJECUCIÓN (Crea la imagen final mínima)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS runner
LABEL maintainer="Inventory MS Team"

# ⭐ PUERTO PARA PURCHASE SERVICE
EXPOSE 9094

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

WORKDIR /app

# Copiar el JAR compilado desde la etapa 'builder'
COPY --from=builder /app/purchase-service/build/libs/purchase-service-*.jar /app/app.jar

# ⭐ COPIAR LOS SCRIPTS DE ARRANQUE Y ESPERA (Asume espera a BD y Eureka en su entrypoint.sh)
COPY purchase-service/entrypoint.sh ./
COPY wait-for-it.sh ./

# =====================================================================
# ⭐ CORRECCIÓN DE PERMISOS (SANEAR FORMATO DE LÍNEA) ⭐
# =====================================================================
# PASO CRÍTICO: Sanitizar el formato de línea (CRLF -> LF).
# Esto elimina los caracteres '\r' (retorno de carro) que causan el error
# de "Operation not permitted" en el chmod.
RUN sed -i 's/\r$//' ./entrypoint.sh ./wait-for-it.sh

# Hacerlos ejecutables
RUN chmod +x entrypoint.sh wait-for-it.sh
# ⭐ ESTABLECER EL PUNTO DE ENTRADA
ENTRYPOINT ["/app/entrypoint.sh"]