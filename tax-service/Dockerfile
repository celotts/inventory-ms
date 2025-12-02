# ---------------------------------------------------------------------
# ETAPA 1: CONSTRUCTOR (Compila la aplicación)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jdk-jammy AS builder
LABEL maintainer="Inventory MS Team"

# ⭐ PASO CRÍTICO 1: Definir los argumentos de construcción (ARG)
# Estos recibirán los valores pasados desde docker-compose.yml o la línea de comandos 'docker build --build-arg ...'
ARG http_proxy
ARG https_proxy
ARG no_proxy

WORKDIR /app

# Copias iniciales de archivos de Gradle
COPY gradle/ gradle/
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .
COPY tax-service/build.gradle tax-service/build.gradle
COPY tax-service/src tax-service/src

# Permisos de ejecución
RUN chmod +x ./gradlew

# ⭐ PASO CRÍTICO 2: Establecer las variables de entorno (ENV)
# Esto hace que el proxy esté disponible para todos los comandos subsiguientes (apt-get, curl, gradlew)
ENV HTTP_PROXY=$http_proxy
ENV HTTPS_PROXY=$https_proxy
ENV NO_PROXY=$no_proxy
ENV http_proxy=$http_proxy
ENV https_proxy=$https_proxy
ENV no_proxy=$no_proxy

# ⭐ PASOS DE DIAGNÓSTICO DE RED (Ahora usarán el proxy si está configurado) ⭐
# 1. Instalar curl para verificar la conectividad de red
RUN apt-get update && apt-get install -y curl
# Si el proxy está configurado correctamente, este curl ya no debería fallar.
RUN curl -s -I http://google.com | grep "HTTP"

# Paso 1: Resolver y cachear dependencias (Ahora usará las variables ENV)
RUN ./gradlew :tax-service:dependencies -x test --console=plain --no-daemon

# Paso 2: Ejecutar la compilación y generar el JAR
RUN ./gradlew :tax-service:bootJar -x test --console=plain --no-daemon

# ---------------------------------------------------------------------
# ETAPA 2: EJECUCIÓN (No necesita cambios si ya funciona la Etapa 1)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS runner
LABEL maintainer="Inventory MS Team"

EXPOSE 9092 # PUERTO TAX SERVICE

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

WORKDIR /app

# Copiar el JAR compilado desde la etapa 'builder'
COPY --from=builder /app/tax-service/build/libs/tax-service-*.jar /app/app.jar

# ⭐ COPIAR LOS SCRIPTS DE ARRANQUE Y ESPERA (Asume espera a Eureka en su entrypoint.sh)
COPY tax-service/entrypoint.sh ./
COPY wait-for-it.sh ./

# Hacerlos ejecutables
RUN chmod +x entrypoint.sh wait-for-it.sh

# ⭐ ESTABLECER EL PUNTO DE ENTRADA
ENTRYPOINT ["/app/entrypoint.sh"]