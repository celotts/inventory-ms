# Imagen de runtime (se mantiene JDK si lo necesitas para herramientas de desarrollo/debugging)
FROM eclipse-temurin:21-jdk-jammy

ARG DEBIAN_FRONTEND=noninteractive
SHELL ["/bin/bash", "-o", "pipefail", "-c"]
# Instala bash y curl en una sola capa limpia
RUN apt-get update \
 && apt-get install -y bash curl \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# 🔁 Copia los scripts desde la ubicación central (infra/scripts)
# ¡CORRECCIÓN APLICADA AQUÍ!
COPY ../infra/scripts/wait-for-it.sh ./
COPY ../infra/scripts/entrypoint.sh ./

# ✅ Asegura permisos de ejecución
RUN chmod +x wait-for-it.sh entrypoint.sh

# 📦 Copia el .jar generado por Gradle (Asegúrate de que el nombre del JAR sea correcto)
COPY tax-service/build/libs/*.jar app.jar

EXPOSE 9092

ENTRYPOINT ["bash", "entrypoint.sh"]