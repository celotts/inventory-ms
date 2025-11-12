#!/usr/bin/env bash
set -euo pipefail

# ----- helpers -----
log()  { echo -e "$*"; }
fail() { echo "❌ $*" >&2; exit 1; }

wait_for_service() {
  local name="$1" host_port="$2" timeout="${3:-30}"
  log "🕒 Esperando ${name} en ${host_port} (timeout ${timeout}s)..."
  bash /app/wait-for-it.sh "${host_port}" --timeout="${timeout}" --strict -- \
    echo "✅ ${name} está disponible."
}

wait_if_set() {
  # Uso: wait_if_set "Config Server" "${CONFIG_HOST}" "${CONFIG_PORT}" "30"
  local name="$1" host="${2:-}" port="${3:-}" timeout="${4:-30}"
  if [[ -n "${host}" && -n "${port}" ]]; then
    wait_for_service "${name}" "${host}:${port}" "${timeout}"
  fi
}

# ----- variables -----
APP_JAR="${JAR_PATH:-/app/app.jar}"
PROFILE="${SPRING_PROFILES_ACTIVE:-docker}"
JAVA_OPTS="${JAVA_OPTS:-}"
TZ="${TZ:-America/Mexico_City}"

# Defaults JVM si no pasas JAVA_OPTS
if [[ -z "${JAVA_OPTS}" ]]; then
  JAVA_OPTS="\
-XX:+ExitOnOutOfMemoryError \
-XX:+UseG1GC \
-XX:MaxGCPauseMillis=200 \
-XX:InitialRAMPercentage=25.0 \
-XX:MaxRAMPercentage=75.0 \
-Dfile.encoding=UTF-8 \
-Duser.timezone=${TZ} \
-Djava.security.egd=file:/dev/./urandom"
fi

# ----- validaciones -----
[[ -f "${APP_JAR}" ]] || fail "No se encontró el JAR en ${APP_JAR}"

# ----- esperas condicionales (setea estas envs en compose si aplican) -----
# Config Server
wait_if_set "Config Server"   "${CONFIG_HOST:-}"   "${CONFIG_PORT:-}"   "${CONFIG_TIMEOUT:-30}"
# Eureka
wait_if_set "Discovery (Eureka)" "${EUREKA_HOST:-}"   "${EUREKA_PORT:-}"   "${EUREKA_TIMEOUT:-30}"
# Base de datos
wait_if_set "Database"        "${DB_HOST:-}"       "${DB_PORT:-5432}"    "${DB_TIMEOUT:-60}"
# Cualquier otro backend que necesites
# wait_if_set "Kafka"           "${KAFKA_HOST:-}"     "${KAFKA_PORT:-9092}"  "30"

# ----- lanzar app -----
log "🚀 Iniciando app: ${APP_JAR}"
log "🧩 Spring profile: ${PROFILE}"
exec java ${JAVA_OPTS} -jar "${APP_JAR}" --spring.profiles.active="${PROFILE}"