# Lab 2: Application Assessment

## Objective
Use GitHub Copilot App Modernization to assess the current state of the legacy application — identify outdated dependencies, security vulnerabilities, and technical debt.

---

## Step 1: Run a Full Assessment Using the Agent

Open Copilot Chat (Ctrl+Shift+I) and type:

```
@modernize-java-assessment Assess this codebase and identify all issues
```

Wait for the assessment to complete. It will analyze:
- Java version compatibility
- Spring Boot version
- Dependency vulnerabilities (CVEs)
- Deprecated APIs and patterns
- Cloud readiness gaps

## Step 2: Get a Detailed Assessment Report

```
@modernize Analyze this repository and generate an assessment report
```

Review the report. It should flag issues like:

**Dependency & platform**
- ❌ Spring Boot 1.5.22 (EOL since August 2019)
- ❌ Java 8 target (should be 17 or 21)
- ❌ Log4j 1.2.17 (Critical CVE-2019-17571)
- ❌ Commons Collections 3.2.1 (deserialization vulnerability)
- ❌ Jackson 2.8.11 (multiple known CVEs)
- ❌ WAR packaging (not cloud-native)
- ❌ No health checks or actuator endpoints

**Source-level Java upgrade issues**
- ❌ `java.util.Date` / `Calendar` / shared `SimpleDateFormat` — `LegacyDateTimeUtils.java`
- ❌ `StringBuffer`, raw types, `Thread` subclassing, `printStackTrace()` — `LegacyStringUtils.java`
- ❌ `Class.newInstance()`, `ObjectInputStream`, hand-rolled DI — `LegacyReflectionService.java`

**Source-level cloud readiness issues**
- ❌ Compile-time constants for ports, paths, URLs, timeouts — `HardcodedConfiguration.java`
- ❌ Stateful controller, local file writes in a request handler — `LegacyAPIController.java`
- ❌ No graceful shutdown, no metrics, no tracing — `ApplicationDeploymentConfig.java`
- ❌ Hard-coded DB credentials, no connection pooling — `LegacyDatabaseConnection.java`

> Not every tool reports these the same way — some group several patterns into one finding.
> Use the [Seeded Issue Catalog](APPENDIX-Seeded-Issue-Catalog.md) to check what the assessment
> found against what is actually in the code.

## Step 3: Check for CVEs Specifically

```
@modernize-java-security Scan this project for CVE vulnerabilities
```

This triggers a focused security scan of all dependencies in `pom.xml`.

## Step 4: Ask About Cloud Readiness

```
@modernize How cloud-ready is this application? What needs to change for Azure deployment?
```

Expected findings:
- Configuration is compiled in, not externalized (ports, file paths, service URLs, timeouts, feature flags)
- Secrets in source code (DB credentials, user passwords, API keys)
- No health/readiness probes and no graceful shutdown
- Stateful controller — a request counter and an in-memory session map that break across replicas
- Local filesystem dependencies (`C:\legacy-app\files\`, `C:\exports\`, `/var/legacy-app/logs/`)
- Database access with no connection pooling and hard-coded connection strings
- WAR packaging instead of JAR
- No containerization support
- No distributed tracing or metrics

## Step 5: Drill Into the SQL / Database Readiness

The database layer is the most common blocker in a real migration, so assess it on its own:

```
@modernize Review LegacyDatabaseConnection.java for cloud readiness issues and tell me what would break on Azure SQL
```

Expected findings:

| Finding | Why it matters on Azure |
|---------|------------------------|
| Hard-coded JDBC URL and credentials | Cannot promote across environments; secrets in source control |
| No connection pooling — a new connection per query | Connection exhaustion under load; Azure SQL enforces limits |
| No retry logic | Azure SQL throttles transiently — a single blip fails the request |
| Stored procedure dependency | Not portable to a managed/serverless target |
| Backups written to a local path | Replaced by Azure Backup / Blob Storage |
| User input concatenated into SQL | SQL injection risk that survives the migration |

Then ask for the remediation:

```
@modernize How do I migrate this from on-prem MySQL to Azure Database for MySQL with HikariCP connection pooling?
```

## Step 6: Review the Assessment Summary

After the assessment completes, you can ask:

```
@modernize Summarize the assessment findings with priority rankings
```

---

## What You Learned

- How to trigger a full application assessment
- How to identify CVEs and security issues
- How to evaluate cloud readiness at both the dependency and source-code level
- How to assess a database layer specifically for cloud migration
- How to use `@modernize-java-assessment` and `@modernize-java-security` agents

---

**Reference:** [Seeded Issue Catalog](APPENDIX-Seeded-Issue-Catalog.md) — full map of what's in the code

**Next:** [Lab 3 - Java & Spring Boot Upgrade](LAB3-Java-SpringBoot-Upgrade.md)
