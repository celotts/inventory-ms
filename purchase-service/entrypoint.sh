#!/usr/bin/env bash
set -euo pipefail

log() {
  echo "[$(date '+%Y-%m-%d %H:%M:%S')] $*"
}
DB_USER=${PRODUCT_DB_USERNAME}  # <-- Usar la variable de entorno
DB_PASS=${PRODUCT_DB_PASSWORD}  # <-- Usar la variable de entorno
wait_for_service() {
  local name="$1"
  local host_port="$2"
  local timeout="${3:-30}"

  log "🕒 Esperando $name en $host_port (timeout ${timeout}s)..."
  bash ./wait-for-it.sh "$host_port" --timeout="$timeout" --strict --
  log "✅ $name está disponible."
}


# === Espera por servicios externos ===
wait_for_service "discovery-service" "discovery-service:8761" 60

# === Validación de archivo JAR ===
if [[ ! -f app.jar ]]; then
  log "❌ Error: app.jar no se encontró en el directorio /app"
  exit 1
fi

# === Lanzar aplicación ===
log "🚀 Iniciando app.jar..."
exec java ${JAVA_OPTS:-} -jar app.jar