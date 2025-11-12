# ---- build stage ----
FROM gradle:8.10.2-jdk21 AS build
WORKDIR /workspace
COPY gradlew settings.gradle build.gradle ./
COPY gradle ./gradle
COPY tax-service ./tax-service
RUN ./gradlew --no-daemon :tax-service:bootJar

# ---- runtime ----
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY tax-service/wait-for-it.sh tax-service/entrypoint.sh ./
RUN chmod +x wait-for-it.sh entrypoint.sh
COPY --from=build /workspace/tax-service/build/libs/*.jar app.jar
EXPOSE 9092
ENTRYPOINT ["./entrypoint.sh"]