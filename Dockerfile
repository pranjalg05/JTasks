# Use official Java 21 base image
FROM eclipse-temurin:21-jdk

# Set working directory
WORKDIR /app

# Copy Maven wrapper and config first
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Fix permissions so ./mvnw can run
RUN chmod +x mvnw

# Pre-download dependencies
RUN ./mvnw dependency:go-offline

# Copy source code
COPY src ./src

# Build the application (skip tests)
RUN ./mvnw clean package -DskipTests

# Expose port (Render sets PORT, so just for clarity)
EXPOSE 8080

# Run the built JAR
CMD ["java", "-jar", "target/JTasks-0.0.1-SNAPSHOT.jar"]
