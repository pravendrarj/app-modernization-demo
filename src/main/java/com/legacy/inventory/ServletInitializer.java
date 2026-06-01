package com.legacy.inventory;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * OUTDATED: ServletInitializer for WAR deployment to external Tomcat
 * Modern apps use embedded server with JAR packaging
 */
public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(LegacyInventoryApplication.class);
    }
}
