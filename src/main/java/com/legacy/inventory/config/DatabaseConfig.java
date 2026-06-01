package com.legacy.inventory.config;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * OUTDATED PATTERNS:
 * - Uses commons-dbcp (should use HikariCP, which is default in Spring Boot 2+)
 * - Manual DataSource configuration (should use Spring Boot auto-config)
 * - Hardcoded credentials in source code
 */
@Configuration
public class DatabaseConfig {

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setDriverClassName("org.h2.Driver");
        dataSource.setUrl("jdbc:h2:mem:inventorydb;DB_CLOSE_DELAY=-1");
        dataSource.setUsername("sa");
        dataSource.setPassword("");
        dataSource.setMaxActive(10);
        dataSource.setMinIdle(2);
        dataSource.setInitialSize(5);

        return dataSource;
    }
}
