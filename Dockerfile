# ============================
# 1. Build Stage
# ============================
FROM maven:3.9.6-eclipse-temurin-21 AS build

# Set working directory
WORKDIR /app

# Copy pom.xml and download dependencies (caches better)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the JAR
RUN mvn clean package -DskipTests

# ============================
# 2. Runtime Stage
# ============================
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy JAR from build stage
COPY --from=build /app/target/job-portal-0.0.1-SNAPSHOT.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
