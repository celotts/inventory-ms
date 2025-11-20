FROM eclipse-temurin:21-jre-jammy

# utilidades mínimas
RUN apt-get update \
 && apt-get install -y bash curl \
 && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# 1. COPIA DE SCRIPTS (Usando la ruta relativa al contexto raíz)
# wait-for-it.sh (compartido)
COPY infra/scripts/wait-for-it.sh ./
# entrypoint.sh (local al servicio)
COPY config-service/entrypoint.sh ./

# 2. ASIGNACIÓN DE PERMISOS (DEBE IR DESPUÉS DE COPIAR)
RUN chmod +x wait-for-it.sh entrypoint.sh

# 3. COPIA DEL JAR (Usando la ruta relativa al contexto raíz)
COPY config-service/build/libs/*.jar app.jar

EXPOSE 8888

# El entrypoint usa el script que acabamos de copiar y hacer ejecutable
ENTRYPOINT ["bash", "entrypoint.sh"]