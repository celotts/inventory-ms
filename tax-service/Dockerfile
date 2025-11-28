# ---------------------------------------------------------------------
# ETAPA 1: CONSTRUCTOR (Compila la aplicación)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jdk-jammy AS builder
LABEL maintainer="Inventory MS Team"

WORKDIR /app

# Copias iniciales de archivos de Gradle del proyecto raíz
COPY gradle/ gradle/
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .

# Copia específica del build.gradle y el código fuente del módulo (!!! CAMBIO A TAX-SERVICE !!!)
COPY tax-service/build.gradle tax-service/build.gradle
COPY tax-service/src tax-service/src

# Permisos de ejecución
RUN chmod +x ./gradlew

# ⭐ PASOS DE DIAGNÓSTICO DE RED ⭐
# 1. Instalar ping para verificar la conectividad de red
RUN apt-get update && apt-get install -y iputils-ping
# 2. Hacer una prueba de conectividad a una URL externa
RUN ping -c 3 google.com

# Paso 1: Resolver y cachear dependencias
# Si el ping anterior FALLA, el problema es de red/proxy en tu entorno.
# Si estás detrás de un proxy, es posible que necesites inyectar las propiedades directamente en este comando:
# Ejemplo: RUN ./gradlew -Dhttp.proxyHost=proxy.corp.com -Dhttp.proxyPort=8080 :tax-service:dependencies ...
RUN ./gradlew :tax-service:dependencies -x test --console=plain --no-daemon

# Paso 2: Ejecutar la compilación y generar el JAR
RUN ./gradlew :tax-service:bootJar -x test --console=plain --no-daemon

# ---------------------------------------------------------------------
# ETAPA 2: EJECUCIÓN (Crea la imagen final mínima)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS runner
LABEL maintainer="Inventory MS Team"

EXPOSE 9092 # ⭐ PUERTO TENTATIVO PARA TAX SERVICE

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