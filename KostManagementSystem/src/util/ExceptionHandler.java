package util;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExceptionHandler {
    private static final String LOG_FILE = "error_log.txt";
    
    /**
     * Handle exception umum
     */
    public static void handleException(Exception e, Component parent) {
        logError(e);
        
        String message = e.getMessage() != null ? e.getMessage() : "Terjadi kesalahan yang tidak diketahui";
        
        if (e instanceof java.sql.SQLException) {
            message = "Koneksi database gagal: " + e.getMessage();
        } else if (e instanceof IllegalArgumentException) {
            message = "Input tidak valid: " + e.getMessage();
        } else if (e instanceof NullPointerException) {
            message = "Data kosong atau tidak ditemukan";
        }
        
        UIUtilities.showErrorMessage(parent, "Error", message);
    }
    
    /**
     * Handle validation error
     */
    public static boolean validateInput(String input, String fieldName, Component parent) {
        if (input == null || input.trim().isEmpty()) {
            UIUtilities.showErrorMessage(parent, "Validation Error", fieldName + " tidak boleh kosong!");
            return false;
        }
        return true;
    }
    
    /**
     * Handle email validation
     */
    public static boolean validateEmail(String email, Component parent) {
        String emailPattern = "^[A-Za-z0-9+_.-]+@(.+)$";
        if (!email.matches(emailPattern)) {
            UIUtilities.showErrorMessage(parent, "Validation Error", "Email tidak valid!");
            return false;
        }
        return true;
    }
    
    /**
     * Handle password validation
     */
    public static boolean validatePassword(String password, Component parent) {
        if (password.length() < 6) {
            UIUtilities.showErrorMessage(parent, "Validation Error", "Password minimal 6 karakter!");
            return false;
        }
        return true;
    }
    
    /**
     * Log error ke file
     */
    private static void logError(Exception e) {
        try (FileWriter fw = new FileWriter(LOG_FILE, true);
             PrintWriter pw = new PrintWriter(fw)) {
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            pw.println("[" + sdf.format(new Date()) + "] ERROR: " + e.getMessage());
            e.printStackTrace(pw);
            pw.println("---");
            
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
    
    /**
     * Clear log file
     */
    public static void clearLog() {
        try (FileWriter fw = new FileWriter(LOG_FILE)) {
            fw.write("");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Get last error message
     */
    public static String getLastError() {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(LOG_FILE))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
