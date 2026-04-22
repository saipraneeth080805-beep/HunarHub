# ================================
# Stage 1 - Build the JAR
# ================================
FROM maven:3.9.5-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml first (Docker caches this layer)
# If pom.xml hasn't changed, Maven dependencies won't re-download
COPY pom.xml .

# Download all dependencies (cached separately from source code)
RUN mvn dependency:go-offline -B

# Copy source code
COPY src ./src

# Build the JAR, skip tests for faster build
RUN mvn clean package -DskipTests

# ================================
# Stage 2 - Run the JAR
# ================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Create uploads directory (for file uploads feature)
RUN mkdir -p /app/uploads

# Copy the built JAR from Stage 1
COPY --from=builder /app/target/hunarhub-1.0.0.jar app.jar

# Expose port 8080
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
