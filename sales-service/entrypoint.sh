#!/bin/sh

# Esperar a la base de datos
/app/wait-for-it.sh db_sales:5432 --timeout=60 --strict -- echo "Base de datos Sales lista"

# Esperar a Config Service
/app/wait-for-it.sh config-service:7777 --timeout=60 --strict -- echo "Config Service listo"

# Arrancar la aplicación
exec java $JAVA_OPTS -jar /app/app.jar
