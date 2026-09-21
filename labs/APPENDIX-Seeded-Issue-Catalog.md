# Appendix: Seeded Issue Catalog

This app is an **intentionally broken** legacy Spring Boot 1.5 / Java 8 application used for the
modernization labs. Beyond the outdated `pom.xml`, seven source files were added that deliberately
seed **Java upgrade** and **cloud readiness** issues so the assessment labs have enough material
to work with.

> **Note on counts:** the numbers below are the seeded *code patterns*. How many findings an
> assessment tool actually reports depends on its rule set — it may group several patterns into a
> single finding, or split one into several. Treat these as a map of where to look, not a target score.

---

## Quick Map

| File | Focus | Patterns |
|------|-------|----------|
| `util/LegacyStringUtils.java` | Java upgrade | 5 |
| `util/LegacyDateTimeUtils.java` | Java upgrade + cloud | 5 + 1 |
| `service/LegacyReflectionService.java` | Java upgrade | 7 |
| `config/HardcodedConfiguration.java` | Cloud readiness | 12 |
| `config/ApplicationDeploymentConfig.java` | Cloud readiness | 16 |
| `controller/LegacyAPIController.java` | Cloud readiness | 9 |
| `dao/LegacyDatabaseConnection.java` | **Cloud readiness — SQL** | 11 |

All paths are relative to `src/main/java/com/legacy/inventory/`.

---

## Java Upgrade Issues

### `util/LegacyStringUtils.java`
| Pattern | Modern replacement |
|---------|-------------------|
| `StringBuffer` for single-threaded concatenation | `StringBuilder` |
| Raw `java.util.List` (no generics) | `List<String>` |
| Subclassing `Thread` directly | `ExecutorService` / virtual threads |
| `e.printStackTrace()` | SLF4J logger |
| `System.out` / `System.err` logging | SLF4J logger |

### `util/LegacyDateTimeUtils.java`
| Pattern | Modern replacement |
|---------|-------------------|
| Shared `static SimpleDateFormat` (not thread-safe) | `DateTimeFormatter` (immutable) |
| `java.util.Date` | `LocalDateTime` / `Instant` |
| `Calendar.getInstance()` | `java.time` API |
| `System.currentTimeMillis()` | `Instant.now()` |
| `synchronized` wrapper around the formatter | Remove — `DateTimeFormatter` is thread-safe |

### `service/LegacyReflectionService.java`
| Pattern | Modern replacement |
|---------|-------------------|
| `Class.newInstance()` (deprecated since Java 9) | `getDeclaredConstructor().newInstance()` |
| `field.setAccessible(true)` to read private state | Accessors / Spring Data projections |
| Reflective private-method invocation | Refactor the design |
| `ObjectOutputStream` serialization | Jackson / JSON |
| `ObjectInputStream` deserialization | Jackson — **also CWE-502 risk** |
| Hand-rolled field-by-field bean copier | MapStruct |
| Hand-rolled reflection-based DI | Spring `@Autowired` |

---

## Cloud Readiness Issues

