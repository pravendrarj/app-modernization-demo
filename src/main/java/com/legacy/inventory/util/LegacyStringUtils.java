package com.legacy.inventory.util;

/**
 * DEPRECATED JAVA UTILITIES
 * This class demonstrates use of deprecated Java 8 APIs that need modernization
 * Java upgrade issue: Uses StringBuffer (thread-safe but slower) instead of StringBuilder
 * Java upgrade issue: Uses deprecated Date/SimpleDateFormat (not thread-safe)
 */
public class LegacyStringUtils {
    
    // JAVA UPGRADE ISSUE: StringBuffer is obsolete; StringBuilder is preferred (Java 1.5+)
    public static String concatenateStrings(String... strings) {
        StringBuffer buffer = new StringBuffer();
        for (String s : strings) {
            buffer.append(s);
            buffer.append(" ");
        }
        return buffer.toString().trim();
    }
    
    // JAVA UPGRADE ISSUE: Using raw types (pre-generics pattern)
    public static void processLegacyCollection(java.util.List list) {
        for (Object item : list) {
            if (item instanceof String) {
                System.out.println((String) item);
            }
        }
    }
    
    // JAVA UPGRADE ISSUE: Deprecated Thread usage pattern
    public static class LegacyWorkerThread extends Thread {
        private String data;
        
        public LegacyWorkerThread(String data) {
            this.data = data;
        }
        
        @Override
        @Deprecated
        public void run() {
            System.out.println(data);
        }
    }
    
    // JAVA UPGRADE ISSUE: Using Throwable.printStackTrace() instead of logging
    public static void handleLegacyException(Exception e) {
        e.printStackTrace(); // Should use logger instead
    }
    
    // JAVA UPGRADE ISSUE: Using System.out instead of logging framework
    public static void logLegacy(String message) {
        System.out.println("[LOG] " + message);
        System.err.println("[ERR] " + message);
    }
}
