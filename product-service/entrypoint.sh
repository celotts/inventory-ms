#!/bin/bash
/app/wait-for-it.sh config-service 7777 || echo "⚠️ Config Service no responde, intentando arrancar..."
exec java -Dspring.profiles.active=docker \
          -Dspring.config.import=optional:configserver:http://config-service:7777 \
          -jar /app/app.jar
