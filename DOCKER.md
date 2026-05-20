# Docker Setup for Employee API

This document provides instructions for building and running the Employee API in Docker.

## 📋 Prerequisites

- Docker 20.10+
- Docker Compose 2.0+ (for docker-compose.yml)
- Git

## 🐳 Docker Image Details

- **Base Image**: Eclipse Temurin 21 JRE Alpine (lightweight, ~200MB)
- **Build Image**: Maven 3.9 with Java 21
- **Multi-stage Build**: Reduces final image size by ~60%
- **Security**: Runs as non-root user
- **Health Check**: Integrated with Spring Boot Actuator

## 🚀 Quick Start

### Option 1: Using Docker Compose (Recommended for Development)

```bash
# Build and run the container
docker-compose up --build

# Stop the container
docker-compose down

# View logs
docker-compose logs -f employee-api

# Rebuild without cache
docker-compose up --build --no-cache
```

### Option 2: Using Docker CLI

#### Build the Image

```bash
# Build with default tag
docker build -t employee-api:latest .

# Build with custom tag
docker build -t employee-api:1.0.0 .

# Build with BuildKit (faster, better caching)
docker buildx build -t employee-api:latest .
```

#### Run the Container

```bash
# Run with default settings
docker run -p 8080:8080 employee-api:latest

# Run with environment variables
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JAVA_OPTS="-Xmx1g -Xms512m" \
  employee-api:latest

# Run in detached mode with a name
docker run -d --name employee-api \
  -p 8080:8080 \
  employee-api:latest

# Run with custom port mapping
docker run -d --name employee-api \
  -p 9090:8080 \
  employee-api:latest
```

#### Access the Application

```bash
# Check if container is running
docker ps

# View container logs
docker logs employee-api

# Follow logs in real-time
docker logs -f employee-api

# Access the API
curl http://localhost:8080/api/employees
```

## 🛠️ Image Management

### List Images

```bash
docker images | grep employee-api
```

### Remove Images

```bash
# Remove specific image
docker rmi employee-api:latest

# Remove all employee-api images
docker rmi $(docker images -q employee-api)

# Remove unused images
docker image prune
```

## 📊 Container Management

### View Container Details

```bash
# Inspect container
docker inspect employee-api

# View resource usage
docker stats employee-api

# View health status
docker inspect --format='{{.State.Health.Status}}' employee-api
```

### Stop/Start Container

```bash
# Stop the container
docker stop employee-api

# Start the container
docker start employee-api

# Restart the container
docker restart employee-api

# Kill the container
docker kill employee-api
```

### Execute Commands in Container

```bash
# Interactive bash shell
docker exec -it employee-api /bin/sh

# View files in container
docker exec employee-api ls -la /app

# Check Java version in container
docker exec employee-api java -version
```

## 🌐 Networking

### Port Mapping

```bash
# Map to different host port
docker run -p 9000:8080 employee-api:latest

# Expose multiple ports
docker run -p 8080:8080 -p 8081:8080 employee-api:latest
```

### Docker Network

```bash
# Create a custom network
docker network create employee-network

# Run container on custom network
docker run --network employee-network \
  --name employee-api \
  -p 8080:8080 \
  employee-api:latest
```

## 🔐 Security Best Practices

1. **Non-root User**: Container runs as `appuser` (UID 100)
2. **Alpine Base**: Minimal base image reduces attack surface
3. **Health Checks**: Monitor container health
4. **Environment Variables**: Use for sensitive data

```bash
# Run with read-only filesystem
docker run --read-only \
  --tmpfs /tmp \
  -p 8080:8080 \
  employee-api:latest

# Set memory limits
docker run -m 1g --memory-swap 1g \
  -p 8080:8080 \
  employee-api:latest
```

## 📈 Performance Optimization

### JVM Tuning

