ARG DOCKER_USERNAME
ARG DOCKER_PASSWORD

FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
EXPOSE 8888

# ✅ Añadimos ./ para forzar ruta relativa al contexto
COPY ./config-service/build/libs/config-service-*.jar /app/app.jar

COPY ./config-service/entrypoint.sh ./
COPY ./infra/wait-for-it.sh ./

RUN apt-get update && apt-get install -y dos2unix && rm -rf /var/lib/apt/lists/*

RUN dos2unix ./entrypoint.sh ./wait-for-it.sh && \
    chmod +x ./entrypoint.sh ./wait-for-it.sh

RUN addgroup --system spring && adduser --system --ingroup spring spring && \
    chown -R spring:spring /app

USER spring:spring
ENTRYPOINT ["/app/entrypoint.sh"]