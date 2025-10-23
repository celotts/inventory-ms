FROM eclipse-temurin:21-jdk-jammy

ARG DEBIAN_FRONTEND=noninteractive
SHELL ["/bin/bash", "-o", "pipefail", "-c"]
# Instala bash y curl en una sola capa limpia
RUN apt-get update \
 && apt-get install -y bash curl \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# 🔁 Copia los scripts primero
COPY wait-for-it.sh wait-for-it.sh
COPY entrypoint.sh entrypoint.sh

# ✅ Asegura permisos de ejecución después de copiarlos
RUN chmod +x wait-for-it.sh entrypoint.sh

# 📦 Copia el .jar generado por Gradle
COPY build/libs/product-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9090

ENTRYPOINT ["bash", "entrypoint.sh"]
# 🔕 No pongas ENTRYPOINT aquí si lo defines en docker-compose

