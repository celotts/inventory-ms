#!/usr/bin/env bash
set -euo pipefail

# ── Configuración por variables de entorno ────────────────────────────────
# Lista separada por comas de hosts:puerto a esperar, p.ej.:
# WAIT_FOR="discovery-service:8761,config-service:7777,tax-db:5432"
WAIT_FOR="${WAIT_FOR:-}"
WAIT_TIMEOUT="${WAIT_TIMEOUT:-30}"         # timeout por servicio
JAVA_OPTS="${JAVA_OPTS:-}"                 # pasa flags de JVM si quieres

wait_for_service() {
  local name="$1"
  local host_port="$2"
  local timeout="${3:-30}"

  echo "🕒 Esperando $name en $host_port (timeout ${timeout}s)..."
  bash ./wait-for-it.sh "$host_port" --timeout="$timeout" --strict -- \
    echo "✅ $name disponible."
}

# ── Esperar dependencias declaradas ───────────────────────────────────────
if [[ -n "$WAIT_FOR" ]]; then
  IFS=',' read -ra targets <<< "$WAIT_FOR"
  for t in "${targets[@]}"; do
    # t puede ser "alias@host:puerto" o "host:puerto"
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

# ── Lanzar app ────────────────────────────────────────────────────────────
echo "🚀 Iniciando app.jar..."
exec java $JAVA_OPTS -jar app.jar