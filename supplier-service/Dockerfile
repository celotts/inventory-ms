# ----------------------------------------------------------------------
# RUNTIME STAGE (JDK 21)
# ----------------------------------------------------------------------
FROM eclipse-temurin:21-jdk-jammy

ARG DEBIAN_FRONTEND=noninteractive
SHELL ["/bin/bash", "-o", "pipefail", "-c"]

# Instala bash y curl para el entrypoint
RUN apt-get update \
 && apt-get install -y bash curl \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# 📦 Copia el .jar desde la raíz del contexto de construcción (..)
# 💥 CORRECCIÓN FINAL: Usamos el nombre de archivo JAR esperado (supplier-service-0.0.1-SNAPSHOT.jar)
# para garantizar que el archivo se encuentre en el contexto de construcción.
COPY supplier-service/build/libs/supplier-service-0.0.1-SNAPSHOT.jar app.jar

# 🔁 Copia los scripts de ejecución desde el root del contexto
COPY infra/scripts/wait-for-it.sh ./
COPY infra/scripts/entrypoint.sh ./

# ✅ Asegura permisos de ejecución
RUN chmod +x wait-for-it.sh entrypoint.sh

EXPOSE 9091

# Define la ruta del JAR para el script entrypoint
ENV JAR_PATH=/app/app.jar

# Usa el script como Entrypoint del contenedor
ENTRYPOINT ["bash", "entrypoint.sh"]