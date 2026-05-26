import view.LoginView;
import model.DatabaseConnection;
import javax.swing.*;
import java.sql.SQLException;

public class MainApp {
    public static void main(String[] args) {
        // Initialize database tables
        try {
            DatabaseConnection.initializeTables();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                "Gagal menginisialisasi database: " + e.getMessage(),
                "Error Database", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Test koneksi database
        try {
            if (!DatabaseConnection.testConnection()) {
                JOptionPane.showMessageDialog(null,
                    "Gagal terhubung ke database!\n" +
                    "Pastikan MySQL sedang berjalan dan database sudah dibuat.",
                    "Error Koneksi", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                "Error: " + e.getMessage(),
                "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Jalankan aplikasi dengan Look and Feel sistem
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            LoginView loginView = new LoginView();
            loginView.pack();
            loginView.setLocationRelativeTo(null);
            loginView.setVisible(true);
        });
    }
}
