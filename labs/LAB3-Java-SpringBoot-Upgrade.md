# Lab 3: Java & Spring Boot Upgrade

## Objective
Use the `@modernize-java-upgrade` agent to upgrade the application from Java 8 / Spring Boot 1.5 to modern versions.

---

## Step 1: Plan the Upgrade

Open Copilot Chat and type:

```
@modernize-java-upgrade Upgrade this project to Java 17 and Spring Boot 3.x
```

The agent will:
1. Analyze current versions
2. Determine the upgrade path (1.5 → 2.x → 3.x)
3. Generate a step-by-step upgrade plan

## Step 2: Review the Upgrade Plan

The agent generates a plan with tasks. Review it carefully. Expected changes:

| Area | From | To |
|------|------|----|
| Java | 1.8 | 17 |
| Spring Boot | 1.5.22 | 3.2+ |
| javax.* | javax.persistence | jakarta.persistence |
| Hibernate | 4.3 | 6.x |
| JUnit | 4 | 5 |
| Packaging | WAR | JAR |

## Step 3: Confirm and Execute the Plan

When prompted, confirm the upgrade plan. The agent will begin modifying files:

```
@modernize-java-upgrade Execute the upgrade plan
```

Watch as it updates:
- `pom.xml` — parent version, Java version, dependencies
- Java source files — `javax.*` → `jakarta.*` imports
- Configuration — Spring Boot 2.x/3.x property names
- Tests — JUnit 4 → JUnit 5 annotations

## Step 4: Handle Breaking Changes

The agent may flag breaking changes. Common ones for this app:

1. **`spring.datasource.driverClassName`** — auto-detection in Boot 3
2. **`server.context-path`** → `server.servlet.context-path`
3. **`SpringBootServletInitializer`** moved to a different package
4. **`@ImportResource`** with XML config needs review

Ask for help with specific issues:

```
@modernize-java-upgrade Fix the breaking change in SecurityConfig.java
```

## Step 5: Modernize Legacy Java APIs in the Source Code

Upgrading the `pom.xml` gets you onto Java 17, but it does **not** modernize code that still
uses obsolete APIs. Those compile fine on Java 17 — they're just wrong. Address them explicitly:

```
@modernize-java-upgrade Replace the legacy Java APIs in LegacyDateTimeUtils.java, LegacyStringUtils.java and LegacyReflectionService.java with modern equivalents
```

| File | Replace | With |
|------|---------|------|
| `LegacyDateTimeUtils.java` | `Date`, `Calendar`, shared `SimpleDateFormat` | `LocalDateTime`, `Instant`, `DateTimeFormatter` |
| `LegacyStringUtils.java` | `StringBuffer`, raw types, `Thread` subclass, `printStackTrace()` | `StringBuilder`, generics, `ExecutorService`, SLF4J |
| `LegacyReflectionService.java` | `Class.newInstance()`, `ObjectInputStream`, manual DI | `getDeclaredConstructor()`, Jackson, Spring `@Autowired` |

Two of these are more than style problems:

- The shared `static SimpleDateFormat` is **not thread-safe** and will corrupt dates under
  concurrent load. `DateTimeFormatter` is immutable and fixes it outright.
- `ObjectInputStream.readObject()` on untrusted bytes is **CWE-502** (deserialization of
  untrusted data) — the same class of flaw behind the Commons Collections CVE in Lab 4.

Ask the agent to explain the risk before accepting the change:

```
@modernize Why is the SimpleDateFormat in LegacyDateTimeUtils unsafe, and what breaks if I leave it?
```

## Step 6: Verify the Upgrade

After the upgrade completes:

```
@modernize Build and test the upgraded application
```

Or run manually:

```bash
mvn clean compile
mvn test
```

> **Before the upgrade, `mvn test` fails** with
> `InaccessibleObjectException: module java.base does not "opens java.lang"` — Spring 4.3 and
> Mockito 1.x can't run on JDK 17+. That is the baseline, not a regression.
> **After** the upgrade to Spring Boot 3.x, those tests should load a context and pass.
> That transition is the clearest signal the upgrade actually worked.

## Step 7: Validate the Changes

```
@modernize Verify this upgrade is complete and nothing was missed
```

---

## Key Commands Used

| Command | Purpose |
|---------|---------|
| `@modernize-java-upgrade Upgrade to Java 17 and Spring Boot 3.x` | Start upgrade |
| `@modernize-java-upgrade Execute the upgrade plan` | Run the plan |
| `@modernize-java-upgrade Replace legacy Java APIs in [files]` | Modernize source-level APIs |
| `@modernize-java-upgrade Fix breaking change in [file]` | Fix specific issues |
| `@modernize Build and test` | Verify results |

---

**Reference:** [Seeded Issue Catalog](APPENDIX-Seeded-Issue-Catalog.md) — Java upgrade patterns and their replacements

---

**Next:** [Lab 4 - CVE Remediation](LAB4-CVE-Remediation.md)
