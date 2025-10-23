FROM eclipse-temurin:21-jdk-jammy

# Instala herramientas necesarias
RUN apt-get update \
 && apt-get install -y bash curl \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Copia los scripts y da permisos
COPY wait-for-it.sh entrypoint.sh ./
RUN chmod +x wait-for-it.sh entrypoint.sh

# Copia el JAR exacto
COPY build/libs/config-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["bash", "entrypoint.sh"]