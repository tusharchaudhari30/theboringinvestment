#!/bin/bash

set -e

REGISTRY="localhost:6000"

# Build & push Authentication Service
echo "Building Authentication Service..."
docker build -t $REGISTRY/theboringproject-authenticationservice:latest ./authenticationservice
docker push $REGISTRY/theboringproject-authenticationservice:latest

# Build & push Portfolio Service
echo "Building Portfolio Service..."
docker build -t $REGISTRY/theboringproject-portfolioservice:latest ./portfolio-service
docker push $REGISTRY/theboringproject-portfolioservice:latest

# Build & push Transaction Service
echo "Building Transaction Service..."
docker build -t $REGISTRY/theboringproject-transactionservice:latest ./transaction-service
docker push $REGISTRY/theboringproject-transactionservice:latest

# Build & push Frontend (React)
echo "Building Frontend (React App)..."
docker build -t $REGISTRY/theboringproject-frontendpersonalfin:latest ./personalfin-frontend
docker push $REGISTRY/theboringproject-frontendpersonalfin:latest

echo "✅ All images built and pushed to $REGISTRY"
