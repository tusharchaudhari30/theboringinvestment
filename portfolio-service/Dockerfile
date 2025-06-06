# ---- Stage 1: Build the application using Amazon Corretto 21 ----
    FROM eclipse-temurin:21.0.6_7-jdk-alpine-3.21 AS build
    WORKDIR /app
    
    # Copy Gradle wrapper and build config first to cache dependencies
    COPY gradlew .
    COPY gradle gradle
    COPY build.gradle .
    COPY settings.gradle .
    
    # Pre-download dependencies (only runs again if build files change)
    RUN ./gradlew dependencies
    
    # Copy the rest of the project
    COPY . .
    
    # Build the Spring Boot application
    RUN ./gradlew build --no-daemon
    
    # ---- Stage 2: Run the application using Amazon Corretto 21 ----
    FROM eclipse-temurin:21.0.6_7-jre-alpine AS runtime
    WORKDIR /app
    
    # Copy the built JAR from the build stage
    COPY --from=build /app/build/libs/*.jar app.jar
    
    # Expose the default Spring Boot port
    EXPOSE 8082
    
    # Run the Spring Boot application
    ENTRYPOINT ["java", "-jar", "app.jar"]
    