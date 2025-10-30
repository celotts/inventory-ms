#!/usr/bin/env bash
set -euo pipefail

WAIT_FOR="${WAIT_FOR:-}"
WAIT_TIMEOUT="${WAIT_TIMEOUT:-30}"
JAVA_OPTS="${JAVA_OPTS:-}"

wait_for_service() {
  local name="$1"
  local host_port="$2"
  local timeout="${3:-30}"

  echo "🕒 Esperando $name en $host_port (timeout ${timeout}s)..."
  ./wait-for-it.sh "$host_port" --timeout="$timeout" --strict -- \
    echo "✅ $name disponible."
}

if [[ -n "$WAIT_FOR" ]]; then
  IFS=',' read -ra targets <<< "$WAIT_FOR"
  for t in "${targets[@]}"; do
    if [[ "$t" == *"@"* ]]; then
      name="${t%@*}"
      host_port="${t#*@}"
    else
      name="$t"
      host_port="$t"
    fi
    wait_for_service "$name" "$host_port" "$WAIT_TIMEOUT"
  done
else
  echo "ℹ️  No hay dependencias (WAIT_FOR vacío)."
fi

echo "🚀 Iniciando app.jar..."
exec java $JAVA_OPTS -jar app.jar