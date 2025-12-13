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

# Copia específica del build.gradle y el código fuente del módulo (auth-service)
COPY auth-service/build.gradle auth-service/build.gradle
COPY auth-service/src auth-service/src

# Permisos de ejecución
RUN chmod +x ./gradlew

# =====================================================================
# CORRECCIÓN DE MEMORIA DE GRADLE: AUMENTO DEL HEAP A 1GB
# Esto corrige el error "Gradle build daemon disappeared unexpectedly" (OOM Kill).
# =====================================================================
ENV GRADLE_OPTS="-Dorg.gradle.daemon=false -Xmx2048m -Xms1024m -XX:MaxMetaspaceSize=1024m -Dorg.gradle.internal.http.connectionTimeout=600000 -Dorg.gradle.internal.http.socketTimeout=600000"

# Paso 1: Resolver y cachear dependencias
# Se elimina --no-daemon
RUN ./gradlew :auth-service:dependencies -x test --console=plain

# Paso 2: Ejecutar la compilación y generar el JAR
# Se elimina --no-daemon
RUN ./gradlew :auth-service:bootJar -x test --console=plain

# Verificar que el JAR se generó correctamente
RUN ls -lh auth-service/build/libs/ && \
    test -f auth-service/build/libs/auth-service-*.jar || \
    (echo "ERROR: JAR no fue generado" && exit 1)

# ---------------------------------------------------------------------
# ETAPA 2: EJECUCIÓN (Crea la imagen final mínima)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS runner
LABEL maintainer="Inventory MS Team"

# Puerto común de Auth Service
EXPOSE 8080

# Instalar dos2unix como root para solucionar el problema de formato CRLF/LF.
RUN apt-get update && apt-get install -y dos2unix && rm -rf /var/lib/apt/lists/*

RUN addgroup --system spring && adduser --system --ingroup spring spring
# USER spring:spring (Movido abajo, después de las operaciones de root)

WORKDIR /app

# Copiar el JAR compilado desde la etapa 'builder'
COPY --from=builder /app/auth-service/build/libs/auth-service-*.jar /app/app.jar

# ⭐ COPIAR LOS SCRIPTS DE ARRANQUE Y ESPERA (Asegúrate de que las rutas sean correctas)
COPY auth-service/entrypoint.sh ./
COPY wait-for-it.sh ./

# =====================================================================
# ⭐ CORRECCIÓN DE PERMISOS (SOLUCIÓN FINAL CON DOS2UNIX) ⭐
# =====================================================================
# PASO CRÍTICO: Sanitizar el formato de línea (CRLF -> LF) con dos2unix.
RUN dos2unix ./entrypoint.sh ./wait-for-it.sh

# Hacerlos ejecutables
RUN chmod +x entrypoint.sh wait-for-it.sh

# Cambiar al usuario spring después de las operaciones de instalación y corrección
USER spring:spring


# ⭐ ESTABLECER EL PUNTO DE ENTRADA CON EL SCRIPT
ENTRYPOINT ["/app/entrypoint.sh"]

# Usamos 'java -jar /app/app.jar' dentro del entrypoint.sh