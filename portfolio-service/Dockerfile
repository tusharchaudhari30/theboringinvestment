# ---- Stage 1: Build the application using Amazon Corretto 21 ----
    FROM eclipse-temurin:21.0.6_7-jdk-alpine-3.21 AS build
    WORKDIR /app
    
    # Copy the project files
    COPY . .
    
    # Build the Spring Boot application (JAR)
    RUN ./gradlew build
    
    # ---- Stage 2: Run the application using Amazon Corretto 21 ----
    FROM eclipse-temurin:21.0.6_7-jre-alpine AS runtime
    WORKDIR /app
    
    # Copy the JAR file from the build stage
    COPY --from=build /app/build/libs/*.jar app.jar
    
    # Expose the default Spring Boot port
    EXPOSE 8082
    
    # Run the Spring Boot application
    ENTRYPOINT ["java", "-jar", "app.jar"]
    