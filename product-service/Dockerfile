# ----------------------------------------------------------------------
# RUNTIME STAGE (Usando JDK 21 como base, según la solicitud)
# Base con JDK para herramientas de desarrollo/debugging.
# ----------------------------------------------------------------------
FROM eclipse-temurin:21-jdk-jammy

ARG DEBIAN_FRONTEND=noninteractive
SHELL ["/bin/bash", "-o", "pipefail", "-c"]

# Instala bash y curl para el entrypoint y el script wait-for-it
RUN apt-get update \
 && apt-get install -y bash curl \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# 📦 Copia el .jar generado por Gradle/Maven y lo renombra a 'app.jar'
# NOTA: Esta ruta asume que el JAR ya fue construido por Gradle.
COPY product-service/build/libs/*.jar app.jar

# 🔁 Copia los scripts de ejecución desde el directorio 'infra/scripts'
# IMPORTANTE: Esto asume que el comando 'docker build' se ejecuta desde el
# directorio raíz del proyecto para que la ruta 'infra/scripts' sea válida.
COPY infra/scripts/wait-for-it.sh ./
COPY infra/scripts/entrypoint.sh ./

# ✅ Asegura permisos de ejecución
RUN chmod +x wait-for-it.sh entrypoint.sh

EXPOSE 9090

# Define la ruta del JAR para el script entrypoint
ENV JAR_PATH=/app/app.jar

# Usa el script como Entrypoint del contenedor
ENTRYPOINT ["bash", "entrypoint.sh"]

# Comando por defecto (si el entrypoint no lo reemplaza)
CMD ["java", "-jar", "/app/app.jar"]