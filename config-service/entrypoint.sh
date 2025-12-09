#!/bin/bash
# Script de entrada para el config-service.
# Ejecuta 'wait-for-it.sh' para esperar por el Discovery Service (Eureka).

# ------------------------------------------------------------------------------------------------
# 1. ESPERAR DEPENDENCIA CRÍTICA (DISCOVERY SERVICE)
# ------------------------------------------------------------------------------------------------
# Asumimos que la URL de Eureka es 'discovery-service:8761', que es el nombre del servicio
# y puerto predeterminados en Docker Compose.
WAIT_HOST="discovery-service:8761"
WAIT_TIMEOUT=45

echo "Esperando a que Discovery Service (Eureka) esté disponible en $WAIT_HOST (Timeout: $WAIT_TIMEOUTs)..."

# Sintaxis: ./wait-for-it.sh <host>:<port> -t <timeout> -- <comando a ejecutar después>
# La ruta a wait-for-it.sh es /app/wait-for-it.sh, según la configuración de tu Dockerfile
/app/wait-for-it.sh $WAIT_HOST -t $WAIT_TIMEOUT -- \
    echo "Discovery Service listo. Procediendo a iniciar el Config Server..."

# Comprobación del resultado de wait-for-it.sh
if [ $? -ne 0 ]; then
    echo "ERROR: El tiempo de espera para $WAIT_HOST ha expirado. El Config Server no se iniciará."
    exit 1
fi

# ------------------------------------------------------------------------------------------------
# 2. INICIAR LA APLICACIÓN SPRING BOOT
# ------------------------------------------------------------------------------------------------
# Ejecuta el JAR principal de Spring Boot. El 'exec' asegura que el proceso Java reemplace
# el proceso del script shell como PID 1.

echo "Iniciando Config Server: java -jar /app/app.jar"
exec java -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar