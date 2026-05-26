package controller;

import model.DatabaseConnection;
import model.User;
import util.ExceptionHandler;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AuthController {
    
    /**
     * Hash password menggunakan SHA-256
     */
    private static String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password; // Fallback jika hash gagal
        }
    }
    
    /**
     * Register user baru
     */
    public static boolean register(String username, String email, String password, String fullName) {
        // Validasi input
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("Email tidak boleh kosong");
        }
        if (password == null || password.length() < 6) {
            throw new IllegalArgumentException("Password minimal 6 karakter");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama lengkap tidak boleh kosong");
        }
        
        String sql = "INSERT INTO users (username, email, password, full_name, role) VALUES (?, ?, ?, ?, 'user')";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username.trim());
            ps.setString(2, email.trim());
            ps.setString(3, hashPassword(password));
            ps.setString(4, fullName.trim());
            
            int result = ps.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            if (e.getMessage().contains("Duplicate entry")) {
                throw new IllegalArgumentException("Username atau email sudah terdaftar");
            }
            throw new RuntimeException("Gagal mendaftar: " + e.getMessage(), e);
        }
    }
    
    /**
     * Login user
     */
    public static User login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("Username tidak boleh kosong");
        }
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("Password tidak boleh kosong");
        }
        
        String sql = "SELECT id, username, email, full_name, role FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username.trim());
            ps.setString(2, hashPassword(password));
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("full_name"),
                            rs.getString("role")
                    );
                    return user;
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Gagal login: " + e.getMessage(), e);
        }
        
        return null; // Login gagal
    }
    
    /**
     * Cek apakah username sudah ada
     */
    public static boolean usernameExists(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username.trim());
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengecek username: " + e.getMessage(), e);
        }
    }
    
    /**
     * Cek apakah email sudah ada
     */
    public static boolean emailExists(String email) {
        String sql = "SELECT id FROM users WHERE email = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email.trim());
            
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengecek email: " + e.getMessage(), e);
        }
    }
    
    /**
     * Update password user
     */
    public static boolean updatePassword(int userId, String newPassword) {
        if (newPassword == null || newPassword.length() < 6) {
            throw new IllegalArgumentException("Password minimal 6 karakter");
        }
        
        String sql = "UPDATE users SET password = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, hashPassword(newPassword));
            ps.setInt(2, userId);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Gagal update password: " + e.getMessage(), e);
        }
    }
    
    /**
     * Get user by ID
     */
    public static User getUserById(int userId) {
        String sql = "SELECT id, username, email, full_name, role FROM users WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("full_name"),
                            rs.getString("role")
                    );
                    return user;
                }
            }
            
        } catch (SQLException e) {
            throw new RuntimeException("Gagal mengambil data user: " + e.getMessage(), e);
        }
        
        return null;
    }
    
    /**
     * Update profile user
     */
    public static boolean updateProfile(int userId, String email, String fullName) {
        String sql = "UPDATE users SET email = ?, full_name = ? WHERE id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, email.trim());
            ps.setString(2, fullName.trim());
            ps.setInt(3, userId);
            
            return ps.executeUpdate() > 0;
            
        } catch (SQLException e) {
            throw new RuntimeException("Gagal update profile: " + e.getMessage(), e);
        }
    }
}
