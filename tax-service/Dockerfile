FROM eclipse-temurin:21-jre-jammy

RUN apt-get update \
 && apt-get install -y bash curl \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app

COPY wait-for-it.sh entrypoint.sh ./
RUN chmod +x wait-for-it.sh entrypoint.sh

# Copia el jar sin “clavar” el nombre
COPY build/libs/*.jar app.jar

EXPOSE 9092
ENTRYPOINT ["bash", "entrypoint.sh"]