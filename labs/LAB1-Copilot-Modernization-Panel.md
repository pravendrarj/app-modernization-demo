# Lab 1: Getting Started with the Copilot Modernization Panel

## Objective
Learn how to access and navigate the GitHub Copilot App Modernization tools from the VS Code panel.

---

## Step 1: Open the Copilot Chat Panel

1. Press **Ctrl+Shift+I** (or click the Copilot Chat icon in the sidebar)
2. You now have access to Copilot Chat where you can invoke modernization agents

## Step 2: Discover Available Agents

In the Copilot Chat input box, type `@` to see available agents. Look for:

- **@modernize** — The main orchestrator for all modernization tasks
- **@modernize-java-assessment** — Assess Java codebases
- **@modernize-java-upgrade** — Upgrade Java/Spring Boot versions
- **@modernize-java-security** — Fix CVE vulnerabilities
- **@modernize-deployment** — Containerization & cloud deployment

## Step 3: Try Your First Command

Type the following in Copilot Chat:

```
@modernize What can you help me with?
```

This shows you the full list of capabilities.

## Step 4: Explore the Modernization Panel

1. Click the **GitHub Copilot App Modernization** icon in the Activity Bar (left sidebar)
2. This opens the dedicated modernization panel with:
   - **Assessment** — Analyze your app's current state
   - **Upgrade** — Plan and execute upgrades
   - **Deployment** — Containerize and deploy to cloud

## Step 5: Use the `#appmod` Context Variable

In Copilot Chat, you can use `#appmod` to provide modernization context:

```
Tell me about the current state of this application #appmod
```

This gives Copilot the app modernization context to provide more relevant answers.

---

## Key Takeaways

| Action | How |
|--------|-----|
| Open Copilot Chat | Ctrl+Shift+I |
| List agents | Type `@` in chat |
| Modernization panel | Activity Bar icon |
| Add appmod context | Use `#appmod` in prompts |

---

**Next:** [Lab 2 - Application Assessment](LAB2-Application-Assessment.md)
