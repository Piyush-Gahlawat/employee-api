# Containerization Setup - Quick Reference

This document provides a quick overview of the Docker and Kubernetes setup for the Employee API.

## 📋 Files Created

### Docker Files
- **`Dockerfile`** - Multi-stage production Dockerfile (recommended)
- **`Dockerfile.slim`** - Alternative lightweight variant
- **`.dockerignore`** - Excludes unnecessary files from build context
- **`docker-compose.yml`** - Development environment setup
- **`docker-compose.prod.yml`** - Production-ready compose file
- **`.env.example`** - Environment variables template

### Documentation
- **`DOCKER.md`** - Comprehensive Docker documentation
- **`k8s-deployment.yaml`** - Kubernetes manifest with deployment, service, HPA
- **`build-docker.sh`** - Build helper script

## 🚀 Quick Start

### Option 1: Docker Compose (Easiest for Development)

```bash
cd employee-api

# Start the application
docker-compose up --build

# Access the API
curl http://localhost:8080/api/employees

# Stop the application
docker-compose down
```

### Option 2: Docker CLI

```bash
cd employee-api

# Build the image
docker build -t employee-api:latest .

# Run the container
docker run -d -p 8080:8080 --name employee-api employee-api:latest

# View logs
docker logs -f employee-api

# Stop the container
docker stop employee-api
docker rm employee-api
```

### Option 3: Kubernetes

```bash
# Deploy to Kubernetes
kubectl apply -f k8s-deployment.yaml

# Check deployment
kubectl get deployments
kubectl get pods
kubectl get services

# Access the service
kubectl port-forward svc/employee-api 8080:8080

# Clean up
kubectl delete -f k8s-deployment.yaml
```

## 📊 Image Information

### Multi-stage Build Process

```
Stage 1 (Builder):
  - Maven 3.9 + Java 21
  - Downloads dependencies
  - Builds application JAR
  - Size: ~700MB (discarded after build)

Stage 2 (Runtime):
  - Eclipse Temurin JRE 21 Alpine
  - Only contains JAR file
  - Final size: ~200-300MB
  - Non-root user for security
```

### Image Features

✓ Multi-stage build for smaller image  
✓ Alpine Linux base (lightweight)  
✓ Non-root user execution  
✓ Health checks included  
✓ Production-ready  
✓ Supports environment variables  

## 🔧 Configuration

### Environment Variables

Copy `.env.example` to `.env` and customize:

```bash
cp .env.example .env
```

Key variables:
- `SPRING_PROFILE` - Application profile (dev/prod)
- `API_PORT` - API port (default: 8080)
- `JAVA_OPTS` - JVM options
- `LOGGING_LEVEL_ROOT` - Log level

### Docker Compose Override

Create `docker-compose.override.yml` for local overrides:

```yaml
version: '3.8'
services:
  employee-api:
    ports:
      - "9090:8080"  # Different port
    environment:
      - LOGGING_LEVEL_COM_EXAMPLE_EMPLOYEEAPI=DEBUG
```

## 📈 Performance

### JVM Tuning

```bash
# Increase heap size for high load
docker run -e JAVA_OPTS="-Xmx1g -Xms512m" employee-api:latest

# Enable G1GC for large heaps
docker run -e JAVA_OPTS="-XX:+UseG1GC -Xmx2g -Xms1g" employee-api:latest
```

### Resource Limits

```bash
# Limit CPU and memory
docker run --cpus=2 --memory=1g employee-api:latest
```

## 🔍 Troubleshooting

### Build Fails
```bash
# Try clearing Docker cache
docker build --no-cache -t employee-api:latest .

# Check Docker version
docker version

# Check disk space
docker system df
```

### Container Won't Start
```bash
# Check logs
docker logs employee-api

# Verify port is not in use
lsof -i :8080

# Check Docker daemon
docker info
```

### Connection Refused
```bash
# Verify container is running
docker ps | grep employee-api

# Check network connectivity
docker exec employee-api curl http://localhost:8080/actuator/health

# Test port mapping
curl http://localhost:8080/api/employees
```

## 📦 Push to Registry

### Docker Hub

```bash
# Login to Docker Hub
docker login

# Tag image
docker tag employee-api:latest yourusername/employee-api:latest

# Push to registry
docker push yourusername/employee-api:latest

# Use from registry
docker run -p 8080:8080 yourusername/employee-api:latest
```

### Azure Container Registry

```bash
# Login to ACR
az acr login --name myregistry

# Tag image
docker tag employee-api:latest myregistry.azurecr.io/employee-api:latest

# Push to ACR
docker push myregistry.azurecr.io/employee-api:latest

# Deploy to AKS
kubectl set image deployment/employee-api \
  employee-api=myregistry.azurecr.io/employee-api:latest
```

## 🧹 Cleanup

```bash
# Remove container
docker rm employee-api

# Remove image
docker rmi employee-api:latest

# Remove all employee-api images
docker rmi $(docker images -q employee-api)

# Clean up unused resources
docker system prune -a
```

## 📚 Documentation

- **Comprehensive Docker Guide**: See [DOCKER.md](DOCKER.md)
- **Kubernetes Deployment**: See [k8s-deployment.yaml](k8s-deployment.yaml)
- **API Documentation**: See [README.md](README.md)

## 🎯 Next Steps

1. **Local Development**: Use `docker-compose up`
2. **Testing**: Run `mvn test` (outside container)
3. **Build Image**: Run `docker build -t employee-api .`
4. **Push to Registry**: `docker push yourusername/employee-api:latest`
5. **Deploy to K8s**: `kubectl apply -f k8s-deployment.yaml`

## 📞 Support

For detailed information:
- Docker: [docker build --help](https://docs.docker.com/engine/reference/commandline/build/)
- Docker Compose: [docker-compose documentation](https://docs.docker.com/compose/)
- Kubernetes: [kubectl reference](https://kubernetes.io/docs/reference/)

---

**Version**: 1.0  
**Last Updated**: May 20, 2026  
**Status**: Production Ready
