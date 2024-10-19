# Base image for Spring Boot
FROM openjdk:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the JAR file or compiled code from the Maven build
COPY target/*.jar app.jar

# Expose the application port
EXPOSE 8082

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
