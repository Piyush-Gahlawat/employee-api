#!/bin/bash

# Docker Build Script for Employee API
# This script helps build and manage the Docker image

set -e

IMAGE_NAME="${1:-employee-api}"
VERSION="${2:-latest}"
TAG="${IMAGE_NAME}:${VERSION}"
DOCKERFILE="${3:-Dockerfile}"

echo "================================"
echo "Employee API - Docker Build"
echo "================================"
echo "Image: $TAG"
echo "Dockerfile: $DOCKERFILE"
echo ""

# Check if Docker is running
if ! docker ps > /dev/null 2>&1; then
    echo "❌ Docker is not running. Please start Docker and try again."
    exit 1
fi

echo "✓ Docker is running"
echo ""

# Build the image
echo "📦 Building Docker image..."
if docker build -f "$DOCKERFILE" -t "$TAG" .; then
    echo "✓ Image built successfully: $TAG"
    echo ""
    
    # Get image size
    SIZE=$(docker images "$IMAGE_NAME" --format "{{.Size}}")
    echo "📊 Image size: $SIZE"
    echo ""
    
    # Show image details
    echo "📋 Image details:"
    docker images "$IMAGE_NAME:$VERSION" --format "table {{.Repository}}\t{{.Tag}}\t{{.Size}}\t{{.CreatedAt}}"
    echo ""
    
    # Optionally run the image
    echo "To run the container, use:"
    echo ""
    echo "  Option 1: Using Docker CLI"
    echo "  $ docker run -p 8080:8080 $TAG"
    echo ""
    echo "  Option 2: Using Docker Compose"
    echo "  $ docker-compose up"
    echo ""
else
    echo "❌ Build failed"
    exit 1
fi
