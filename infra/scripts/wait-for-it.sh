#!/usr/bin/env bash
# Uso: ./wait-for-it.sh host:puerto --timeout=30 --strict -- comando...
set -euo pipefail
HOSTPORT="${1:-}"; shift || true
TIMEOUT=30
STRICT=0

while [[ $# -gt 0 ]]; do
  case "$1" in
    --timeout=*) TIMEOUT="${1#*=}"; shift ;;
    --strict)    STRICT=1; shift ;;
    --) shift; break ;;
    *) break ;;
  esac
done

if [[ -z "$HOSTPORT" ]]; then
  echo "Uso: $0 host:puerto [--timeout=N] [--strict] -- [comando]"; exit 1
fi

HOST="${HOSTPORT%%:*}"
PORT="${HOSTPORT##*:}"

echo "⏳ Esperando $HOST:$PORT (timeout ${TIMEOUT}s)..."
for ((i=0;i<TIMEOUT;i++)); do
  if (echo > /dev/tcp/$HOST/$PORT) >/dev/null 2>&1; then
    echo "✅ $HOST:$PORT disponible"
    exec "$@"
  fi
  sleep 1
done

echo "❌ Timeout esperando $HOST:$PORT"
[[ "$STRICT" -eq 1 ]] && exit 1 || exec "$@"