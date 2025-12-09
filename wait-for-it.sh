#!/bin/bash
# wait-for-it.sh

# Uso: wait-for-it.sh host:port [-t timeout] [-- command args]

# Valores por defecto
TIMEOUT=30
QUIET=0
HOST=
PORT=
COMMAND=

# Función para imprimir mensajes
echoerr() {
  if [ "$QUIET" -ne 1 ]; then
    echo "$@" 1>&2
  fi
}

# Función de uso
usage() {
  cat << USAGE >&2
Usage:
  $cmdname host:port [-t timeout] [-- command args]
  -t TIMEOUT | --timeout=TIMEOUT  Tiempo máximo de espera en segundos.
  -q | --quiet                    No imprime la salida de estado.
  -- COMMAND ARGS                 Comando a ejecutar después de la espera.
USAGE
  exit 1
}

# Parsear argumentos
while [ "$#" -gt 0 ]; do
  case "$1" in
    *:* )
      HOST=$(printf "%s\n" "$1" | cut -d : -f 1)
      PORT=$(printf "%s\n" "$1" | cut -d : -f 2)
      shift 1
      ;;
    -q | --quiet)
      QUIET=1
      shift 1
      ;;
    -t)
      TIMEOUT="$2"
      if [ "$TIMEOUT" = "" ]; then break; fi
      shift 2
      ;;
    --timeout=*)
      TIMEOUT="${1#*=}"
      shift 1
      ;;
    --)
      shift
      COMMAND="$@"
      break
      ;;
    -*)
      echoerr "Argumento desconocido: $1"
      usage
      ;;
    *)
      COMMAND="$@"
      break
      ;;
  esac
done

# Validaciones
if [ "$HOST" = "" ] || [ "$PORT" = "" ]; then
  echoerr "Error: Debes especificar el host y puerto."
  usage
fi

# Ciclo de espera
start_ts=$(date +%s)
echoerr "Esperando a $HOST:$PORT..."

while :
do
  (echo > /dev/tcp/$HOST/$PORT) >/dev/null 2>&1
  result=$?
  if [ $result -eq 0 ]; then
    end_ts=$(date +%s)
    echoerr "$HOST:$PORT está disponible después de $((end_ts - start_ts)) segundos."
    break
  fi
  current_ts=$(date +%s)
  if [ $((current_ts - start_ts)) -gt "$TIMEOUT" ]; then
    echoerr "Error: Se excedió el tiempo de espera de $TIMEOUT segundos para $HOST:$PORT."
    exit 1
  fi
  sleep 1
done

# Ejecutar comando si se especificó
if [ "$COMMAND" != "" ]; then
  exec $COMMAND
fi