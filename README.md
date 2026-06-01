# Legacy Inventory Management System

A deliberately outdated Java application built for **demonstrating GitHub Copilot App Modernization** capabilities in VS Code.

## Tech Stack (All Outdated)

| Component | Current (Legacy) | Recommended Modern |
|---|---|---|
| **Java** | 8 | 21 |
| **Spring Boot** | 1.5.22 (EOL) | 3.3+ |
| **Hibernate** | 4.3.x | 6.x |
| **JPA** | javax.persistence | jakarta.persistence |
| **Servlet** | javax.servlet 3.1 | jakarta.servlet 6.0 |
| **Validation** | javax.validation 1.1 | jakarta.validation 3.0 |
| **Mail** | javax.mail | jakarta.mail |
| **JAXB** | javax.xml.bind (removed JDK 11+) | jakarta.xml.bind |
| **Logging** | Log4j 1.x (CVE!) | SLF4J + Logback |
| **Testing** | JUnit 4 | JUnit 5 (Jupiter) |
| **Jackson** | 2.8.x (CVEs) | 2.17+ |
| **Connection Pool** | Commons DBCP | HikariCP |
| **HTTP Client** | HttpURLConnection | WebClient / RestClient |
| **Build/Deploy** | WAR on Tomcat | JAR with embedded server |
| **Security** | WebSecurityConfigurerAdapter | SecurityFilterChain bean |
| **Collections** | Commons Collections 3.x (CVE!) | Java built-in / Guava 33 |
| **Guava** | 18.0 | 33+ |
| **MySQL Connector** | 5.1.x | 8.x (com.mysql) |

---

## Demo Scenarios for GitHub Copilot App Modernization

### 1. Framework Upgrade Assessment
**What to demo:** Detection of outdated Spring Boot 1.5.x, Hibernate 4.x, and Java 8
- Run appmod assessment to detect Spring Boot 1.5.22 (EOL since 2019)
- Identify javax.* namespace usage (should be jakarta.*)
- Flag deprecated Spring annotations (WebMvcConfigurerAdapter, WebSecurityConfigurerAdapter)

### 2. CVE / Security Vulnerability Detection
**What to demo:** Identification of dependencies with known CVEs
- **Log4j 1.2.17** — CVE-2019-17571 (critical deserialization vulnerability)
- **Commons Collections 3.2.1** — CVE-2015-6420 (deserialization RCE)
- **Commons FileUpload 1.3.1** — CVE-2016-1000031 (DDoS)
- **Jackson Databind 2.8.x** — Multiple deserialization CVEs
- **H2 1.4.193** — CVE-2022-23221 (RCE)
- **MySQL Connector 5.1.38** — Multiple CVEs

### 3. Deprecated API Detection
**What to demo:** Finding deprecated/removed APIs
- `javax.xml.bind.DatatypeConverter` — removed from JDK 11+
- `java.net.URL` constructor — deprecated in Java 20
- `WebSecurityConfigurerAdapter` — deprecated in Spring Security 5.7
- `WebMvcConfigurerAdapter` — deprecated in Spring 5.0
- `JpaRepository.findOne()` — changed to `findById()` returning Optional
- `SimpleDateFormat` — thread-unsafe, replaced by `DateTimeFormatter`
- `java.util.Date` — replaced by `java.time` API

### 4. Security Anti-Pattern Detection
**What to demo:** Security configuration issues
- CSRF disabled in SecurityConfig
- Wildcard CORS (`allowedOrigins("*")`)
- Hardcoded credentials (admin/admin123)
- Plain text passwords in DB
- SQL injection in `ProductDao.searchProducts()`
- Stack trace exposure in error responses
- H2 console enabled without security
- No TLS/SSL configuration for email

### 5. Cloud Migration Readiness
**What to demo:** Local-only patterns that block cloud deployment
- WAR packaging (should be JAR for containers)
- Local filesystem usage for reports (`/var/reports/`)
- Local file-based logging (`/var/log/inventory/`)
- No Docker/container support
- No Kubernetes manifests
- No health check endpoints (no Spring Actuator)
- No externalized configuration (secrets hardcoded)
- No distributed caching (custom HashMap cache)
- No cloud-native database config (no connection string from env)
- SMTP hardcoded to internal server

