package com.legacy.inventory.util;

import java.util.*;
import java.text.SimpleDateFormat;

/**
 * LEGACY DATE AND TIME HANDLING
 * Java upgrade issue: SimpleDateFormat is NOT thread-safe (should use java.time API - LocalDateTime, ZonedDateTime)
 * Cloud readiness issue: Hard-coded timezone assumptions
 * Java upgrade issue: Date class is obsolete; should use java.time
 */
public class LegacyDateTimeUtils {
    
    // JAVA UPGRADE ISSUE: SimpleDateFormat is not thread-safe and should not be shared
    // Should use ThreadLocal<SimpleDateFormat> or preferably java.time.LocalDateTime
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    // JAVA UPGRADE ISSUE: Also uses Date class (deprecated in favor of LocalDateTime)
    public static String formatDate(Date date) {
        return dateFormat.format(date);
    }
    
    // JAVA UPGRADE ISSUE: Using deprecated Date constructor
    public static Date createDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, day);
        return cal.getTime();
    }
    
    // CLOUD READINESS ISSUE: Hard-coded timezone assumptions
    public static Date getCurrentDateInUSEastern() {
        TimeZone tz = TimeZone.getTimeZone("US/Eastern");
        Calendar cal = Calendar.getInstance(tz);
        return cal.getTime();
    }
    
    // JAVA UPGRADE ISSUE: Using System.currentTimeMillis() instead of Instant
    public static long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }
    
    // JAVA UPGRADE ISSUE: Synchronizing on SimpleDateFormat (anti-pattern)
    public static synchronized String formatDateThreadSafe(Date date) {
        return dateFormat.format(date);
    }
}
