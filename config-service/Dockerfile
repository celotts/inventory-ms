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

# Copia específica del build.gradle y el código fuente del módulo (!!! CAMBIO A CONFIG-SERVICE !!!)
COPY config-service/build.gradle config-service/build.gradle
COPY config-service/src config-service/src

# Permisos de ejecución
RUN chmod +x ./gradlew

# Configuración de timeouts para Gradle (Opcional, pero se mantiene)
ENV GRADLE_OPTS="-Dorg.gradle.internal.http.connectionTimeout=600000 -Dorg.gradle.internal.http.socketTimeout=600000"

# Paso 1: Resolver y cachear dependencias (!!! CAMBIO A CONFIG-SERVICE !!!)
RUN ./gradlew :config-service:dependencies -x test --console=plain --no-daemon

# Paso 2: Ejecutar la compilación y generar el JAR (!!! CAMBIO A CONFIG-SERVICE !!!)
RUN ./gradlew :config-service:bootJar -x test --console=plain --no-daemon

# ---------------------------------------------------------------------
# ETAPA 2: EJECUCIÓN (Crea la imagen final mínima)
# ---------------------------------------------------------------------
FROM eclipse-temurin:21-jre-jammy AS runner
LABEL maintainer="Inventory MS Team"

# ⭐ PUERTO DEL CONFIG SERVICE
EXPOSE 7777

RUN addgroup --system spring && adduser --system --ingroup spring spring
USER spring:spring

WORKDIR /app

# Copiar el JAR compilado desde la etapa 'builder' (!!! CAMBIO A CONFIG-SERVICE !!!)
COPY --from=builder /app/config-service/build/libs/config-service-*.jar /app/app.jar

# ⭐ COPIAR LOS SCRIPTS DE ARRANQUE Y ESPERA
COPY config-service/entrypoint.sh ./
COPY wait-for-it.sh ./

# Hacerlos ejecutables
#RUN chmod +x entrypoint.sh wait-for-it.sh

# ⭐ ESTABLECER EL PUNTO DE ENTRADA CON EL SCRIPT
ENTRYPOINT ["/app/entrypoint.sh"]

# Usamos 'java -jar /app/app.jar' dentro del entrypoint.sh