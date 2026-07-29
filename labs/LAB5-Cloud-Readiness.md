# Lab 5: Cloud Readiness & Containerization

## Objective
Use `@modernize-deployment` to make the application cloud-ready — generate Dockerfiles, Kubernetes manifests, and prepare for Azure deployment.

---

## Step 1: Generate a Dockerfile

Open Copilot Chat and type:

```
@modernize-deployment Generate a Dockerfile for this Java application
```

The agent will:
- Detect the app's runtime requirements
- Generate a multi-stage Dockerfile
- Use an appropriate base image (Eclipse Temurin JDK 17)
- Configure the JAR execution

## Step 2: Review the Dockerfile

The generated Dockerfile should include:
- Multi-stage build (builder + runtime)
- Maven build in the first stage
- Minimal runtime image in the second stage
- Proper health check configuration
- Non-root user for security

## Step 3: Generate Kubernetes Manifests

```
@modernize-deployment Generate Kubernetes deployment manifests for Azure Kubernetes Service (AKS)
```

This generates:
- `deployment.yaml` — Pod spec with resource limits
- `service.yaml` — ClusterIP/LoadBalancer service
- `configmap.yaml` — Externalized configuration
- Health check probes (liveness/readiness)

## Step 4: Add Cloud-Native Configuration

```
@modernize Add Spring Boot Actuator health endpoints and externalize configuration for cloud deployment
```

Expected changes:
- Add `spring-boot-starter-actuator` dependency
- Configure health/readiness/liveness endpoints
- Externalize hardcoded values (SMTP config, DB URL) to environment variables
- Add Spring profiles for `dev`, `staging`, `prod`

## Step 5: Generate Azure-Specific Deployment

```
@modernize-deployment Prepare this application for Azure Container Apps deployment
```

This generates:
- Azure Container Apps configuration
- Bicep/ARM templates for infrastructure
- CI/CD pipeline configuration (GitHub Actions)

## Step 6: Get a Containerization Plan

```
@modernize Generate a complete containerization plan for this application
```

The plan will address:
- Converting WAR → JAR packaging
- Removing embedded Tomcat dependency hacks
- Externalizing all configuration
- Adding proper logging (stdout/stderr for containers)
- Database connection pooling for cloud (HikariCP)

---

## Key Commands Used

| Command | Purpose |
|---------|---------|
| `@modernize-deployment Generate Dockerfile` | Create container config |
| `@modernize-deployment Generate K8s manifests` | Kubernetes deployment |
| `@modernize-deployment Prepare for Azure Container Apps` | Azure-specific setup |
| `@modernize Add actuator and externalize config` | Cloud-native patterns |

---

**Next:** [Lab 6 - End-to-End Modernization with @modernize](LAB6-End-to-End-Modernization.md)
