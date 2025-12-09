#!/bin/sh
# entrypoint.sh - Lógica de arranque para Tax Service

# ----------------------------------------------------------------
# CONFIGURACIÓN Y RUTAS
# ----------------------------------------------------------------
WAIT_FOR_IT=/app/wait-for-it.sh
JAR_NAME=${1:-app.jar}

# DEPENDENCIAS DE INFRAESTRUCTURA (Nombres de servicio del docker-compose)
DISCOVERY_HOST=discovery-service:8761
CONFIG_HOST=config-service:7777

echo "======================================================"
echo " INICIANDO ENTRYPOINT ESTÁNDAR (Tax) para $JAR_NAME"
echo "======================================================"

# 1. Esperar al Servidor de Descubrimiento (Eureka)
echo "-> 1/2 Esperando a Discovery Service en $DISCOVERY_HOST..."
$WAIT_FOR_IT $DISCOVERY_HOST -t 60 -- echo "Discovery Service OK."

# 2. Esperar al Servidor de Configuración (Config Server)
echo "-> 2/2 Esperando a Config Service en $CONFIG_HOST..."
$WAIT_FOR_IT $CONFIG_HOST -t 60 -- echo "Config Service OK. Procediendo..."


# 3. Lanzar la aplicación principal
echo "-> Lanzando la aplicación $JAR_NAME..."
exec java -jar /app/$JAR_NAME