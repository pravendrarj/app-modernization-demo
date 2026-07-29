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
- ❌ Spring Boot 1.5.22 (EOL since August 2019)
- ❌ Java 8 target (should be 17 or 21)
- ❌ Log4j 1.2.17 (Critical CVE-2019-17571)
- ❌ Commons Collections 3.2.1 (deserialization vulnerability)
- ❌ Jackson 2.8.11 (multiple known CVEs)
- ❌ WAR packaging (not cloud-native)
- ❌ No health checks or actuator endpoints

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
- No externalized configuration (hardcoded SMTP, no profiles)
- No health/readiness probes
- WAR packaging instead of JAR
- No containerization support
- No distributed tracing or metrics

## Step 5: Review the Assessment Summary

After the assessment completes, you can ask:

```
@modernize Summarize the assessment findings with priority rankings
```

---

## What You Learned

- How to trigger a full application assessment
- How to identify CVEs and security issues
- How to evaluate cloud readiness
- How to use `@modernize-java-assessment` and `@modernize-java-security` agents

---

**Next:** [Lab 3 - Java & Spring Boot Upgrade](LAB3-Java-SpringBoot-Upgrade.md)
