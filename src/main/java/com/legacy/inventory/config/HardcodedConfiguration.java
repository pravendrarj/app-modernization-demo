package com.legacy.inventory.config;

import java.io.File;

/**
 * CLOUD READINESS ISSUES: Hard-coded Configuration
 * Cloud readiness issue: All configuration is hard-coded instead of using environment variables
 * Cloud readiness issue: References local file paths that won't work in containers
 * Cloud readiness issue: No support for externalized configuration (12-factor app)
 */
public class HardcodedConfiguration {
    
    // CLOUD READINESS ISSUE: Hard-coded application server port
    public static final int APP_SERVER_PORT = 8080;
    
    // CLOUD READINESS ISSUE: Hard-coded file storage path (won't work in containers)
    public static final String FILE_STORAGE_PATH = "C:\\legacy-app\\files\\";
    
    // CLOUD READINESS ISSUE: Hard-coded log directory
    public static final String LOG_DIRECTORY = "/var/legacy-app/logs/";
    
    // CLOUD READINESS ISSUE: Hard-coded temporary directory
    public static final String TEMP_DIRECTORY = "/tmp/legacy-app/";
    
    // CLOUD READINESS ISSUE: Hard-coded cache settings
    public static final int CACHE_SIZE = 1000;
    public static final long CACHE_TTL_SECONDS = 3600;
    
    // CLOUD READINESS ISSUE: Hard-coded thread pool sizes (not scalable)
    public static final int THREAD_POOL_SIZE = 10;
    public static final int QUEUE_CAPACITY = 100;
    
    // CLOUD READINESS ISSUE: Hard-coded timeout values
    public static final long HTTP_TIMEOUT_MS = 30000;
    public static final long DB_QUERY_TIMEOUT_MS = 60000;
    
    // CLOUD READINESS ISSUE: Hard-coded external service URLs
    public static final String PAYMENT_SERVICE_URL = "http://legacy-payment-service.local:9090/api";
    public static final String EMAIL_SERVICE_URL = "http://mail.internal.company.com:25";
    
    // CLOUD READINESS ISSUE: Hard-coded feature flags (should use external service)
    public static final boolean ENABLE_NEW_CHECKOUT = false;
    public static final boolean ENABLE_ASYNC_PROCESSING = false;
    public static final boolean ENABLE_DISTRIBUTED_CACHE = false;
    
    // CLOUD READINESS ISSUE: Hard-coded authentication settings
    public static final String AUTH_PROVIDER = "local";
    public static final long SESSION_TIMEOUT_MINUTES = 30;
    
    public static String getFilePath(String filename) {
        return FILE_STORAGE_PATH + filename;
    }
    
    public static File getOrCreateTempDirectory() {
        File tempDir = new File(TEMP_DIRECTORY);
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        return tempDir;
    }
}
