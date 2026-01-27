#!/bin/bash
# Script de entrada corregido y simplificado

WAIT_FOR_SERVICE="discovery-service"
WAIT_PORT="8761"
TIMEOUT=60

echo "🔍 Verificando conexión con $WAIT_FOR_SERVICE:$WAIT_PORT..."

# Usamos un bucle nativo de Bash para verificar el puerto
# Esto no requiere scripts externos como wait-for-it.sh
COUNTER=0
until (echo > /dev/tcp/$WAIT_FOR_SERVICE/$WAIT_PORT) >/dev/null 2>&1; do
    if [ $COUNTER -ge $TIMEOUT ]; then
        echo "❌ ERROR: Tiempo de espera agotado para $WAIT_FOR_SERVICE"
        exit 1
    fi
    echo "⏳ Esperando a Eureka... ($COUNTER/$TIMEOUT)"
    sleep 2
    COUNTER=$((COUNTER + 2))
done

echo "✅ Eureka detectado y respondiendo!"
echo "🚀 Iniciando aplicación Java..."

# Iniciamos Java usando exec para manejar señales de cierre (SIGTERM)
exec java -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar