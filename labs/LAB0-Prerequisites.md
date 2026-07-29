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
cd appmodJava
code .
```

## Verify the App Runs

```bash
mvn clean install -DskipTests
mvn spring-boot:run
```

The app starts on **http://localhost:3010/inventory**

> **Note:** This is a legacy Spring Boot 1.5 app with Java 8, outdated dependencies, and known CVEs. The labs will guide you through modernizing it.

---

**Next:** [Lab 1 - Getting Started with Copilot Modernization Panel](LAB1-Copilot-Modernization-Panel.md)
