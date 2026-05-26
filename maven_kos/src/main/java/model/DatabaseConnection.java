package model;

import java.sql.*;

public class DatabaseConnection {
    private static String URL = "jdbc:mysql://localhost:3306/db_kostku";
    private static String USER = "root";
    private static String PASSWORD = ""; // Ganti dengan password MySQL Anda atau gunakan config.properties

    static {
        // Baca konfigurasi dari config.properties jika tersedia
        java.util.Properties props = new java.util.Properties();
        java.io.File cfg = new java.io.File("config.properties");
        if (cfg.exists()) {
            try (java.io.FileReader fr = new java.io.FileReader(cfg)) {
                props.load(fr);
                URL = props.getProperty("db.url", URL);
                USER = props.getProperty("db.user", USER);
                PASSWORD = props.getProperty("db.password", PASSWORD);
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
    }
    
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver tidak ditemukan!", e);
        }
    }
    
    public static void closeConnection(Connection conn) {
        try {
            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    // Test koneksi
    public static boolean testConnection() {
        try (Connection conn = getConnection()) {
            return conn != null && !conn.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
    
    // Initialize database tables
    public static void initializeTables() throws SQLException {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // Create users table
            String createUsersTable = "CREATE TABLE IF NOT EXISTS users (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "username VARCHAR(50) UNIQUE NOT NULL," +
                    "email VARCHAR(100) UNIQUE NOT NULL," +
                    "password VARCHAR(255) NOT NULL," +
                    "full_name VARCHAR(100)," +
                    "role VARCHAR(20) DEFAULT 'user'," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            stmt.executeUpdate(createUsersTable);
            
            // Ensure other tables exist (if needed, add more CREATE TABLE statements here)
            String createKamarTable = "CREATE TABLE IF NOT EXISTS kamar (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY," +
                    "nomor_kamar VARCHAR(50) UNIQUE NOT NULL," +
                    "tipe VARCHAR(50)," +
                    "harga DECIMAL(10,2)," +
                    "status VARCHAR(20) DEFAULT 'KOSONG'," +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                    ")";
            stmt.executeUpdate(createKamarTable);
            
        } catch (SQLException e) {
            throw new SQLException("Gagal menginisialisasi tabel: " + e.getMessage(), e);
        }
    }
}