#!/bin/bash
# wait-for-it.sh (Modo Silencioso y Permisivo)

HOST=$1
PORT=$2
TIMEOUT=30

echo "⏳ Verificando $HOST:$PORT..."

for i in $(seq 1 $TIMEOUT); do
  if bash -c "cat < /dev/tcp/$HOST/$PORT" > /dev/null 2>&1; then
    echo "✅ $HOST:$PORT está vivo!"
    exit 0
  fi
  sleep 1
done

echo "⚠️ Advertencia: No se pudo conectar a $HOST:$PORT tras ${TIMEOUT}s, pero seguiremos adelante..."
exit 0 # Forzamos éxito para no bloquear el arranque
