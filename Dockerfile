# syntax=docker/dockerfile:1

# ---- Build stage ----
FROM maven:3.8.8-eclipse-temurin-8 AS build
WORKDIR /workspace

# Cache dependencies first
COPY pom.xml .
RUN mvn -B -q dependency:go-offline

# Build the executable WAR
COPY src ./src
RUN mvn -B -q clean package -DskipTests

# ---- Runtime stage ----
FROM eclipse-temurin:8-jre
WORKDIR /app

# Non-root user for security
RUN groupadd --system spring && useradd --system --gid spring spring

# Spring Boot repackages the WAR as an executable archive (embedded Tomcat)
COPY --from=build /workspace/target/legacy-inventory-mgmt-1.0.0-SNAPSHOT.war app.war

# Application listens on 3010 with context path /inventory
EXPOSE 3010

USER spring

ENV JAVA_OPTS=""

ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.war"]
