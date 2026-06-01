package com.legacy.inventory.util;

import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * OUTDATED PATTERNS:
 * - Custom cache implementation (should use Spring Cache, Redis, or Caffeine)
 * - Not thread-safe (uses HashMap instead of ConcurrentHashMap)
 * - Manual TTL management
 * - Init/destroy lifecycle via XML config
 * - No generics type safety
 */
public class SimpleCacheManager {

    private static final Logger logger = Logger.getLogger(SimpleCacheManager.class);

    // OUTDATED: Raw HashMap for caching (not thread-safe)
    private Map<String, CacheEntry> cache = new HashMap<>();
    private int maxSize;
    private int ttlMinutes;

    public void setMaxSize(int maxSize) {
        this.maxSize = maxSize;
    }

    public void setTtlMinutes(int ttlMinutes) {
        this.ttlMinutes = ttlMinutes;
    }

    // OUTDATED: Init method called from XML config
    public void init() {
        logger.info("Initializing SimpleCacheManager with maxSize=" + maxSize + ", ttl=" + ttlMinutes + " minutes");
    }

    // OUTDATED: Cleanup method called from XML config
    public void cleanup() {
        logger.info("Cleaning up SimpleCacheManager");
        cache.clear();
    }

    // OUTDATED: No generics, stores Object
    public void put(String key, Object value) {
        if (cache.size() >= maxSize) {
            evictOldest();
        }
        cache.put(key, new CacheEntry(value, System.currentTimeMillis()));
    }

    public Object get(String key) {
        CacheEntry entry = cache.get(key);
        if (entry == null) {
            return null;
        }

        // Check TTL
        long age = System.currentTimeMillis() - entry.timestamp;
        if (age > ttlMinutes * 60 * 1000) {
            cache.remove(key);
            return null;
        }

        return entry.value;
    }

    public void remove(String key) {
        cache.remove(key);
    }

    // OUTDATED: Manual eviction with Iterator
    private void evictOldest() {
        String oldestKey = null;
        long oldestTime = Long.MAX_VALUE;

        // OUTDATED: Using Iterator instead of streams
        Iterator<Map.Entry<String, CacheEntry>> iterator = cache.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, CacheEntry> entry = iterator.next();
            if (entry.getValue().timestamp < oldestTime) {
                oldestTime = entry.getValue().timestamp;
                oldestKey = entry.getKey();
            }
        }

        if (oldestKey != null) {
            cache.remove(oldestKey);
        }
    }

    // OUTDATED: Inner class without records (Java 14+)
    private static class CacheEntry {
        Object value;
        long timestamp;

        CacheEntry(Object value, long timestamp) {
            this.value = value;
            this.timestamp = timestamp;
        }
    }
}
