#!/bin/sh
# entrypoint.sh - Lógica de arranque para el Discovery Service (Eureka)

# El Discovery Service es el primer servicio de infraestructura y no debe esperar a nadie.

# Variables (Asume que el JAR fue renombrado a /app/app.jar)
JAR_NAME=${1:-app.jar}

echo "======================================================"
echo " INICIANDO ENTRYPOINT DE INFRAESTRUCTURA (Discovery) para $JAR_NAME"
echo "======================================================"

echo "-> Iniciando sin dependencias de red externas..."

# Lanzar la aplicación principal
exec java -jar /app/$JAR_NAME