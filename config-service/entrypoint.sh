#!/bin/sh
echo "🚀 Lanzando CONFIG-SERVICE..."
exec java -Dspring.profiles.active=docker \
          -jar /app/app.jar