### `config/HardcodedConfiguration.java`
Everything is a compile-time constant, so nothing can be changed per environment:
server port, file storage path (`C:\legacy-app\files\`), log directory (`/var/legacy-app/logs/`),
temp directory, cache size and TTL, thread-pool size and queue capacity, HTTP and DB timeouts,
payment service URL, email service URL, three feature flags, auth provider and session timeout.
`getOrCreateTempDirectory()` also writes to the local filesystem.

**Fix direction:** externalize via environment variables / `application-{profile}.yml`, move secrets
to Azure Key Vault, move feature flags to a config service, move file storage to Blob Storage.

### `config/ApplicationDeploymentConfig.java`
Hard-coded config/secrets/certificate file paths, no environment-variable support, hard-coded
`DEBUG` log level and local log file, **missing health endpoints**, **no graceful shutdown**
(calls `System.exit(0)`), SSL disabled with a hard-coded keystore path, **no metrics export**,
**no distributed tracing**, hard-coded connection and memory limits, hard-coded region, and
secrets loaded from a properties file.

**Fix direction:** Spring Boot Actuator (`/actuator/health/liveness`, `/actuator/health/readiness`),
Micrometer + Prometheus, OpenTelemetry, `server.shutdown=graceful`, Key Vault for secrets.

### `controller/LegacyAPIController.java`
| Pattern | Why it blocks the cloud |
|---------|------------------------|
| `static int requestCounter` | Diverges across replicas |
| `Map requestCache` holding the logged-in user | Session lost when a request hits another pod |
| Hard-coded `/v1/` path versioning | Versioning should be routed, not compiled in |
| Writes to `C:\exports\` inside a request handler | Path doesn't exist in a container; blocks the thread |
| Hard-coded external service URL | Not environment-specific |
| No retry on the outbound call | One blip fails the request |
| No connect/read timeout | Hung dependency exhausts the thread pool |
| No health/readiness endpoints | Orchestrator can't tell if the pod is alive |
| Hard-coded limits as private constants | Not tunable |

**Fix direction:** make the controller stateless, move session state to Redis via Spring Session,
push exports to Blob Storage or a queue, add Resilience4j retry + circuit breaker and explicit timeouts.

---

## SQL Cloud Readiness Issues — `dao/LegacyDatabaseConnection.java`

This file is the focus of the database-migration lab.

| # | Pattern | Cloud-ready target |
|---|---------|-------------------|
| 1 | Hard-coded JDBC URL `jdbc:mysql://legacy-db.local:3306/inventory_db` | Connection string from env var / Key Vault |
| 2 | Hard-coded credentials (`admin` / `Legacy@2018`) | Azure Managed Identity |
| 3 | Manual `Class.forName("com.mysql.jdbc.Driver")` | Obsolete since JDBC 4.0 — remove |
| 4 | **No connection pooling** — new connection per query | HikariCP (Spring Boot default) |
| 5 | Hard-coded connect/socket timeouts (30s / 60s) | Externalized datasource properties |
| 6 | `Statement.execute(userSuppliedSql)` | `PreparedStatement` / JPA — also SQL-injection risk |
| 7 | Hard-coded 365-day retention `DELETE` | Parameterized, scheduled outside the app |
| 8 | No retry on transient failures | Retry with backoff — Azure SQL throttles transiently |
| 9 | Hard-coded read-replica URL | Read-replica routing via config |
| 10 | Stored procedure `sp_legacy_batch_process` + hard-coded batch size | Portable query / ORM, tunable batch size |
| 11 | Backups written to `C:\legacy-backups\db\` | Azure Backup / Blob Storage |

**Reference rewrite:**

```java
// Before — connection per call, secrets in source, no pooling
Class.forName("com.mysql.jdbc.Driver");
return DriverManager.getConnection(
    "jdbc:mysql://legacy-db.local:3306/inventory_db", "admin", "Legacy@2018");

// After — pooled, externalized, no driver registration needed
@Bean
DataSource dataSource() {
    HikariConfig cfg = new HikariConfig();
    cfg.setJdbcUrl(System.getenv("DATABASE_URL"));
    cfg.setMaximumPoolSize(20);
    cfg.setMinimumIdle(5);
    return new HikariDataSource(cfg);
}
```

---

## Pre-existing Issues (not seeded — but you will hit them)

These exist in the original app and are worth knowing before you start:

- **`mvn test` fails on JDK 17+.** Spring 4.3 (Boot 1.5) and Mockito 1.x use an old cglib that
  cannot run under JPMS strong encapsulation:
  `InaccessibleObjectException: module java.base does not "opens java.lang"`.
  Two tests error out for this reason. `DateUtilsTest` (8 tests) still passes.
  This is not a bug in your changes — it is the single most concrete argument for the upgrade.
- `mvn clean compile` **does** succeed on any JDK.
- `SecurityConfig` hard-codes three user passwords; `DatabaseConfig` hard-codes DB credentials.
- `SimpleCacheManager` uses an unsynchronized `HashMap` as a shared cache.
- `ProductDao` concatenates user input into SQL.
