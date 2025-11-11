#!/usr/bin/env bash
# Minimal "wait-for-it" compatible: host:port [--timeout=SECONDS] [--strict] [-- <cmd>]
set -euo pipefail

usage() { echo "Uso: $0 host:port [--timeout=SECONDS] [--strict] [-- <cmd>]"; }

HOST_PORT="${1:-}"; shift || true
[[ -z "${HOST_PORT}" || "${HOST_PORT}" != *:* ]] && usage && exit 1
HOST="${HOST_PORT%:*}"
PORT="${HOST_PORT##*:}"

TIMEOUT=30
STRICT=false
CMD=()

# parse flags
while [[ $# -gt 0 ]]; do
  case "$1" in
    --timeout=*) TIMEOUT="${1#*=}"; shift;;
    --strict)    STRICT=true; shift;;
    --)          shift; CMD=("$@"); break;;
    *)           echo "Flag desconocido: $1"; usage; exit 1;;
  esac
done

end=$(( $(date +%s) + TIMEOUT ))
while :; do
  if (echo >"/dev/tcp/${HOST}/${PORT}") >/dev/null 2>&1; then
    echo "OK: ${HOST}:${PORT} abierto."
    break
  fi
  if [[ $(date +%s) -ge $end ]]; then
    echo "ERROR: timeout esperando ${HOST}:${PORT} (${TIMEOUT}s)." >&2
    $STRICT && exit 1 || break
  fi
  sleep 1
done

if [[ ${#CMD[@]} -gt 0 ]]; then
  exec "${CMD[@]}"
fi