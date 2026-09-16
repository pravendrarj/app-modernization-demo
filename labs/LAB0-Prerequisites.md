# Lab 0: Prerequisites & Setup

## What You Need

| Tool | Version | Purpose |
|------|---------|---------|
| VS Code | Latest | IDE |
| Java JDK | 8+ | Build & run the app |
| Maven | 3.6+ | Dependency management |
| GitHub Copilot Extension | Latest | AI assistance |
| GitHub Copilot App Modernization Extension | Latest | App modernization tools |

## Install the Modernization Extension

1. Open VS Code
2. Go to **Extensions** (Ctrl+Shift+X)
3. Search for **"GitHub Copilot"** → Install
4. Search for **"Java Extension Pack"** → Install
5. Search for **"GitHub Copilot App Modernization"** → Install
6. Sign in with your GitHub account when prompted

## Open the Project

```bash
cd appmod-java
code .
```

## Verify the App Compiles

```bash
mvn clean compile
```

This succeeds on any JDK.

## Running the App

```bash
mvn clean install -DskipTests
mvn spring-boot:run
```

The app starts on **http://localhost:3010/inventory**

> ### ⚠️ Read this before you run `mvn test` or `spring-boot:run`
>
> This app targets **Java 8**. Spring Boot 1.5 (Spring 4.3) and Mockito 1.x bundle an old
> version of cglib that **cannot run on JDK 17 or newer**. You will see:
>
> ```
> InaccessibleObjectException: Unable to make protected final java.lang.Class
> java.lang.ClassLoader.defineClass(...) accessible:
> module java.base does not "opens java.lang" to unnamed module
> ```
>
> Two of the eleven tests error out for this reason (`DateUtilsTest` still passes).
> **This is expected, not a mistake on your part.** You have two options:
>
> - **Run the labs on JDK 8 or 11** if you want the app to start before you upgrade it.
> - **Stay on your modern JDK** and treat this failure as Exhibit A for why the upgrade is
>   necessary. Everything except `mvn test` and `spring-boot:run` works fine either way.
>
> After Lab 3 upgrades the app to Java 17 + Spring Boot 3.x, this failure disappears.

> **Note:** This is a legacy Spring Boot 1.5 app with Java 8, outdated dependencies, and known CVEs.
> It also contains deliberately seeded Java-upgrade and cloud-readiness issues — see the
> [Seeded Issue Catalog](APPENDIX-Seeded-Issue-Catalog.md) for a full map of what's in there
> and where. The labs will guide you through modernizing it.

---

**Next:** [Lab 1 - Getting Started with Copilot Modernization Panel](LAB1-Copilot-Modernization-Panel.md)
