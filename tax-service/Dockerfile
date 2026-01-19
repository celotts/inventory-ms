ARG DOCKER_USERNAME
ARG DOCKER_PASSWORD
# Solo usamos la etapa de ejecución (Runner)
FROM eclipse-temurin:21-jre-jammy
LABEL maintainer="Inventory MS Team"

# Puerto configurado para Tax Service
EXPOSE 9093

# Crear usuario de sistema para no ejecutar como root
RUN addgroup --system spring && adduser --system --ingroup spring spring

WORKDIR /app

# 1. Copiamos el JAR que compilamos previamente en la Mac
# (La ruta relativa depende de que el 'context' en el yaml sea '..')
COPY tax-service/build/libs/tax-service-*.jar /app/app.jar

# 2. Copiamos los scripts de arranque
COPY tax-service/entrypoint.sh /app/entrypoint.sh
COPY tax-service/wait-for-it.sh /app/wait-for-it.sh

# 3. Corregimos permisos y dueño de los archivos en un solo paso
RUN chmod +x /app/entrypoint.sh /app/wait-for-it.sh && \
    chown spring:spring /app/app.jar /app/entrypoint.sh /app/wait-for-it.sh

# Cambiamos al usuario seguro
USER spring:spring

ENTRYPOINT ["/app/entrypoint.sh"]