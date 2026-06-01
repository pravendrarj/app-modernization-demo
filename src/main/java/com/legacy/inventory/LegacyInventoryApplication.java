package com.legacy.inventory;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ImportResource;

/**
 * Legacy Inventory Management System
 * 
 * OUTDATED PATTERNS:
 * - Extends SpringBootServletInitializer for WAR deployment (should use JAR)
 * - Uses @ImportResource for XML configuration (should use Java config)
 * - Targets Java 8 runtime
 * - Spring Boot 1.5.x (EOL)
 */
@SpringBootApplication
@ImportResource("classpath:spring-config.xml")
public class LegacyInventoryApplication extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LegacyInventoryApplication.class);
    }

    public static void main(String[] args) {
        SpringApplication.run(LegacyInventoryApplication.class, args);
    }
}
