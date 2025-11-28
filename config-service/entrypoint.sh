#!/bin/sh
# entrypoint.sh - Lógica de arranque para el Config Service

# Variables (Asume que el JAR fue renombrado a /app/app.jar)
JAR_NAME=${1:-app.jar}

echo "======================================================"
echo " INICIANDO ENTRYPOINT DE INFRAESTRUCTURA (Config) para $JAR_NAME"
echo "======================================================"

echo "-> Iniciando sin dependencias de red externas. Nota: La configuración de espera a Eureka debe estar en el docker-compose/application.yml."

# Lanzar la aplicación principal
exec java -jar /app/$JAR_NAME