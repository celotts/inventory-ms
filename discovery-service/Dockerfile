# ---------------------------------------------------------------------
# ETAPA 1: CONSTRUCTOR (Compila la aplicación)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jdk-jammy AS builder
LABEL maintainer="Inventory MS Team"

#ARG DOCKER_USERNAME
#ARG DOCKER_PASSWORD

WORKDIR /app

# Copias iniciales de archivos de Gradle del proyecto raíz
COPY gradle/ gradle/
COPY gradlew .
COPY settings.gradle .
COPY build.gradle .

# Copia específica del build.gradle y el código fuente del módulo (discovery-service)
COPY discovery-service/build.gradle discovery-service/build.gradle
COPY discovery-service/src discovery-service/src

# Permisos de ejecución
RUN chmod +x ./gradlew

# =====================================================================
# CORRECCIÓN DE MEMORIA DE GRADLE: Se añaden límites de memoria (OOM Kill)
# =====================================================================
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Xmx512m -Xms256m"

# Paso 1: Resolver y cachear dependencias (discovery-service)
RUN ./gradlew :discovery-service:dependencies -x test --console=plain

# Paso 2: Ejecutar la compilación y generar el JAR (discovery-service)
RUN ./gradlew :discovery-service:bootJar -x test --console=plain

# Verificar que el JAR se generó correctamente
RUN ls -lh discovery-service/build/libs/ && \
    test -f discovery-service/build/libs/discovery-service-*.jar || \
    (echo "ERROR: JAR no fue generado" && exit 1)

# ---------------------------------------------------------------------
# ETAPA 2: EJECUCIÓN (Crea la imagen final mínima)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS runner
LABEL maintainer="Inventory MS Team"
EXPOSE 8761

# Instalar dos2unix como root para solucionar el problema de formato CRLF/LF.
RUN apt-get update && apt-get install -y dos2unix && rm -rf /var/lib/apt/lists/*

# 1. Crea el usuario 'spring'
RUN addgroup --system spring && adduser --system --ingroup spring spring

WORKDIR /app

# 2. Copia el JAR y los scripts (AÚN COMO USUARIO ROOT)
COPY --from=builder /app/discovery-service/build/libs/discovery-service-*.jar /app/app.jar
COPY discovery-service/entrypoint.sh ./
COPY wait-for-it.sh ./

# =====================================================================
# ⭐ CORRECCIÓN DE PERMISOS (SOLUCIÓN FINAL CON DOS2UNIX) ⭐
# =====================================================================
# PASO CRÍTICO: Sanitizar el formato de línea (CRLF -> LF) con dos2unix.
RUN dos2unix ./entrypoint.sh ./wait-for-it.sh

# ⭐ 3. APLICAR PERMISOS: Se ejecuta como ROOT (el usuario por defecto de la imagen)
RUN chmod +x entrypoint.sh wait-for-it.sh

# ⭐ 4. ASIGNAR PROPIEDAD: Cambia el dueño de /app y todos sus archivos a 'spring'
RUN chown -R spring:spring /app

# ⭐ 5. CAMBIAR AL USUARIO SIN PRIVILEGIOS
USER spring:spring

ENTRYPOINT ["/app/entrypoint.sh"]