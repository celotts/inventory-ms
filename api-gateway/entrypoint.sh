#!/bin/bash

echo "🚀 Starting API Gateway..."

# Esperar a servicios críticos
echo "⏳ Waiting for Discovery Service..."
./wait-for-it.sh discovery-service:8761 --timeout=60 --strict -- echo "✅ Discovery Service is ready"

echo "⏳ Waiting for Config Service..."
./wait-for-it.sh config-service:7777 --timeout=60 --strict -- echo "✅ Config Service is ready"

# Pequeña pausa adicional
echo "⏳ Waiting additional 5 seconds for services to be fully ready..."
sleep 5

# Iniciar API Gateway
echo "🌐 Starting API Gateway on port 8090..."
exec java $JAVA_OPTS -jar app.jar
