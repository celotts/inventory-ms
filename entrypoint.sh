#!/bin/sh
# entrypoint.sh - Lógica de arranque para un microservicio Spring Boot

# Ruta al script de espera (asume que fue copiado al root / del contenedor)
WAIT_FOR_IT=/wait-for-it.sh

# Nombre del JAR (adaptar por servicio: product-service.jar, supplier-service.jar, etc.)
JAR_NAME=$1

# Host y Puerto del Discovery Service (Asume el nombre de servicio de Docker Compose)
DISCOVERY_HOST=discovery-service:8761

echo "Iniciando Entrypoint para $JAR_NAME"

# 1. Esperar al Servidor de Descubrimiento (Crucial para el registro de servicios)
echo "-> Esperando a Discovery Service en $DISCOVERY_HOST..."
# El timeout se establece en 60 segundos
$WAIT_FOR_IT $DISCOVERY_HOST -t 60 -- echo "Discovery Service OK. Procediendo..."

# 2. Si el servicio también usa una base de datos, añádela aquí:
# DB_HOST=product-db:5432
# echo "-> Esperando a la base de datos en $DB_HOST..."
# $WAIT_FOR_IT $DB_HOST -t 60 -- echo "Base de datos OK."


# 3. Lanzar la aplicación principal
# El JAR debe haber sido copiado a /app/ en el Dockerfile.
echo "-> Lanzando $JAR_NAME..."
exec java -jar /app/$JAR_NAME