```bash
# Set heap size
docker run -e JAVA_OPTS="-Xmx1g -Xms512m" \
  -p 8080:8080 \
  employee-api:latest

# Enable G1GC for large heaps
docker run -e JAVA_OPTS="-XX:+UseG1GC -Xmx1g -Xms512m" \
  -p 8080:8080 \
  employee-api:latest
```

### Resource Limits

```bash
# Limit CPU and memory
docker run \
  --cpus=2 \
  --memory=1g \
  -p 8080:8080 \
  employee-api:latest
```

## 🐛 Troubleshooting

### Container Won't Start

```bash
# Check logs
docker logs employee-api

# Check for port conflicts
docker ps -a | grep employee-api

# Check health status
docker inspect --format='{{.State.Health}}' employee-api
```

### High Memory Usage

```bash
# Check memory usage
docker stats employee-api

# Reduce heap size
docker run -e JAVA_OPTS="-Xmx512m" \
  -p 8080:8080 \
  employee-api:latest
```

### API Not Responding

```bash
# Test connectivity
docker exec employee-api curl http://localhost:8080/actuator/health

# Check application logs
docker logs employee-api | tail -50
```

## 📦 Volume Mounting

### Mount Local Directory

```bash
# Mount configuration
docker run -v $(pwd)/config:/app/config \
  -p 8080:8080 \
  employee-api:latest

# Mount logs
docker run -v $(pwd)/logs:/app/logs \
  -p 8080:8080 \
  employee-api:latest
```

## 🏗️ Production Deployment

### Build for Production

```bash
# Build with BuildKit
DOCKER_BUILDKIT=1 docker build -t employee-api:prod .

# Tag with registry
docker tag employee-api:prod myregistry.azurecr.io/employee-api:prod

# Push to registry
docker push myregistry.azurecr.io/employee-api:prod
```

### Run in Production

```bash
docker run -d \
  --name employee-api \
  --restart unless-stopped \
  --log-driver json-file \
  --log-opt max-size=10m \
  --log-opt max-file=3 \
  --cpus=2 \
  --memory=1g \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e JAVA_OPTS="-Xmx1g -Xms512m -XX:+UseG1GC" \
  -p 8080:8080 \
  employee-api:prod
```

## 📝 Dockerfile Breakdown

### Stage 1: Builder

```dockerfile
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src src
RUN mvn clean package -DskipTests
```

- Uses Maven with Java 21
- Downloads dependencies first (better caching)
- Builds application JAR

### Stage 2: Runtime

```dockerfile
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/target/employee-api-0.0.1-SNAPSHOT.jar app.jar
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser
EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --start-period=10s --retries=3 \
  CMD curl -f http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
```

- Lightweight Alpine Linux base
- Copies JAR from builder stage
- Creates non-root user
- Includes health check
- Exposes port 8080

## 🔍 Image Size Analysis

```bash
# View image size
docker images employee-api

# Analyze layers
docker history employee-api:latest

# Get detailed size info
docker inspect employee-api:latest | grep -A5 Size
```

Typical sizes:
- Builder stage: ~700MB (not in final image)
- Final image: ~200-300MB

## 📚 Additional Resources

- [Docker Documentation](https://docs.docker.com/)
- [Spring Boot Docker Guide](https://spring.io/guides/gs/spring-boot-docker/)
- [Eclipse Temurin Docker Images](https://hub.docker.com/_/eclipse-temurin)

## 🤝 Common Commands Reference

| Command | Description |
|---------|-------------|
| `docker-compose up --build` | Build and start containers |
| `docker-compose down` | Stop and remove containers |
| `docker build -t employee-api .` | Build image |
| `docker run -p 8080:8080 employee-api` | Run container |
| `docker logs employee-api` | View logs |
| `docker ps` | List running containers |
| `docker images` | List images |
| `docker stop employee-api` | Stop container |
| `docker rm employee-api` | Remove container |
| `docker rmi employee-api` | Remove image |
