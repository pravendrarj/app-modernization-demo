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

## Step 4: Externalize Hard-Coded Configuration

```
@modernize Add Spring Boot Actuator health endpoints and externalize configuration for cloud deployment
```

Expected changes:
- Add `spring-boot-starter-actuator` dependency
- Configure health/readiness/liveness endpoints
- Externalize hardcoded values (SMTP config, DB URL) to environment variables
- Add Spring profiles for `dev`, `staging`, `prod`

Then tackle the two files where configuration is compiled in as constants:

```
@modernize Externalize every hard-coded value in HardcodedConfiguration.java and ApplicationDeploymentConfig.java into environment variables and Spring profiles
```

| Currently hard-coded | Should come from |
|---------------------|------------------|
| Server port, timeouts, thread-pool sizes | `application-{profile}.yml` |
| File/log/temp paths (`C:\legacy-app\files\`, `/var/...`) | Blob Storage + stdout logging |
| Payment and email service URLs | Environment variables |
| Feature flags | Config service / Azure App Configuration |
| API keys, certificate paths, keystore | Azure Key Vault |
| Region (`us-east-1`) | Platform-provided metadata |

Also fix the two deployment-blocking gaps in `ApplicationDeploymentConfig.java`:

```
@modernize Replace the System.exit(0) shutdown with graceful shutdown, and add Prometheus metrics and distributed tracing
```

## Step 5: Make the Application Stateless

Containers scale horizontally, which means any replica can serve any request. `LegacyAPIController.java`
assumes otherwise:

```
@modernize Refactor LegacyAPIController to be stateless so it can run with multiple replicas
```

| Problem | Effect with 3 replicas | Fix |
|---------|----------------------|-----|
| `static int requestCounter` | Each pod reports a different count | Micrometer counter |
| `Map requestCache` holding the logged-in user | Login on pod A, "not logged in" on pod B | Spring Session + Redis |
| Writes exports to `C:\exports\` in the handler | Path doesn't exist; blocks the request thread | Blob Storage, async |
| No timeout/retry on the outbound call | One hung dependency exhausts the thread pool | Resilience4j |

## Step 6: Migrate the Database Layer to Azure

This is usually the longest pole in a real migration. `LegacyDatabaseConnection.java` is the target:

```
@modernize Make LegacyDatabaseConnection.java cloud-ready for Azure Database for MySQL
```

Expected remediations:

| Issue | Remediation |
|-------|-------------|
| Hard-coded JDBC URL and credentials | Env vars / Key Vault, then Managed Identity |
| **No connection pooling** (new connection per query) | HikariCP — Spring Boot's default pool |
| `Class.forName("com.mysql.jdbc.Driver")` | Delete — obsolete since JDBC 4.0 |
| No retry logic | Retry with exponential backoff (Azure SQL throttles transiently) |
| `Statement.execute(userSql)` | `PreparedStatement` — also closes a SQL-injection hole |
| Stored procedure + hard-coded batch size | Portable query or ORM, externalized batch size |
| Backups to `C:\legacy-backups\db\` | Azure Backup / Blob Storage |

Ask for the pooling configuration specifically, since it's what makes the app survive load:

```
@modernize Show me the HikariCP DataSource configuration to replace the manual DriverManager connections
```

## Step 7: Generate Azure-Specific Deployment

```
@modernize-deployment Prepare this application for Azure Container Apps deployment
```

This generates:
- Azure Container Apps configuration
- Bicep/ARM templates for infrastructure
- CI/CD pipeline configuration (GitHub Actions)

## Step 8: Get a Containerization Plan

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
| `@modernize Refactor [controller] to be stateless` | Enable horizontal scaling |
| `@modernize Make [dao] cloud-ready for Azure Database for MySQL` | Database migration |

---

**Reference:** [Seeded Issue Catalog](APPENDIX-Seeded-Issue-Catalog.md) — cloud readiness and SQL patterns

---

**Next:** [Lab 6 - End-to-End Modernization with @modernize](LAB6-End-to-End-Modernization.md)
