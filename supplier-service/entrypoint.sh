#!/bin/sh
# entrypoint.sh - Lógica de arranque para Supplier Service (con BD)

# 1. Aseguramos que el WAIT_TIMEOUT tenga un valor por defecto si la variable está vacía
WAIT_TIMEOUT=${WAIT_TIMEOUT:-60}
WAIT_FOR_IT="/app/wait-for-it.sh"
JAR_NAME="/app/app.jar"

# DEPENDENCIAS
DISCOVERY_HOST="discovery-service:8761"
CONFIG_HOST="config-service:7777"
DB_HOST="supplier-db:5432"

echo "======================================================"
echo " INICIANDO ENTRYPOINT CON BD (Supplier)"
echo "======================================================"

# 1. Esperar a la Base de Datos
echo "-> 1/3 Esperando a la base de datos en $DB_HOST..."
# CORRECCIÓN: Quitamos el 'exec' implícito de wait-for-it usando solo los comandos de comprobación
sh $WAIT_FOR_IT "$DB_HOST" -t "$WAIT_TIMEOUT" -- echo "DB OK"

# 2. Esperar al Servidor de Descubrimiento (Eureka)
echo "-> 2/3 Esperando a Discovery Service en $DISCOVERY_HOST..."
sh $WAIT_FOR_IT "$DISCOVERY_HOST" -t 60 -- echo "Discovery OK"

# 3. Esperar al Servidor de Configuración (Config Server)
echo "-> 3/3 Esperando a Config Service en $CONFIG_HOST..."
sh $WAIT_FOR_IT "$CONFIG_HOST" -t 60 -- echo "Config OK"

# 4. Lanzar la aplicación principal
echo "-> Lanzando la aplicación con Java..."
# Usamos la ruta absoluta y nos aseguramos de que exec reciba el comando limpio
exec java -jar "$JAR_NAME"