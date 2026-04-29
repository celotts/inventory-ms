#!/bin/bash
/app/wait-for-it.sh config-service 7777 || echo "⚠️ Config Service no responde, intentando arrancar..."
exec java $JAVA_OPTS -jar /app/app.jar
