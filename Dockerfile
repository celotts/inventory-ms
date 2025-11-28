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
COPY supplier-service/build.gradle supplier-service/build.gradle
COPY supplier-service/src supplier-service/src

# Permisos de ejecución
RUN chmod +x ./gradlew

# Configuración de timeouts para Gradle
ENV GRADLE_OPTS="-Dorg.gradle.internal.http.connectionTimeout=600000 -Dorg.gradle.internal.http.socketTimeout=600000"

# Paso 1: Resolver y cachear dependencias
RUN ./gradlew :supplier-service:dependencies -x test --console=plain --no-daemon

# Paso 2: Ejecutar la compilación y generar el JAR
RUN ./gradlew :supplier-service:bootJar -x test --console=plain --no-daemon

# ---------------------------------------------------------------------
# ETAPA 2: EJECUCIÓN (Crea la imagen final mínima)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS runner
LABEL maintainer="Inventory MS Team"

EXPOSE 9091

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

WORKDIR /app

# Copiar el JAR compilado desde la etapa 'builder'
COPY --from=builder /app/supplier-service/build/libs/supplier-service-*.jar /app/app.jar

# ENTRYPOINT para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "/app/app.jar"]