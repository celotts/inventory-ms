#!/usr/bin/env bash
# Simple “wait for TCP port” helper
# usage: ./wait-for-it.sh host:port [-t timeout] -- command args...
set -e

HOSTPORT="$1"; shift || true
TIMEOUT=60

# parse -t TIMEOUT opcional
while getopts "t:" opt; do
  case "$opt" in
    t) TIMEOUT="$OPTARG" ;;
  esac
done
shift $((OPTIND -1))

HOST="${HOSTPORT%:*}"
PORT="${HOSTPORT#*:}"

echo "⏳ Esperando a $HOST:$PORT (timeout ${TIMEOUT}s)..."
for i in $(seq 1 "$TIMEOUT"); do
  if (echo >"/dev/tcp/$HOST/$PORT") >/dev/null 2>&1; then
    echo "✅ $HOST:$PORT disponible"
    exec "$@"
    exit 0
  fi
  sleep 1
done

echo "❌ Timeout esperando $HOST:$PORT"
exit 1