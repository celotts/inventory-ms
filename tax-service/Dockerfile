# ---- stage 1: traer curl binario sin apt ----
FROM curlimages/curl:8.10.1 AS curlbin

# ---- stage 2: runtime Java con bash (Ubuntu/Jammy) ----
FROM eclipse-temurin:21-jre-jammy

# Locale básico (opcional, útil para logs)
ENV LANG=C.UTF-8 LC_ALL=C.UTF-8

WORKDIR /app

# Copiamos curl y certificados al runtime (sin apt)
COPY --from=curlbin /usr/bin/curl /usr/bin/curl
COPY --from=curlbin /etc/ssl/certs/ca-certificates.crt /etc/ssl/certs/ca-certificates.crt

# Copiamos app y scripts
COPY build/libs/*.jar /app/app.jar
COPY entrypoint.sh wait-for-it.sh /app/
RUN chmod +x /app/entrypoint.sh /app/wait-for-it.sh

EXPOSE 9092
ENTRYPOINT ["/app/entrypoint.sh"]