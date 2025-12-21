#!/bin/sh
# entrypoint.sh - Lógica de arranque corregida

# Usamos la ruta donde el log dice que está el script
WAIT_FOR_IT=/app/wait-for-it.sh
JAR_NAME=$1

echo "======================================================"
echo " INICIANDO ENTRYPOINT ESTÁNDAR para $JAR_NAME"
echo "======================================================"

# 1. Esperar al Discovery Service
echo "-> 1/3 Esperando a Discovery Service en discovery-service:8761..."
$WAIT_FOR_IT discovery-service:8761 --timeout=60 --strict -- echo "-> Discovery Service OK"

# 2. Esperar al Config Service (Tus logs mostraron que fallaba aquí también)
echo "-> 2/3 Esperando a Config Service en config-service:7777..."
$WAIT_FOR_IT config-service:7777 --timeout=60 --strict -- echo "-> Config Service OK"

# 3. Esperar a la Base de Datos (Solo si la variable DB_HOST existe)
# En tu docker-compose para Tax es 'db_tax'
if [ -n "$DB_HOST" ]; then
    echo "-> 3/3 Esperando a Base de Datos en $DB_HOST:5432..."
    $WAIT_FOR_IT $DB_HOST:5432 --timeout=60 --strict -- echo "-> Base de Datos OK"
fi

echo "-> Lanzando la aplicación $JAR_NAME..."
exec java $JAVA_OPTS -jar /app/$JAR_NAME