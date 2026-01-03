#!/bin/bash
# Script de entrada corregido para config-service.

# 1. Variables
WAIT_HOST="discovery-service"
WAIT_PORT="8761"
WAIT_TIMEOUT=60

echo "⏳ Esperando a que Eureka esté disponible en $WAIT_HOST:$WAIT_PORT..."

# 2. EJECUCIÓN CON FORMATO DESGLOSADO
# Usamos los flags explícitos -h y -p para que el script no se confunda
/app/wait-for-it.sh -h "$WAIT_HOST" -p "$WAIT_PORT" -t "$WAIT_TIMEOUT" -- java -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar

# 3. Manejo de error
if [ $? -ne 0 ]; then
    echo "❌ ERROR: El tiempo de espera para $WAIT_HOST ha expirado."
    exit 1
fi