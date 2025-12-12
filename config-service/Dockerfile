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

# Copia específica del build.gradle y el código fuente del módulo (!!! CAMBIO A CONFIG-SERVICE !!!)
COPY config-service/build.gradle config-service/build.gradle
COPY config-service/src config-service/src

# Permisos de ejecución
RUN chmod +x ./gradlew

# =====================================================================
# CORRECCIÓN DE MEMORIA DE GRADLE: AUMENTO DEL HEAP A 1GB
# Esto corrige el error "Gradle build daemon disappeared unexpectedly" (OOM Kill).
# =====================================================================
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Xmx2048m -Xms1024m -XX:MaxMetaspaceSize=1024m -Dorg.gradle.internal.http.connectionTimeout=600000 -Dorg.gradle.internal.http.socketTimeout=600000"

# Paso 1: Resolver y cachear dependencias (!!! CAMBIO A CONFIG-SERVICE !!!)
# Se elimina --no-daemon
RUN ./gradlew :config-service:dependencies -x test --console=plain

# Paso 2: Ejecutar la compilación y generar el JAR (!!! CAMBIO A CONFIG-SERVICE !!!)
# Se elimina --no-daemon
RUN ./gradlew :config-service:bootJar -x test --console=plain

# Verificar que el JAR se generó correctamente
RUN ls -lh config-service/build/libs/ && \
    test -f config-service/build/libs/config-service-*.jar || \
    (echo "ERROR: JAR no fue generado" && exit 1)

# ---------------------------------------------------------------------
# ETAPA 2: EJECUCIÓN (Crea la imagen final mínima)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS runner
LABEL maintainer="Inventory MS Team"

# ⭐ PUERTO DEL CONFIG SERVICE
EXPOSE 7777

# Instalar dos2unix como root
# (Se mueve este paso antes de añadir el usuario para usar permisos de root)
RUN apt-get update && apt-get install -y dos2unix && rm -rf /var/lib/apt/lists/*

RUN addgroup --system spring && adduser --system --ingroup spring spring
# USER spring:spring (Se mueve abajo, después de las operaciones de root)

WORKDIR /app

# Copiar el JAR compilado desde la etapa 'builder' (!!! CAMBIO A CONFIG-SERVICE !!!)
COPY --from=builder /app/config-service/build/libs/config-service-*.jar /app/app.jar

# ⭐ COPIAR LOS SCRIPTS DE ARRANQUE Y ESPERA
# ASUMIMOS que wait-for-it.sh está DENTRO de config-service/ junto a entrypoint.sh
COPY config-service/entrypoint.sh ./
COPY config-service/wait-for-it.sh ./

# =====================================================================
# ⭐ CORRECCIÓN DE PERMISOS (SOLUCIÓN FINAL CON DOS2UNIX) ⭐
# =====================================================================
# PASO CRÍTICO: Sanitizar el formato de línea (CRLF -> LF) de forma robusta.
# dos2unix es una utilidad dedicada que resuelve este problema de raíz.
RUN dos2unix ./entrypoint.sh ./wait-for-it.sh

# Hacerlos ejecutables (AHORA FUNCIONARÁ)
RUN chmod +x entrypoint.sh wait-for-it.sh

# Cambiar al usuario spring después de las operaciones de instalación y corrección
USER spring:spring


# ⭐ ESTABLECER EL PUNTO DE ENTRADA CON EL SCRIPT
ENTRYPOINT ["/app/entrypoint.sh"]

# Usamos 'java -jar /app/app.jar' dentro del entrypoint.sh