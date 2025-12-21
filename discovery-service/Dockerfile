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

# Copia específica del build.gradle y el código fuente del módulo
COPY discovery-service/build.gradle discovery-service/build.gradle
COPY discovery-service/src discovery-service/src

# === INICIO DE CORRECCIÓN CRÍTICA DE FORMATO Y PERMISOS ===

# 1. Instalar dos2unix temporalmente en la etapa builder para la corrección
RUN apt-get update && \
    apt-get install -y dos2unix --no-install-recommends && \
    rm -rf /var/lib/apt/lists/*

# 2. ⭐ APLICAR CORRECCIÓN DE FORMATO AL SCRIPT GRADLEW ⭐
RUN dos2unix ./gradlew

# 3. Asignar permisos de ejecución
RUN chmod +x ./gradlew

# =====================================================================
# CORRECCIÓN DE MEMORIA DE GRADLE: AUMENTO DEL HEAP A 1GB
# =====================================================================
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Xmx2048m -Xms1024m -XX:MaxMetaspaceSize=1024m -Dorg.gradle.internal.http.connectionTimeout=600000 -Dorg.gradle.internal.http.socketTimeout=600000"

# =====================================================================
# ⭐ CORRECCIÓN CRÍTICA: DESCARGA/GENERACIÓN DEL GRADLE WRAPPER JAR ⭐
# Resuelve el error "Unable to access jarfile /app/gradle/wrapper/gradle-wrapper.jar"
# =====================================================================

# 1. Forzar la descarga del gradle-wrapper.jar.
RUN ./gradlew wrapper --no-daemon

# 2. Resolver y cachear dependencias (discovery-service)
# Volvemos a usar -s por si falla por dependencias/red.
RUN ./gradlew :discovery-service:dependencies --no-daemon -s

# 3. Ejecutar la compilación y generar el JAR (discovery-service)
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

# 1. Crea el usuario 'spring'
RUN addgroup --system spring && adduser --system --ingroup spring spring

WORKDIR /app

# 2. Copia el JAR y los scripts (AÚN COMO USUARIO ROOT)
COPY --from=builder /app/discovery-service/build/libs/discovery-service-*.jar /app/app.jar
COPY discovery-service/entrypoint.sh ./
COPY wait-for-it.sh ./

# =====================================================================
# ⭐ CORRECCIÓN DE PERMISOS (ETAPA RUNNER) ⭐
# =====================================================================
# PASO CRÍTICO: Sanitizar el formato de línea (CRLF -> LF) con dos2unix.
#RUN dos2unix ./entrypoint.sh ./wait-for-it.sh

# 3. APLICAR PERMISOS: Se ejecuta como ROOT
RUN chmod +x entrypoint.sh wait-for-it.sh

# 4. ASIGNAR PROPIEDAD: Cambia el dueño de /app y todos sus archivos a 'spring'
RUN chown -R spring:spring /app

# 5. CAMBIAR AL USUARIO SIN PRIVILEGIOS
USER spring:spring

ENTRYPOINT ["/app/entrypoint.sh"]