### 6. Code Pattern Modernization
**What to demo:** Obsolete coding patterns
- XML-based Spring configuration (`spring-config.xml`)
- Manual getters/setters (should use Lombok or records)
- `java.util.Date` / `Calendar` / `SimpleDateFormat`
- Pre-Java 8 iteration (for-each loops instead of streams)
- Raw `HashMap` for API responses (should use records/DTOs)
- `synchronized` method for thread safety
- Manual singleton cache instead of Spring Cache
- JUnit 4 `@RunWith` instead of JUnit 5 `@ExtendWith`

---

## Project Structure

```
src/
├── main/
│   ├── java/com/legacy/inventory/
│   │   ├── LegacyInventoryApplication.java    # Spring Boot 1.5 + WAR init
│   │   ├── ServletInitializer.java            # WAR deployment support
│   │   ├── config/
│   │   │   ├── SecurityConfig.java            # Deprecated WebSecurityConfigurerAdapter
│   │   │   ├── WebConfig.java                 # Deprecated WebMvcConfigurerAdapter
│   │   │   └── DatabaseConfig.java            # Commons DBCP, hardcoded creds
│   │   ├── controller/
│   │   │   ├── ProductController.java         # @RequestMapping, javax.servlet
│   │   │   └── OrderController.java           # Legacy REST patterns
│   │   ├── dao/
│   │   │   └── ProductDao.java                # SQL injection, raw JDBC
│   │   ├── exception/
│   │   │   └── GlobalExceptionHandler.java    # Stack trace exposure
│   │   ├── filter/
│   │   │   └── RequestLoggingFilter.java      # javax.servlet.Filter
│   │   ├── model/
│   │   │   ├── Product.java                   # javax.persistence, java.util.Date
│   │   │   ├── Category.java                  # Eager fetch, no records
│   │   │   ├── Order.java                     # String status, EAGER loading
│   │   │   ├── OrderItem.java                 # Legacy entity
│   │   │   └── User.java                      # Plain text password field
│   │   ├── repository/
│   │   │   ├── ProductRepository.java         # Native queries, Date params
│   │   │   ├── OrderRepository.java           # Legacy queries
│   │   │   ├── CategoryRepository.java
│   │   │   └── UserRepository.java
│   │   ├── scheduler/
│   │   │   └── InventoryScheduler.java        # Local filesystem, Log4j
│   │   ├── service/
│   │   │   ├── ProductService.java            # Log4j, field injection, no Optional
│   │   │   ├── OrderService.java              # Legacy patterns
│   │   │   └── NotificationService.java       # javax.mail, HttpURLConnection, JAXB
│   │   └── util/
│   │       ├── DateUtils.java                 # MD5, JAXB, SimpleDateFormat
│   │       └── SimpleCacheManager.java        # Custom cache, XML lifecycle
│   └── resources/
│       ├── application.properties             # Spring Boot 1.5.x config
│       ├── spring-config.xml                  # XML-based bean config
│       ├── log4j.xml                          # Log4j 1.x config
│       └── data.sql                           # Sample data
└── test/
    └── java/com/legacy/inventory/
        ├── service/ProductServiceTest.java    # JUnit 4
        ├── controller/ProductControllerIntegrationTest.java
        └── util/DateUtilsTest.java
```

---

## How to Run the Demo

### Prerequisites
- Java 8 JDK (the app won't run on newer JDKs without modifications)
- Maven 3.x

### Steps
```bash
# Build the project
mvn clean package -DskipTests

# Run the application
mvn spring-boot:run

# Access the application
# API: http://localhost:8080/inventory/api/products
# H2 Console: http://localhost:8080/inventory/h2-console
# Login: admin/admin123
```

### Using GitHub Copilot App Modernization

1. **Open the project** in VS Code with GitHub Copilot extension
2. **Run Assessment**: Use `@appmod` in Copilot Chat to analyze the codebase
3. **Review Findings**: Copilot will identify all the issues listed above
4. **Plan Migration**: Get recommendations for step-by-step modernization
5. **Execute Upgrades**: Use Copilot to assist with:
   - Spring Boot 1.5 → 3.3 upgrade
   - Java 8 → 21 migration
   - javax → jakarta namespace migration
   - CVE remediation
   - Cloud-native transformation (Docker, K8s, Azure/AWS)
   - JUnit 4 → JUnit 5 migration
   - Security hardening

---

## Summary of Issues Count

| Category | Count |
|---|---|
| Outdated frameworks | 8+ |
| Known CVEs in dependencies | 10+ |
| Deprecated APIs | 12+ |
| Security anti-patterns | 8+ |
| Cloud migration blockers | 10+ |
| Code pattern issues | 15+ |
| **Total modernization opportunities** | **60+** |
