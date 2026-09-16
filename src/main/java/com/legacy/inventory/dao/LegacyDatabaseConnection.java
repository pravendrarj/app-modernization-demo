package com.legacy.inventory.dao;

import java.sql.*;

/**
 * CLOUD READINESS ISSUE - SQL SPECIFIC
 * Hard-coded database connection strings
 * No connection pooling for cloud scalability
 * Direct SQL with hard-coded credentials
 * Not using cloud-managed databases (Azure SQL, Managed PostgreSQL, etc.)
 */
public class LegacyDatabaseConnection {
    
    // CLOUD READINESS ISSUE - SQL: Hard-coded database connection string
    // Should use environment variables or Azure Key Vault
    private static final String DB_URL = "jdbc:mysql://legacy-db.local:3306/inventory_db";
    private static final String DB_USER = "admin";
    private static final String DB_PASSWORD = "Legacy@2018";
    
    // CLOUD READINESS ISSUE - SQL: Hard-coded database settings
    private static final int CONNECTION_TIMEOUT = 30000;
    private static final int SOCKET_TIMEOUT = 60000;
    
    // CLOUD READINESS ISSUE - SQL: No connection pooling (not cloud-ready)
    // Creates new connection for every query instead of using HikariCP or other pooling
    public static Connection getConnection() throws SQLException {
        // CLOUD READINESS ISSUE - SQL: Manual driver registration is obsolete since JDBC 4.0
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL driver not found on classpath", e);
        }
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
    }
    
    // CLOUD READINESS ISSUE - SQL: Hard-coded SQL with potential issues
    public static void executeDirectSQL(String customQuery) throws SQLException {
        Connection conn = getConnection();
        try {
            // CLOUD READINESS ISSUE: Direct SQL execution without parameterization
            // This is both a SQL injection risk and not cloud-ready
            Statement stmt = conn.createStatement();
            stmt.execute(customQuery);
            stmt.close();
        } finally {
            conn.close();
        }
    }
    
    // CLOUD READINESS ISSUE - SQL: Hard-coded database maintenance task
    // Not suitable for containerized/serverless environments
    public static void deleteOldRecords() throws SQLException {
        Connection conn = getConnection();
        try {
            // Hard-coded SQL with hard-coded retention period
            String deleteSQL = "DELETE FROM orders WHERE created_date < DATE_SUB(NOW(), INTERVAL 365 DAY)";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(deleteSQL);
            stmt.close();
        } finally {
            conn.close();
        }
    }
    
    // CLOUD READINESS ISSUE - SQL: No retry logic for transient cloud failures
    public static ResultSet queryDatabase(String query) throws SQLException {
        Connection conn = getConnection();
        Statement stmt = conn.createStatement();
        return stmt.executeQuery(query);
    }
    
    // CLOUD READINESS ISSUE - SQL: Hard-coded replication settings (not cloud-ready)
    private static final String REPLICA_DB_URL = "jdbc:mysql://legacy-db-replica.local:3306/inventory_db";
    
    public static Connection getReplicaConnection() throws SQLException {
        // CLOUD READINESS ISSUE - SQL: Manual driver registration is obsolete since JDBC 4.0
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL driver not found on classpath", e);
        }
        return DriverManager.getConnection(REPLICA_DB_URL, DB_USER, DB_PASSWORD);
    }
    
    // CLOUD READINESS ISSUE - SQL: Hard-coded stored procedure call
    // Stored procedures are database-specific and not portable across cloud providers
    public static void callLegacyStoredProcedure() throws SQLException {
        Connection conn = getConnection();
        try {
            CallableStatement cstmt = conn.prepareCall("{call sp_legacy_batch_process(?)}");
            cstmt.setInt(1, 1000); // Hard-coded batch size
            cstmt.execute();
            cstmt.close();
        } finally {
            conn.close();
        }
    }
    
    // CLOUD READINESS ISSUE - SQL: Hard-coded database backup path (local filesystem)
    public static String getDatabaseBackupPath() {
        return "C:\\legacy-backups\\db\\inventory_backup_";
    }
}
