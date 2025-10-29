#!/usr/bin/env bash
set -euo pipefail

# Variables opcionales:
# WAIT_FOR   -> "host:port" (ej. product-db:5432)
# WAIT_TIMEOUT -> segundos (default 30)

if [[ -n "${WAIT_FOR:-}" ]]; then
  echo "🕒 Esperando dependencia: $WAIT_FOR (${WAIT_TIMEOUT:-30}s)..."
  bash /app/wait-for-it.sh "$WAIT_FOR" --timeout="${WAIT_TIMEOUT:-30}" --strict -- echo "✅ Dependencia lista"
fi

echo "🚀 Iniciando app.jar..."
exec java ${JAVA_OPTS:-} -jar /app/app.jar