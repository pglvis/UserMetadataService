# Start from an official OpenJDK image
FROM eclipse-temurin:17-jdk-alpine

# Set a working directory inside the container
WORKDIR /app

# Copy build output (jar) into the container
COPY build/libs/UserMetadata-0.0.1-SNAPSHOT.jar app.jar

# Expose the port your Spring Boot app runs on
EXPOSE 8282

# Run the Spring Boot app
ENTRYPOINT ["java","-jar","app.jar"]