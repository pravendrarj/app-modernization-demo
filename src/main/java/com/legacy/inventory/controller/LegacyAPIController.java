package com.legacy.inventory.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import java.io.*;

/**
 * CLOUD READINESS ISSUES - REST API AND STATELESS DESIGN
 * Cloud readiness issue: Controller holds state (not stateless)
 * Cloud readiness issue: Hard-coded API paths and versioning
 * Cloud readiness issue: No proper error handling for cloud environments
 * Cloud readiness issue: Local file I/O in web request handler
 */
@Controller
@RequestMapping("/legacy-api")
public class LegacyAPIController {
    
    // CLOUD READINESS ISSUE: Stateful controller (should be stateless for horizontal scaling)
    private static int requestCounter = 0;
    private java.util.Map<String, Object> requestCache = new java.util.HashMap<>();
    private long lastRequestTime = 0;
    
    // CLOUD READINESS ISSUE: Hard-coded API version in path (not scalable)
    @GetMapping("/v1/products")
    public String getProductsV1() {
        return "products-v1";
    }
    
    // CLOUD READINESS ISSUE: Local file I/O in API request (blocks container scalability)
    @PostMapping("/v1/export")
    public String exportProducts(@RequestParam String format) throws IOException {
        String filename = "C:\\exports\\products_" + System.currentTimeMillis() + "." + format;
        FileWriter fw = new FileWriter(filename);
        fw.write("product,price,quantity\n");
        fw.close();
        return "File saved to: " + filename; // This path won't work in containers!
    }
    
    // CLOUD READINESS ISSUE: No proper error handling for cloud-specific failures
    @GetMapping("/v1/external-data")
    public String getExternalData() throws Exception {
        // Hard-coded external endpoint URL (should be externalized)
        java.net.URL url = new java.net.URL("http://legacy-external-api.local:8080/data");
        java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
        
        // CLOUD READINESS ISSUE: No retry logic for transient failures
        // CLOUD READINESS ISSUE: No timeout configuration for cloud environments
        java.io.BufferedReader reader = new java.io.BufferedReader(
            new java.io.InputStreamReader(conn.getInputStream())
        );
        String result = reader.readLine();
        reader.close();
        return result;
    }
    
    // CLOUD READINESS ISSUE: Request counter across instances (not cloud-safe)
    @GetMapping("/v1/health")
    public String health() {
        requestCounter++; // This won't work in distributed environment!
        return "OK - Total requests: " + requestCounter;
    }
    
    // CLOUD READINESS ISSUE: Hard-coded session management (not cloud-ready)
    @PostMapping("/v1/login")
    public String login(@RequestParam String username) {
        lastRequestTime = System.currentTimeMillis();
        requestCache.put("currentUser", username);
        return "Login successful for " + username;
    }
    
    // CLOUD READINESS ISSUE: Depends on local session state
    @GetMapping("/v1/user-profile")
    public String getUserProfile() {
        String currentUser = (String) requestCache.get("currentUser");
        if (currentUser == null) {
            return "Not logged in"; // Session lost in multi-instance deployment!
        }
        return "Profile for " + currentUser;
    }
    
    // CLOUD READINESS ISSUE: Hard-coded response formats (not flexible)
    @GetMapping(value = "/v1/data", produces = "application/json")
    public String getDataAsJSON() {
        return "{\"status\":\"ok\"}"; // String concatenation instead of proper serialization
    }
    
    // CLOUD READINESS ISSUE: No health check endpoint for container orchestration
    // This should have liveness and readiness probes
    
    // CLOUD READINESS ISSUE: Hard-coded configuration in controller
    private static final int MAX_RECORDS = 1000;
    private static final long TIMEOUT_SECONDS = 30;
    private static final String CACHE_KEY_PREFIX = "legacy_";
}
