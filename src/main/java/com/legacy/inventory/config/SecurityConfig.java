package com.legacy.inventory.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * OUTDATED PATTERNS:
 * - Extends WebSecurityConfigurerAdapter (deprecated in Spring Security 5.7+, removed in 6.0)
 * - Uses in-memory users with plain text passwords
 * - Disables CSRF (security risk)
 * - No password encoder configured properly
 * - Uses deprecated authorize() API
 * - No OAuth2/JWT support
 * - Basic authentication only
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // OUTDATED: Override deprecated method
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .authorizeRequests()
                .anyRequest().permitAll();

        // OUTDATED: Allow H2 console frames
        http.headers().frameOptions().disable();
    }

    // OUTDATED: In-memory authentication with plain text passwords
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
            // SECURITY: Using deprecated .withDefaultPasswordEncoder()
            .withUser("admin")
                .password("admin123")
                .roles("ADMIN")
            .and()
            .withUser("user")
                .password("user123")
                .roles("USER")
            .and()
            .withUser("manager")
                .password("manager123")
                .roles("USER", "ADMIN");
    }
}
