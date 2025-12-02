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

# 2. COPIA DEL SERVICIO Y SCRIPTS RELACIONADOS
COPY product-service/ /app/product-service/

# 2b. CORRECCIÓN CRÍTICA: Aplicar CHMOD AQUÍ (Donde debe funcionar)
RUN chmod +x /app/product-service/entrypoint.sh /app/product-service/wait-for-it.sh


# 3. DESCARGA Y CACHÉ DE DEPENDENCIAS
RUN ./gradlew :product-service:dependencies -x test --console=plain --no-daemon


# 4. COMPILA EL JAR
RUN ./gradlew :product-service:bootJar -x test --console=plain --no-daemon

# ---------------------------------------------------------------------
# ETAPA 2: EJECUCIÓN (Crea la imagen final mínima)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS runner
LABEL maintainer="Inventory MS Team"

# Puerto para Product Service
EXPOSE 9090
RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring
WORKDIR /app


COPY --from=builder /app/product-service/build/libs/product-service-*.jar /app/app.jar

# 5. COPIAR SCRIPTS DESDE LA UBICACIÓN EN EL BUILDER
COPY --from=builder /app/product-service/entrypoint.sh /app/entrypoint.sh
COPY --from=builder /app/product-service/wait-for-it.sh /app/wait-for-it.sh

# 6. HACERLOS EJECUTABLES (ELIMINAMOS ESTE PASO QUE FALLA)
# RUN chmod +x /app/entrypoint.sh /app/wait-for-it.sh <--- ¡ELIMINADO!

ENTRYPOINT ["/app/entrypoint.sh"]