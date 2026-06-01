package com.legacy.inventory.util;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * OUTDATED: JUnit 4 test for utility class
 */
public class DateUtilsTest {

    @Test
    public void testFormatDate() {
        Date date = new Date(1609459200000L); // 2021-01-01
        String formatted = DateUtils.formatDate(date);
        assertNotNull(formatted);
    }

    @Test
    public void testFormatDateNull() {
        String result = DateUtils.formatDate(null);
        assertEquals("", result);
    }

    @Test
    public void testParseDate() {
        Date date = DateUtils.parseDate("2021-01-01");
        assertNotNull(date);
    }

    @Test
    public void testParseDateInvalid() {
        Date date = DateUtils.parseDate("not-a-date");
        assertNull(date);
    }

    @Test
    public void testAddDays() {
        Date date = new Date();
        Date result = DateUtils.addDays(date, 7);
        assertTrue(result.after(date));
    }

    @Test
    public void testIsExpired() {
        Date pastDate = new Date(System.currentTimeMillis() - 86400000);
        assertTrue(DateUtils.isExpired(pastDate));

        Date futureDate = new Date(System.currentTimeMillis() + 86400000);
        assertFalse(DateUtils.isExpired(futureDate));
    }

    @Test
    public void testHashString() {
        String hash = DateUtils.hashString("test");
        assertNotNull(hash);
        assertEquals(32, hash.length()); // MD5 produces 32 hex chars
    }

    @Test
    public void testGenerateId() {
        String id1 = DateUtils.generateId();
        String id2 = DateUtils.generateId();
        assertNotEquals(id1, id2);
    }
}
