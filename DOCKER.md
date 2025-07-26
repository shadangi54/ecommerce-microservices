# 🐳 Docker Deployment Guide

This guide explains how to deploy the entire E-commerce Microservices platform using Docker and Docker Compose.

## Prerequisites

- [Docker](https://www.docker.com/get-started) (version 20.10+)
- [Docker Compose](https://docs.docker.com/compose/install/) (version 2.0+)

## Quick Start

The easiest way to start all services is using the provided startup script:

### Windows

```bash
.\start-docker-services.bat
```

### Linux/Mac

```bash
chmod +x start-docker-services.sh
./start-docker-services.sh
```

## Manual Deployment

If you prefer to control the deployment process manually:

### 1. Start Infrastructure Services

Start Redis, Zookeeper, and Kafka:

```bash
docker-compose up -d redis zookeeper kafka
```

Wait for these services to be fully initialized (approximately 15-20 seconds).

### 2. Start Core Services

Start the Discovery and Authentication services:

```bash
docker-compose up -d discovery-service auth-service
```

Wait for these services to be fully initialized (approximately 20-30 seconds).

### 3. Start Gateway Service

```bash
docker-compose up -d gateway-service
```

### 4. Start Business Services

```bash
docker-compose up -d product-service order-service inventory-service notification-service
```

## Service URLs

Once all containers are running, you can access the services at:

- **Discovery Dashboard**: http://localhost:8761
- **Auth Service**: http://localhost:8084
- **Gateway**: http://localhost:9090
- **Product Service**: http://localhost:8080 or http://localhost:9090/products
- **Order Service**: http://localhost:8081 or http://localhost:9090/orders
- **Inventory Service**: http://localhost:8082 or http://localhost:9090/inventory
- **Notification Service**: http://localhost:8083
- **Kafka UI**: http://localhost:8090 (Web interface to monitor Kafka topics, consumers, etc.)

## Kafka Configuration

The Kafka setup is based on the Kraft mode (KRaft) configuration, which doesn't require ZooKeeper for operation (though ZooKeeper is still included in this deployment for compatibility).

### Key Kafka Configuration:

- **Kafka Version**: Apache Kafka 3.8.0
- **Port Mapping**:
  - 9092: Main listener port for applications
  - 19092: Inter-broker communication
  - 29092: Controller
- **Topic**: The main notification topic is `shadangi54-notification-topic`
- **UI**: Access Kafka UI at http://localhost:8090 to monitor topics, partitions, and consumer groups

## Monitoring Container Logs

View logs from all services:

```bash
docker-compose logs -f
```

View logs from a specific service:

```bash
docker-compose logs -f [service-name]
# Example: docker-compose logs -f gateway-service
```

## Container Management

### Check Running Containers

```bash
docker-compose ps
```

### Stop All Services

```bash
docker-compose down
```

### Restart a Specific Service

```bash
docker-compose restart [service-name]
# Example: docker-compose restart gateway-service
```

### Rebuild and Restart a Service

```bash
docker-compose up -d --build [service-name]
# Example: docker-compose up -d --build product-service
```

## Testing the Deployment

You can use the included Postman collections to test the deployed services:

1. Import `Ecommerce_Microservices_Complete_Final.postman_collection.json`
2. Import `Ecommerce_Microservices_Environment.postman_environment.json`
3. Set the environment to "Ecommerce Microservices Environment"
4. Update the base URLs in the environment to match your Docker deployment (if needed)
5. Run the requests in the collection

## Troubleshooting

### Kafka Issues

If you encounter issues with Kafka connectivity:

1. Check if Kafka is healthy:

   ```bash
   docker-compose logs kafka
   ```

2. Verify Kafka is listening on the expected ports:

   ```bash
   docker exec ecom-kafka netstat -tuln
   ```

3. Test topic listing:

   ```bash
   docker exec ecom-kafka kafka-topics.sh --bootstrap-server kafka:9092 --list
   ```

4. If consumer services like notification-service can't connect to Kafka, try restarting the Kafka container:

   ```bash
   docker-compose restart kafka
   ```

5. Ensure the topic exists:

   ```bash
   docker exec ecom-kafka kafka-topics.sh --bootstrap-server kafka:9092 --create --if-not-exists --topic shadangi54-notification-topic --partitions 1 --replication-factor 1
   ```

6. For debugging notification issues, check both the order-service and notification-service logs:
   ```bash
   docker-compose logs order-service notification-service
   ```

### Connection Issues

If services can't connect to each other, ensure they're on the same Docker network:

```bash
docker network ls
# You should see "ecommerce-microservices_ecom-network"
```

Check the health of individual services:

```bash
# Example: Check the health of the gateway service
curl http://localhost:9090/actuator/health
```

### Health Check Issues

If services fail with the error "container is unhealthy", it may be due to the health check command:

1. The Alpine-based images don't have `curl` installed by default, but they do have `wget`. The Docker Compose file has been configured to use `wget` for health checks.

2. If you encounter issues with health checks, check the logs for the container:

   ```bash
   docker logs ecom-discovery-service
   ```

3. You can manually test the health endpoint to verify it's working:

   ```bash
   docker exec ecom-discovery-service wget --spider http://localhost:8761/actuator/health
   ```

4. For auth-service specifically, ensure the health check uses the `/login` endpoint instead of `/actuator/health`:

   ```yaml
   healthcheck:
     test:
       [
         "CMD",
         "wget",
         "--no-verbose",
         "--tries=1",
         "--spider",
         "http://localhost:8084/login",
       ]
     interval: 10s
     timeout: 5s
     retries: 3
   ```

5. If necessary, you can install curl in a running container for debugging:
   ```bash
   docker exec ecom-discovery-service apk add --no-cache curl
   ```

```
org.springframework.security.web.UnreachableFilterChainException: A filter chain that matches any request [...] has already been configured
```

The issue is related to conflicting security filter chains in `AuthServerConfig.class` and `ResourceServerConfig.class`. To fix this:

1. Check the Spring Security configuration in the auth-service:

   ```bash
   # Open the source files
   code auth-service/src/main/java/com/shadangi54/auth/config/AuthServerConfig.java
   code auth-service/src/main/java/com/shadangi54/auth/config/ResourceServerConfig.java
   ### Spring Security Configuration Issues
   ```

If the auth-service fails to start with this error:

- In AuthServerConfig.java, add specific matchers:

  ```java
  @Bean
  public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
      return http
          .securityMatcher("/login", "/oauth2/**") // Add specific matchers
          // rest of the configuration
          .build();
  }
  ```

- OR in ResourceServerConfig.java, add specific matchers:
  ```java
  @Bean
  public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
      return http
          .securityMatcher("/api/**") // Add specific matchers
          // rest of the configuration
          .build();
  }
  ```

### Container Issues

If a container isn't starting properly, check its logs:

```bash
docker-compose logs [service-name]
```

## Security Notes

The Docker deployment uses the same security configuration as the local deployment:

- Default admin user: `admin/admin123`
- Default regular user: `testuser/user123`
- OAuth2 client: `web-client/secret`

For production deployments, you should customize these credentials and implement proper secrets management.

## Data Persistence

In this default setup, all services use in-memory H2 databases that will be reset when containers restart. For production use, you should configure persistent database volumes or external database servers.

## Resource Requirements

The complete deployment requires approximately:

- 2GB+ of RAM
- 2+ CPU cores
- 2GB+ of disk space

Adjust your Docker resource settings if you encounter performance issues.
