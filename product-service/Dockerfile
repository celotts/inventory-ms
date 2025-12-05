# ---------------------------------------------------------------------
# ETAPA 1: CONSTRUCTOR (Compila la aplicación)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jdk-jammy AS builder
LABEL maintainer="Inventory MS Team"

WORKDIR /app

# 1. Copia archivos de la raíz del proyecto (configuración de Gradle)
COPY gradle/ gradle/
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .
RUN chmod +x ./gradlew

# Instalar dos2unix como root para solucionar el problema de formato CRLF/LF.
RUN apt-get update && apt-get install -y dos2unix && rm -rf /var/lib/apt/lists/*

# =====================================================================
# CORRECCIÓN DE MEMORIA DE GRADLE: Se añaden límites de memoria (OOM Kill)
# para el proceso de construcción de Gradle.
# =====================================================================
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Xmx512m -Xms256m"

# 2. COPIA DEL SERVICIO Y SCRIPTS RELACIONADOS
COPY product-service/ /app/product-service/

# =====================================================================
# ⭐ CORRECCIÓN DE PERMISOS (SOLUCIÓN FINAL CON DOS2UNIX) ⭐
# =====================================================================
# PASO CRÍTICO: Sanitizar el formato de línea (CRLF -> LF) con dos2unix,
# ya que sed falló por permisos.
RUN dos2unix /app/product-service/entrypoint.sh /app/product-service/wait-for-it.sh

# 2b. APLICAR PERMISOS DE EJECUCIÓN
RUN chmod +x /app/product-service/entrypoint.sh /app/product-service/wait-for-it.sh


# 3. DESCARGA Y CACHÉ DE DEPENDENCIAS
# Se elimina --no-daemon
RUN ./gradlew :product-service:dependencies -x test --console=plain


# 4. COMPILA EL JAR
# Se elimina --no-daemon
RUN ./gradlew :product-service:bootJar -x test --console=plain

# ---------------------------------------------------------------------
# ETAPA 2: EJECUCIÓN (Crea la imagen final mínima)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS runner
LABEL maintainer="Inventory MS Team"

# Puerto para Product Service.
EXPOSE 9090
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring
WORKDIR /app


COPY --from=builder /app/product-service/build/libs/product-service-*.jar /app/app.jar

# 5. COPIAR SCRIPTS DESDE LA UBICACIÓN EN EL BUILDER
# NOTA: Se ha eliminado el flag experimental --chmod=0755 ya que los permisos
# ya fueron establecidos correctamente en la etapa 'builder'
COPY --from=builder /app/product-service/entrypoint.sh /app/entrypoint.sh
COPY --from=builder /app/product-service/wait-for-it.sh /app/wait-for-it.sh

# 6. HACERLOS EJECUTABLES (ELIMINADO PORQUE YA SE HIZO EN LA ETAPA BUILDER)

ENTRYPOINT ["/app/entrypoint.sh"]