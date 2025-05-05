#!/usr/bin/env bash
set -euo pipefail

function wait_for_service() {
  local name="$1"
  local host_port="$2"
  local timeout="${3:-30}"

  echo "🕒 Esperando $name en $host_port (timeout ${timeout}s)..."
  bash ./wait-for-it.sh "$host_port" --timeout="$timeout" --strict -- \
    echo "✅ $name está disponible."
}

# === Esperar a PostgreSQL ===
wait_for_service "PostgreSQL" "product-db:5432" 30

# === Lanzar app ===
echo "🚀 Iniciando app.jar..."
exec java $JAVA_OPTS -jar app.jar