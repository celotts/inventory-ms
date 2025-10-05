# supplier-service/Dockerfile
FROM eclipse-temurin:21-jdk-jammy

RUN set -eux; \
    sed -i -e 's|http://|https://|g' /etc/apt/sources.list; \
    apt-get -o Acquire::Retries=5 -o Acquire::http::Timeout=30 update; \
    apt-get install -y --no-install-recommends bash curl ca-certificates; \
    rm -rf /var/lib/apt/lists/*

WORKDIR /app
COPY wait-for-it.sh entrypoint.sh ./
RUN chmod +x wait-for-it.sh entrypoint.sh

# 👇 usa wildcard para no fallar por el nombre exacto
COPY build/libs/*.jar app.jar

EXPOSE 9091
ENTRYPOINT ["bash", "entrypoint.sh"]