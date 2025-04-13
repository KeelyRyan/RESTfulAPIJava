# Use official OpenJDK image as base
FROM openjdk:17-jdk-slim

# Set working directory in container
WORKDIR /app

# Copy built jar from local target/ to container
COPY target/orders-0.0.1-SNAPSHOT.jar app.jar

# Expose the port Spring Boot runs on
EXPOSE 8000

# Run the application
ENTRYPOINT ["java", "-jar", "-Dh2.console.webAllowOthers=true", "app.jar"]

RUN mkdir -p /data
