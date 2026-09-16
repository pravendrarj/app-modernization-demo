# Lab 6: End-to-End Modernization with @modernize

## Objective
Use the `@modernize` orchestrator agent to run the full assess → plan → execute workflow in one flow.

---

## Step 1: Start the Full Modernization Workflow

Open Copilot Chat and type:

```
@modernize Modernize this Java application for Azure deployment
```

The `@modernize` agent orchestrates the entire process:
1. **Assess** — Scans the codebase for issues
2. **Plan** — Creates a prioritized migration plan
3. **Execute** — Applies changes step by step

## Step 2: Follow the Guided Workflow

The agent will ask you questions to clarify your target:

- **Target Java version?** → Java 17 or 21
- **Target Spring Boot version?** → 3.2+
- **Target cloud platform?** → Azure (AKS / Container Apps / App Service)
- **Fix CVEs?** → Yes

Answer the prompts to customize the modernization plan.

## Step 3: Review the Generated Plan

The agent creates a structured plan at `.github/modernize/`. Review:

```
@modernize Show me the modernization plan
```

The plan contains:
- `plan.md` — High-level migration steps
- `tasks.json` — Granular tasks with dependencies

## Step 4: Execute the Plan

```
@modernize Execute the plan
```

The agent processes tasks in dependency order:
1. Upgrade Java version
2. Upgrade Spring Boot
3. Fix CVE vulnerabilities
4. Migrate javax → jakarta
5. Replace legacy Java APIs (`Date`/`SimpleDateFormat` → `java.time`, `ObjectInputStream` → Jackson)
6. Externalize hard-coded configuration and secrets
7. Refactor stateful components for horizontal scaling
8. Add database connection pooling (HikariCP) and retry logic
9. Add cloud-native configuration (actuator, metrics, graceful shutdown)
10. Generate Dockerfile
11. Generate deployment manifests

## Step 5: Validate the Modernization

After execution completes:

```
@modernize Validate that the modernization is complete
```

The agent checks:
- ✅ Application compiles
- ✅ Tests pass — including the two that previously failed with
  `InaccessibleObjectException` on JDK 17+ (the clearest proof the upgrade landed)
- ✅ No remaining CVEs
- ✅ No hard-coded configuration, credentials, or filesystem paths
- ✅ No stateful components blocking horizontal scaling
- ✅ Database uses pooled connections with externalized credentials
- ✅ Cloud-ready configuration (health probes, metrics, graceful shutdown)
- ✅ Container support added

Cross-check the result against the catalog of what was deliberately seeded:

```
@modernize Compare the current state against labs/APPENDIX-Seeded-Issue-Catalog.md and list anything still outstanding
```

## Step 6: Generate a Migration Summary

```
@modernize Generate a migration summary report
```

This produces a summary of all changes made, before/after comparisons, and any remaining manual steps.

---

## Agents & Tools Reference

| Agent | Use Case |
|-------|----------|
| `@modernize` | Full orchestration (assess → plan → execute) |
| `@modernize-java-assessment` | Assessment only |
| `@modernize-java-upgrade` | Java/Spring Boot version upgrades |
| `@modernize-java-security` | CVE scanning and remediation |
| `@modernize-deployment` | Dockerfile, K8s, Azure deployment |
| `@modernize-rearchitecture` | Major rearchitecture (multi-agent) |

| Context Variable | Purpose |
|-----------------|---------|
| `#appmod` | Adds app modernization context to any Copilot Chat prompt |

---

## Bonus: Rearchitecture (Advanced)

For a full rearchitecture with multi-agent coordination:

```
@modernize-rearchitecture Rearchitect this monolithic application into microservices
```

This activates the multi-agent team:
- **Architect** — Analyzes and designs the target architecture
- **Planner** — Creates implementation plan
- **Worker** — Executes tasks with TDD
- **Tester** — Validates runtime behavior

---

## Summary of All Labs

| Lab | Topic | Agent Used |
|-----|-------|------------|
| 0 | Prerequisites & Setup | — |
| 1 | Copilot Modernization Panel | All |
| 2 | Application Assessment | `@modernize-java-assessment` |
| 3 | Java & Spring Boot Upgrade | `@modernize-java-upgrade` |
| 4 | CVE Remediation | `@modernize-java-security` |
| 5 | Cloud Readiness & Containerization | `@modernize-deployment` |
| 6 | End-to-End Modernization | `@modernize` |
| — | [Seeded Issue Catalog](APPENDIX-Seeded-Issue-Catalog.md) | Reference |

---

**Congratulations!** You've completed all labs and modernized a legacy Java application using GitHub Copilot App Modernization.
