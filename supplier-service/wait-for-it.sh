#!/usr/bin/env bash
# wait-for-it.sh: Esperar por servicios TCP

set -e

hostport="$1"
shift
timeout=60

if [[ "$1" == "--timeout="* ]]; then
    timeout="${1#*=}"
    shift
fi

IFS=':' read -r host port <<< "$hostport"

echo "⏳ Esperando $host:$port (timeout: ${timeout}s)"

for i in $(seq 1 $timeout); do
    if (echo > "/dev/tcp/$host/$port") >/dev/null 2>&1; then
        echo "✅ $host:$port está disponible"
        exec "$@"
        exit 0
    fi
    sleep 1
done

echo "❌ Timeout esperando $host:$port"
exit 1