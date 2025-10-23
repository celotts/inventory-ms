FROM eclipse-temurin:21-jdk-jammy

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

# 📦 Copia el .jar correcto para API Gateway
COPY build/libs/api-gateway-0.0.1-SNAPSHOT.jar app.jar

# 🌐 Puerto actualizado para API Gateway
EXPOSE 8090

ENTRYPOINT ["bash", "entrypoint.sh"]