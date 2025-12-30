# ---------------------------------------------------------------------
# ETAPA 1: CONSTRUCTOR (Compila la aplicación)
# ---------------------------------------------------------------------
ARG DOCKER_USERNAME
ARG DOCKER_PASSWORD

FROM eclipse-temurin:21-jdk-jammy AS builder
LABEL maintainer="Inventory MS Team"

# ⭐ PASO CRÍTICO 1: Definir los argumentos de construcción (ARG) para el Proxy
ARG http_proxy
ARG https_proxy
ARG no_proxy

WORKDIR /app

# Instalar dos2unix como root para solucionar el problema de formato CRLF/LF.
RUN apt-get update && apt-get install -y dos2unix && rm -rf /var/lib/apt/lists/*

# Copias iniciales de archivos de Gradle del proyecto raíz
COPY gradle/ gradle/
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .
# Copiar el archivo gradle.properties para la configuración explícita del proxy de Gradle
COPY gradle.properties .

# Copia específica del build.gradle y el código fuente del módulo
COPY api-gateway/build.gradle api-gateway/build.gradle
COPY api-gateway/src api-gateway/src

# Copiamos los scripts aquí porque la ETAPA 2 los va a copiar desde la ETAPA 1 usando COPY --from=builder
COPY api-gateway/entrypoint.sh api-gateway/entrypoint.sh
COPY api-gateway/wait-for-it.sh api-gateway/wait-for-it.sh

# =====================================================================
# ⭐ CORRECCIÓN DE PERMISOS (SOLUCIÓN FINAL CON DOS2UNIX) ⭐
# =====================================================================
# PASO CRÍTICO: Sanitizar el formato de línea (CRLF -> LF) con dos2unix.
RUN dos2unix api-gateway/entrypoint.sh api-gateway/wait-for-it.sh

# Permisos de ejecución
RUN chmod +x ./gradlew
RUN chmod +x api-gateway/entrypoint.sh api-gateway/wait-for-it.sh

# ⭐ PASO CRÍTICO 2: Establecer las variables de entorno (ENV) para el Proxy
# Esto hace que el proxy esté disponible para apt-get, curl y gradlew
ENV HTTP_PROXY=$http_proxy
ENV HTTPS_PROXY=$https_proxy
ENV NO_PROXY=$no_proxy
ENV http_proxy=$http_proxy
ENV https_proxy=$https_proxy
ENV no_proxy=$no_proxy


# =====================================================================
# CORRECCIÓN DE MEMORIA DE GRADLE: AUMENTO DEL HEAP A 3GB
# =====================================================================
ENV GRADLE_OPTS="-Xmx3072m -Xms1536m -XX:MaxMetaspaceSize=1536m"

# ⭐ PASOS DE DIAGNÓSTICO DE RED (Importante para verificar el proxy) ⭐
# 1. Instalar curl para verificar la conectividad de red
RUN apt-get update && apt-get install -y curl
# Si el proxy está configurado correctamente, este curl ya no debería fallar.
RUN curl -s -I http://google.com | grep "HTTP"

# Paso 1: Resolver y cachear dependencias (Ahora usará el proxy)
# ⭐ CRÍTICO: Forzar GRADLE_OPTS para aplicar 3GB de heap.
RUN GRADLE_OPTS="-Xmx3072m -Xms1536m" ./gradlew :api-gateway:dependencies -x test --console=plain --no-daemon

# Paso 2: Ejecutar la compilación y generar el JAR
# ⭐ CRÍTICO: Forzar GRADLE_OPTS para aplicar 3GB de heap.
RUN GRADLE_OPTS="-Xmx3072m -Xms1536m" ./gradlew :api-gateway:bootJar -x test --console=plain --no-daemon

# Verificar que el JAR se generó correctamente
RUN ls -lh api-gateway/build/libs/ && \
    test -f api-gateway/build/libs/api-gateway-*.jar || \
    (echo "ERROR: JAR no fue generado" && exit 1)

# ---------------------------------------------------------------------
# ETAPA 2: EJECUCIÓN (Crea la imagen final mínima)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS runner
LABEL maintainer="Inventory MS Team"

# ⭐ PUERTO DE API GATEWAY
EXPOSE 8090

RUN addgroup --system spring && adduser --system --ingroup spring spring
# NOTA: USER se aplica después de la copia de scripts para asegurar permisos

WORKDIR /app

# Copiar el JAR compilado desde la etapa 'builder'
COPY --from=builder /app/api-gateway/build/libs/api-gateway-*.jar /app/app.jar

# ⭐ COPIAR LOS SCRIPTS DE ARRANQUE Y ESPERA (CORRECCIÓN DEFINITIVA)
# Usando COPY --from con CHOWN y CHMOD para establecer permisos y propiedad correcta.
COPY --from=builder --chown=spring:spring --chmod=0755 /app/api-gateway/entrypoint.sh /app/entrypoint.sh
COPY --from=builder --chown=spring:spring --chmod=0755 /app/api-gateway/wait-for-it.sh /app/wait-for-it.sh

# Cambiar al usuario spring después de las operaciones de instalación y corrección
USER spring:spring

# ⭐ ESTABLECER EL PUNTO DE ENTRADA
ENTRYPOINT ["/app/entrypoint.sh"]