#!/bin/sh
echo "🚀 Lanzando DISCOVERY-SERVICE..."
exec java -Dspring.profiles.active=docker \
          -jar /app/app.jar
