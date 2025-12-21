# ---------------------------------------------------------------------
# ETAPA 1: CONSTRUCTOR (Compila la aplicación)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jdk-jammy AS builder
LABEL maintainer="Inventory MS Team"

ARG DOCKER_USERNAME
ARG DOCKER_PASSWORD

# ⭐ PASO CRÍTICO 1: Definir los argumentos de construcción (ARG)
# Estos recibirán los valores pasados desde docker-compose.yml o la línea de comandos 'docker build --build-arg ...'
ARG http_proxy
ARG https_proxy
ARG no_proxy

WORKDIR /app

# Instalar dos2unix como root para solucionar el problema de formato CRLF/LF.
RUN apt-get update && apt-get install -y dos2unix && rm -rf /var/lib/apt/lists/*

# Copias iniciales de archivos de Gradle
COPY gradle/ gradle/
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .
COPY tax-service/build.gradle tax-service/build.gradle
COPY tax-service/src tax-service/src

# Copiamos los scripts aquí porque la ETAPA 2 los va a copiar desde la ETAPA 1 usando COPY --from=builder
COPY tax-service/entrypoint.sh tax-service/entrypoint.sh
COPY tax-service/wait-for-it.sh tax-service/wait-for-it.sh

# =====================================================================
# ⭐ CORRECCIÓN DE PERMISOS (SOLUCIÓN FINAL CON DOS2UNIX) ⭐
# =====================================================================
# PASO CRÍTICO: Sanitizar el formato de línea (CRLF -> LF) con dos2unix.
# Esto reemplaza el comando 'sed -i' problemático.
RUN dos2unix tax-service/entrypoint.sh tax-service/wait-for-it.sh

# Permisos de ejecución
RUN chmod +x ./gradlew
RUN chmod +x tax-service/entrypoint.sh tax-service/wait-for-it.sh

# ⭐ PASO CRÍTICO 2: Establecer las variables de entorno (ENV)
# Esto hace que el proxy esté disponible para todos los comandos subsiguientes (apt-get, curl, gradlew)
ENV HTTP_PROXY=$http_proxy
ENV HTTPS_PROXY=$https_proxy
ENV NO_PROXY=$no_proxy
ENV http_proxy=$http_proxy
ENV https_proxy=$https_proxy
ENV no_proxy=$no_proxy

# =====================================================================
# ✅ CORRECCIÓN DE MEMORIA: APLICACIÓN FORZADA EN RUN
# Esto corrige el error "Gradle build daemon disappeared unexpectedly" (OOM Kill).
# =====================================================================
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Xmx2048m -Xms1024m -XX:MaxMetaspaceSize=1024m -Dorg.gradle.internal.http.connectionTimeout=600000 -Dorg.gradle.internal.http.socketTimeout=600000"

# ⭐ PASOS DE DIAGNÓSTICO DE RED (Ahora usarán el proxy si está configurado) ⭐
# 1. Instalar curl para verificar la conectividad de red
RUN apt-get update && apt-get install -y curl
# Si el proxy está configurado correctamente, este curl ya no debería fallar.
RUN curl -s -I http://google.com | grep "HTTP"

# Paso 1: Resolver y cachear dependencias
# ⭐ CRÍTICO: Forzar GRADLE_OPTS y reintroducir --no-daemon
RUN GRADLE_OPTS="-Xmx2048m -Xms1024m" ./gradlew :tax-service:dependencies -x test --console=plain --no-daemon

# Paso 2: Ejecutar la compilación y generar el JAR
# ⭐ CRÍTICO: Forzar GRADLE_OPTS y reintroducir --no-daemon
RUN GRADLE_OPTS="-Xmx2048m -Xms1024m" ./gradlew :tax-service:bootJar -x test --console=plain --no-daemon

# Verificar que el JAR se generó correctamente
RUN ls -lh tax-service/build/libs/ && \
    test -f tax-service/build/libs/tax-service-*.jar || \
    (echo "ERROR: JAR no fue generado" && exit 1)

# ---------------------------------------------------------------------
# ETAPA 2: EJECUCIÓN
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS runner
LABEL maintainer="Inventory MS Team"

# PUERTO TAX SERVICE (Comentario movido a su propia línea)
EXPOSE 9092

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

WORKDIR /app

# Copiar el JAR compilado desde la etapa 'builder'
COPY --from=builder /app/tax-service/build/libs/tax-service-*.jar /app/app.jar

# ⭐ COPIAR LOS SCRIPTS DE ARRANQUE Y ESPERA (CORRECCIÓN DEFINITIVA)
# Usando COPY --from para preservar permisos y usuario.
# Esto reemplaza el obsoleto 'COPY script ./' y elimina la necesidad del 'RUN chmod +x'.
COPY --from=builder --chown=spring:spring --chmod=0755 /app/tax-service/entrypoint.sh /app/entrypoint.sh
COPY --from=builder --chown=spring:spring --chmod=0755 /app/tax-service/wait-for-it.sh /app/wait-for-it.sh

# ⭐ ESTABLECER EL PUNTO DE ENTRADA
ENTRYPOINT ["/app/entrypoint.sh"]