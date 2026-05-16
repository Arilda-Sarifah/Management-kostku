package view;

import controller.AuthController;
import util.ExceptionHandler;
import util.UIUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class RegisterView extends JDialog {
    private JTextField usernameField, emailField, fullNameField;
    private JPasswordField passwordField, confirmPasswordField;
    private JButton registerBtn, cancelBtn;
    private JLabel errorLabel;
    private JFrame parentFrame;
    
    public RegisterView(JFrame parentFrame) {
        super(parentFrame, "Daftar Akun Baru", true);
        this.parentFrame = parentFrame;
        
        setSize(500, 600);
        setLocationRelativeTo(parentFrame);
        setResizable(false);
        
        initUI();
    }
    
    private void initUI() {
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(UIUtilities.LIGHT_BG);
        
        JPanel cardPanel = UIUtilities.createRoundedPanel(UIUtilities.CARD_BG, 22);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        cardPanel.setLayout(new GridBagLayout());
        cardPanel.setPreferredSize(new Dimension(450, 560));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        
        JLabel titleLabel = new JLabel("Buat Akun Baru");
        titleLabel.setFont(UIUtilities.FONT_SUBTITLE);
        titleLabel.setForeground(UIUtilities.TEXT_PRIMARY);
        gbc.gridy = 0;
        gbc.insets = new Insets(0, 0, 20, 0);
        cardPanel.add(titleLabel, gbc);
        
        errorLabel = new JLabel("");
        errorLabel.setFont(UIUtilities.FONT_SMALL);
        errorLabel.setForeground(UIUtilities.ERROR_COLOR);
        errorLabel.setVisible(false);
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 12, 0);
        cardPanel.add(errorLabel, gbc);
        
        JLabel fullNameLabel = new JLabel("Nama Lengkap");
        fullNameLabel.setFont(UIUtilities.FONT_REGULAR);
        fullNameLabel.setForeground(UIUtilities.TEXT_PRIMARY);
        gbc.gridy = 2;
        cardPanel.add(fullNameLabel, gbc);
        
        fullNameField = UIUtilities.createModernTextField("");
        fullNameField.setToolTipText("Masukkan nama lengkap");
        gbc.gridy = 3;
        cardPanel.add(fullNameField, gbc);
        
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(UIUtilities.FONT_REGULAR);
        usernameLabel.setForeground(UIUtilities.TEXT_PRIMARY);
        gbc.gridy = 4;
        cardPanel.add(usernameLabel, gbc);
        
        usernameField = UIUtilities.createModernTextField("");
        usernameField.setToolTipText("Masukkan username");
        gbc.gridy = 5;
        cardPanel.add(usernameField, gbc);
        
        JLabel emailLabel = new JLabel("Email");
        emailLabel.setFont(UIUtilities.FONT_REGULAR);
        emailLabel.setForeground(UIUtilities.TEXT_PRIMARY);
        gbc.gridy = 6;
        cardPanel.add(emailLabel, gbc);
        
        emailField = UIUtilities.createModernTextField("");
        emailField.setToolTipText("Masukkan email");
        gbc.gridy = 7;
        cardPanel.add(emailField, gbc);
        
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(UIUtilities.FONT_REGULAR);
        passwordLabel.setForeground(UIUtilities.TEXT_PRIMARY);
        gbc.gridy = 8;
        cardPanel.add(passwordLabel, gbc);
        
        passwordField = UIUtilities.createModernPasswordField("");
        passwordField.setToolTipText("Minimal 6 karakter");
        gbc.gridy = 9;
        cardPanel.add(passwordField, gbc);
        
        JLabel confirmLabel = new JLabel("Konfirmasi Password");
        confirmLabel.setFont(UIUtilities.FONT_REGULAR);
        confirmLabel.setForeground(UIUtilities.TEXT_PRIMARY);
        gbc.gridy = 10;
        cardPanel.add(confirmLabel, gbc);
        
        confirmPasswordField = UIUtilities.createModernPasswordField("");
        confirmPasswordField.setToolTipText("Ulangi password");
        gbc.gridy = 11;
        gbc.insets = new Insets(12, 0, 24, 0);
        cardPanel.add(confirmPasswordField, gbc);
        
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 0));
        buttonsPanel.setOpaque(false);
        
        cancelBtn = UIUtilities.createModernButton("BATAL", UIUtilities.MUTED_COLOR, UIUtilities.TEXT_PRIMARY);
        cancelBtn.setPreferredSize(new Dimension(150, 45));
        cancelBtn.addActionListener(e -> dispose());
        buttonsPanel.add(cancelBtn);
        
        registerBtn = UIUtilities.createModernButton("DAFTAR", UIUtilities.PRIMARY_COLOR, Color.WHITE);
        registerBtn.setPreferredSize(new Dimension(150, 45));
        registerBtn.addActionListener(e -> handleRegister());
        buttonsPanel.add(registerBtn);
        
        gbc.gridy = 12;
        gbc.insets = new Insets(0, 0, 0, 0);
        cardPanel.add(buttonsPanel, gbc);
        
        mainPanel.add(cardPanel);
        setContentPane(mainPanel);
    }
    
    private void handleRegister() {
        try {
            String fullName = fullNameField.getText().trim();
            String username = usernameField.getText().trim();
            String email = emailField.getText().trim();
            String password = new String(passwordField.getPassword());
            String confirmPassword = new String(confirmPasswordField.getPassword());
            
            // Validasi
            if (!ExceptionHandler.validateInput(fullName, "Nama Lengkap", this)) return;
            if (!ExceptionHandler.validateInput(username, "Username", this)) return;
            if (!ExceptionHandler.validateInput(email, "Email", this)) return;
            if (!ExceptionHandler.validateEmail(email, this)) return;
            if (!ExceptionHandler.validatePassword(password, this)) return;
            
            if (!password.equals(confirmPassword)) {
                UIUtilities.showErrorMessage(this, "Error", "Password tidak cocok!");
                confirmPasswordField.setText("");
                return;
            }
            
            // Cek username dan email
            if (AuthController.usernameExists(username)) {
                UIUtilities.showErrorMessage(this, "Error", "Username sudah terdaftar!");
                return;
            }
            
            if (AuthController.emailExists(email)) {
                UIUtilities.showErrorMessage(this, "Error", "Email sudah terdaftar!");
                return;
            }
            
            registerBtn.setEnabled(false);
            registerBtn.setText("Mendaftar...");
            
            // Attempt register
            boolean success = AuthController.register(username, email, password, fullName);
            
            if (success) {
                UIUtilities.showSuccessMessage(this, "Sukses", 
                    "Akun berhasil dibuat! Silakan login dengan username dan password Anda.");
                dispose();
            }
            
        } catch (IllegalArgumentException ex) {
            errorLabel.setText(ex.getMessage());
            errorLabel.setVisible(true);
        } catch (Exception ex) {
            ExceptionHandler.handleException(ex, this);
        } finally {
            registerBtn.setEnabled(true);
            registerBtn.setText("DAFTAR");
        }
    }
}
