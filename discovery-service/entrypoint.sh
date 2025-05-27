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

# === Lanzar app ===
echo "🚀 Iniciando app.jar..."
exec java -jar app.jar