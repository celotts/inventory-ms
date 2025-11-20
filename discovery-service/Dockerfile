# ----------------------------------------
# STAGE 1: BUILDER (Compila el JAR)
# ----------------------------------------
# Usamos una imagen JDK (más grande) para ejecutar Gradle y compilar la aplicación.
FROM eclipse-temurin:21-jdk-jammy AS builder

# Establece el directorio de trabajo dentro del contenedor.
WORKDIR /app

# Copia los archivos de Gradle necesarios para la compilación (ajusta estas rutas si tu contexto es diferente)
COPY gradlew .
COPY gradle /app/gradle
COPY build.gradle /app/
COPY settings.gradle /app/

# Copia el código fuente completo del servicio de descubrimiento
COPY discovery-service /app/discovery-service

# Ejecuta el build de la aplicación, creando el JAR.
# Usamos :discovery-service:bootJar para asegurarnos de construir solo este módulo.
RUN chmod +x ./gradlew && ./gradlew :discovery-service:bootJar --no-daemon -x test

# ----------------------------------------
# STAGE 2: RUNNER (Ejecuta la Aplicación)
# ----------------------------------------
# Usamos la imagen JRE (más ligera) para el runtime final.
FROM eclipse-temurin:21-jre-jammy

# Instalamos utilidades necesarias (bash y curl son comunes para scripts de entrypoint)
RUN apt-get update && apt-get install -y bash curl && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copiamos el JAR compilado desde la etapa 'builder' (¡La parte clave del Multi-Stage!)
# La ruta es relativa al directorio de trabajo en la etapa 'builder'.
COPY --from=builder /app/discovery-service/build/libs/*.jar app.jar

# Copiamos los scripts de utilidades y de inicio.
COPY infra/scripts/wait-for-it.sh ./
COPY discovery-service/entrypoint.sh ./
RUN chmod +x wait-for-it.sh entrypoint.sh

# Puerto de Eureka (predeterminado)
EXPOSE 8761

# Comando de inicio
ENTRYPOINT ["bash", "entrypoint.sh"]