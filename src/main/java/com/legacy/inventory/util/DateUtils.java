package com.legacy.inventory.util;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * OUTDATED PATTERNS:
 * - Uses commons-lang 2.x (should be commons-lang3)
 * - Uses SimpleDateFormat (not thread-safe, should use DateTimeFormatter)
 * - Uses java.util.Date (should use java.time API)
 * - Uses javax.xml.bind.DatatypeConverter (removed from JDK 11+)
 * - Uses MD5 for hashing (insecure, should use SHA-256+)
 * - Manual singleton pattern (should use Spring bean)
 * - Static utility class with state
 */
public class DateUtils {

    private static final Logger logger = Logger.getLogger(DateUtils.class);

    // OUTDATED: Static SimpleDateFormat instances (thread-unsafe)
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    private static final SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static final SimpleDateFormat LEGACY_FORMAT = new SimpleDateFormat("dd/MM/yyyy");

    private DateUtils() {
        // Prevent instantiation
    }

    // OUTDATED: Using java.util.Date
    public static String formatDate(Date date) {
        if (date == null) return "";
        return DATE_FORMAT.format(date);
    }

    public static String formatDateTime(Date date) {
        if (date == null) return "";
        return DATETIME_FORMAT.format(date);
    }

    public static Date parseDate(String dateStr) {
        if (StringUtils.isEmpty(dateStr)) return null;
        try {
            return DATE_FORMAT.parse(dateStr);
        } catch (ParseException e) {
            logger.error("Failed to parse date: " + dateStr, e);
            return null;
        }
    }

    // OUTDATED: Calendar-based date manipulation
    public static Date addDays(Date date, int days) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, days);
        return calendar.getTime();
    }

    public static Date getStartOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date getEndOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    // OUTDATED: Using deprecated Date methods
    @SuppressWarnings("deprecation")
    public static boolean isExpired(Date expiryDate) {
        if (expiryDate == null) return false;
        Date now = new Date();
        return expiryDate.before(now);
    }

    // OUTDATED: Manual date difference calculation
    public static long daysBetween(Date start, Date end) {
        long diff = end.getTime() - start.getTime();
        return diff / (1000 * 60 * 60 * 24);
    }

    // SECURITY: Using MD5 (insecure hash)
    public static String hashString(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(input.getBytes());
            // OUTDATED: Using javax.xml.bind.DatatypeConverter (removed in JDK 11+)
            return DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            logger.error("MD5 not available", e);
            return input;
        }
    }

    // OUTDATED: Manual UUID generation
    public static String generateId() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }
}
