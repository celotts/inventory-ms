#!/usr/bin/env bash
set -e

log() {
  echo "[$(date '+%Y-%m-%d %H:%M:%S')] $*"
}

wait_for_service() {
  local name="$1"
  local host="${2%:*}"
  local port="${2#*:}"
  local timeout="${3:-60}"

  log "🕒 Esperando $name en $host:$port (timeout ${timeout}s)..."

  # Usamos timeout y bash nativo para verificar el puerto
  timeout "$timeout" bash -c "until printf '' 2>/dev/null >/dev/tcp/$host/$port; do sleep 2; done"

  if [ $? -eq 0 ]; then
    log "✅ $name está disponible."
  else
    log "❌ Timeout alcanzado para $name. Intentando arrancar de todas formas..."
  fi
}

# === Espera por servicios externos ===
# Solo esperamos al discovery-service
wait_for_service "discovery-service" "discovery-service:8761" 60

# === Validación de archivo JAR ===
if [[ ! -f app.jar ]]; then
  log "❌ Error: app.jar no se encontró en el directorio /app"
  exit 1
fi

# === Lanzar aplicación ===
log "🚀 Iniciando app.jar..."
# Aseguramos que JAVA_OPTS se pase correctamente
exec java $JAVA_OPTS -jar app.jar