# Imagen de runtime (no necesitas JDK para ejecutar)
FROM eclipse-temurin:21-jre-jammy

# utilidades mínimas (solo si de verdad las necesitas)
RUN apt-get update \
 && apt-get install -y bash curl \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Scripts
COPY wait-for-it.sh entrypoint.sh ./
RUN chmod +x wait-for-it.sh entrypoint.sh

# Copia el JAR cualquiera que sea su nombre
COPY build/libs/*.jar app.jar

EXPOSE 8761
ENTRYPOINT ["bash", "entrypoint.sh"]