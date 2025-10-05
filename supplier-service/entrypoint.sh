#!/usr/bin/env bash
set -euo pipefail

# Defaults razonables para ejecutar dentro de compose
DB_HOST="${SUPPLIER_DB_HOST:-supplier-db}"
DB_PORT="${SUPPLIER_DB_PORT:-5432}"

# (Opcional) espera sólo si HAY base definida
echo "🔎 Comprobando base de datos ${DB_HOST}:${DB_PORT}..."
/app/wait-for-it.sh "${DB_HOST}:${DB_PORT}" -t 60 -- echo "💚 DB lista"

echo "🚀 Iniciando supplier-service..."
# Permite pasar JAVA_OPTS desde .env
exec java ${JAVA_OPTS:-} -jar /app/app.jar