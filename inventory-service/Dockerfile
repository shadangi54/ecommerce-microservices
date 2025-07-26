FROM eclipse-temurin:17-jdk-alpine as build
WORKDIR /workspace/app

# Copy maven executable and pom.xml
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .

# Make the mvnw script executable
RUN chmod +x mvnw

# Build all dependencies (will be cached if no changes to pom.xml)
RUN ./mvnw dependency:go-offline -B

# Copy the source code
COPY src src

# Build the application
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Production stage
FROM eclipse-temurin:17-jre-alpine
VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency

# Copy the dependency application layer by layer
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app

# Set the entrypoint
ENTRYPOINT ["java","-cp","app:app/lib/*","com.shadangi54.inventory.InventoryServiceApplication"]

# Expose the service port
EXPOSE 8082

# Health check
HEALTHCHECK --interval=30s --timeout=3s \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8082/actuator/health || exit 1
