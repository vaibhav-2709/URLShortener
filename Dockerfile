# Use official OpenJDK image
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy jar file
COPY target/urlshortener-0.0.1-SNAPSHOT.jar app.jar

# Expose port
EXPOSE 8080

# Run app
ENTRYPOINT ["java","-jar","app.jar"]