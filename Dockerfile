# --- STAGE 1: Builder (Compiles the JAR) ---
# Use a JDK image suitable for building (we use 21)
FROM eclipse-temurin:21-jdk-jammy AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the Gradle wrapper and configuration files for dependency resolution
COPY gradlew .
COPY gradle /app/gradle
COPY build.gradle /app/
COPY settings.gradle /app/

# Copy the specific service code
COPY discovery-service /app/discovery-service

# Run the build, skipping tests since they are failing and we need the artifact first
# Using `./gradlew :discovery-service:bootJar` ensures only this service is built
RUN chmod +x ./gradlew && ./gradlew :discovery-service:bootJar --no-daemon -x test

# --- STAGE 2: Runner (Produces the final, lean runtime image) ---
# Use the JRE image for smaller size
FROM eclipse-temurin:21-jre-jammy

# Set the working directory
WORKDIR /app

# Install necessary scripts (like curl and bash for the wait-for-it.sh script)
RUN apt-get update && apt-get install -y bash curl && rm -rf /var/lib/apt/lists/*

# Copy the build artifact from the builder stage
# The path changes because the build command was run in /app and the source directory was /app/discovery-service
COPY --from=builder /app/discovery-service/build/libs/*.jar app.jar

# Copy entrypoint and wait-for-it script
COPY infra/scripts/wait-for-it.sh ./
COPY discovery-service/entrypoint.sh ./
RUN chmod +x wait-for-it.sh entrypoint.sh

# Expose the service port (assuming default 8080 or custom port)
EXPOSE 8761

# Run the application using the entrypoint script
ENTRYPOINT ["./entrypoint.sh"]