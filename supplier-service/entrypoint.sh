#!/bin/sh
# entrypoint.sh - Lógica de arranque para Supplier Service (con BD)

# ----------------------------------------------------------------
# CONFIGURACIÓN Y RUTAS
# ----------------------------------------------------------------
WAIT_FOR_IT=/app/wait-for-it.sh
JAR_NAME=${1:-app.jar}

# DEPENDENCIAS DE INFRAESTRUCTURA
DISCOVERY_HOST=discovery-service:8761
CONFIG_HOST=config-service:7777

# ⭐⭐⭐ AJUSTADO PARA SUPPLIER SERVICE ⭐⭐⭐
DB_HOST=supplier-db:5432

echo "======================================================"
echo " INICIANDO ENTRYPOINT CON BD (Supplier) para $JAR_NAME"
echo "======================================================"

# 1. Esperar a la Base de Datos
echo "-> 1/3 Esperando a la base de datos en $DB_HOST..."
$WAIT_FOR_IT $DB_HOST -t 90 -- echo "Base de datos OK."

# 2. Esperar al Servidor de Descubrimiento (Eureka)
echo "-> 2/3 Esperando a Discovery Service en $DISCOVERY_HOST..."
$WAIT_FOR_IT $DISCOVERY_HOST -t 60 -- echo "Discovery Service OK."

# 3. Esperar al Servidor de Configuración (Config Server)
echo "-> 3/3 Esperando a Config Service en $CONFIG_HOST..."
$WAIT_FOR_IT $CONFIG_HOST -t 60 -- echo "Config Service OK. Procediendo..."


# 4. Lanzar la aplicación principal
echo "-> Lanzando la aplicación $JAR_NAME..."
exec java -jar /app/$JAR_NAME