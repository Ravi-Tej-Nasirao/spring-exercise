# Use the official OpenJDK 17 image as the base image
FROM openjdk:17-jdk-slim

# Set the working directory inside the container
WORKDIR /app

# Copy the Spring Boot application JAR file into the container
COPY target/company-search-service.jar /app/company-search-service.jar

# Expose port 8080 to the outside world
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "company-search-service.jar"]