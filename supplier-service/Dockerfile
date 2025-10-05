FROM eclipse-temurin:21-jdk-jammy

# Instala bash y curl en una sola capa limpia
RUN set -eux; \
    sed -i -e 's|http://|https://|g' /etc/apt/sources.list; \
    apt-get -o Acquire::Retries=5 -o Acquire::http::Timeout=30 update; \
    apt-get install -y --no-install-recommends bash curl ca-certificates; \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app

# 🔁 Copia los scripts primero
COPY wait-for-it.sh wait-for-it.sh
COPY entrypoint.sh entrypoint.sh

# ✅ Asegura permisos de ejecución después de copiarlos
RUN chmod +x wait-for-it.sh entrypoint.sh

# 📦 Copia el .jar generado por Gradle
COPY build/libs/SUPPLIER-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 9091

ENTRYPOINT ["bash", "entrypoint.sh"]
# 🔕 No pongas ENTRYPOINT aquí si lo defines en docker-compose

