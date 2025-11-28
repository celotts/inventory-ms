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

# Copia específica del build.gradle y el código fuente del módulo (auth-service)
COPY auth-service/build.gradle auth-service/build.gradle
COPY auth-service/src auth-service/src

# Permisos de ejecución
RUN chmod +x ./gradlew

# Configuración de timeouts para Gradle
ENV GRADLE_OPTS="-Dorg.gradle.internal.http.connectionTimeout=600000 -Dorg.gradle.internal.http.socketTimeout=600000"

# Paso 1: Resolver y cachear dependencias
RUN ./gradlew :auth-service:dependencies -x test --console=plain --no-daemon

# Paso 2: Ejecutar la compilación y generar el JAR
RUN ./gradlew :auth-service:bootJar -x test --console=plain --no-daemon

# ---------------------------------------------------------------------
# ETAPA 2: EJECUCIÓN (Crea la imagen final mínima)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS runner
LABEL maintainer="Inventory MS Team"

# Puerto común de Auth Service
EXPOSE 8080

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

WORKDIR /app

# Copiar el JAR compilado desde la etapa 'builder'
COPY --from=builder /app/auth-service/build/libs/auth-service-*.jar /app/app.jar

# ⭐ COPIAR LOS SCRIPTS DE ARRANQUE Y ESPERA
# Asegúrese de que wait-for-it.sh esté en la carpeta raíz del proyecto
COPY auth-service/entrypoint.sh ./
COPY wait-for-it.sh ./

# Hacerlos ejecutables
RUN chmod +x entrypoint.sh wait-for-it.sh

# ⭐ ESTABLECER EL PUNTO DE ENTRADA CON EL SCRIPT
ENTRYPOINT ["/app/entrypoint.sh"]

# Usamos 'java -jar /app/app.jar' dentro del entrypoint.sh