#!/usr/bin/env bash
set -euo pipefail

# Variables opcionales:
# WAIT_FOR   -> "host1:port1,host2:port2"
# WAIT_TIMEOUT -> segundos (default 30)

# --- INICIO DE CORRECCIÓN ---
if [[ -n "${WAIT_FOR:-}" ]]; then
  echo "🕒 Esperando dependencias: $WAIT_FOR (${WAIT_TIMEOUT:-30}s)..."

  # Divide la cadena WAIT_FOR en elementos separados por coma
  IFS=',' read -ra HOST_PORTS <<< "$WAIT_FOR"

  # Itera sobre cada dependencia y la espera individualmente
  for HOST_PORT in "${HOST_PORTS[@]}"; do
    echo "  ⏳ Esperando servicio individual: $HOST_PORT"
    # Llama al script de espera con un solo host:port
    bash /app/wait-for-it.sh "$HOST_PORT" --timeout="${WAIT_TIMEOUT:-30}" --strict -- true # 'true' es el comando dummy

    # Verifica si el wait-for-it falló (si estaba en modo --strict)
    if [ $? -ne 0 ]; then
        echo "❌ Fallo al esperar $HOST_PORT. Saliendo."
        exit 1
    fi
  done
  echo "✅ Todas las dependencias están listas."
fi
# --- FIN DE CORRECCIÓN ---

echo "🚀 Iniciando app.jar..."
exec java ${JAVA_OPTS:-} -jar /app/app.jar