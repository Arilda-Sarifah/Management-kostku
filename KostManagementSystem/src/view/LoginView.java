package view;

import controller.AuthController;
import model.User;
import util.ExceptionHandler;
import util.UIUtilities;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.geom.RoundRectangle2D;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginBtn, registerBtn;
    private JLabel errorLabel;
    private User currentUser;
    
    public LoginView() {
        setTitle("Kost Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(900, 600));
        setResizable(false);
        setUndecorated(true);
        
        initUI();
    }
    
    private void initUI() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIUtilities.LIGHT_BG);
        
        JPanel leftPanel = createLeftPanel();
        mainPanel.add(leftPanel, BorderLayout.WEST);
        
        JPanel rightWrapper = new JPanel(new GridBagLayout());
        rightWrapper.setOpaque(true);
        rightWrapper.setBackground(UIUtilities.LIGHT_BG);
        
        JPanel rightPanel = createRightPanel();
        rightWrapper.add(rightPanel);
        
        mainPanel.add(rightWrapper, BorderLayout.CENTER);
        
        setContentPane(mainPanel);
    }
    
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0, UIUtilities.PRIMARY_COLOR, getWidth(), getHeight(), UIUtilities.SECONDARY_COLOR);
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        panel.setPreferredSize(new Dimension(430, 600));
        panel.setLayout(new GridBagLayout());
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.insets = new Insets(30, 40, 30, 40);
        gbc.weightx = 1;
        gbc.weighty = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel titleLabel = new JLabel("KOST MANAGER");
        titleLabel.setFont(UIUtilities.FONT_TITLE);
        titleLabel.setForeground(Color.WHITE);
        panel.add(titleLabel, gbc);
        
        gbc.gridy = 1;
        JLabel subtitleLabel = new JLabel("Dashboard Manajemen Kost yang bersih dan modern");
        subtitleLabel.setFont(UIUtilities.FONT_REGULAR);
        subtitleLabel.setForeground(new Color(255, 255, 255, 190));
        panel.add(subtitleLabel, gbc);
        
        gbc.gridy = 2;
        gbc.insets = new Insets(20, 40, 12, 40);
        JLabel feature1 = new JLabel("• Kelola kamar dan penghuni");
        feature1.setFont(UIUtilities.FONT_SMALL);
        feature1.setForeground(Color.WHITE);
        panel.add(feature1, gbc);
        
        gbc.gridy = 3;
        JLabel feature2 = new JLabel("• Pantau pembayaran cepat");
        feature2.setFont(UIUtilities.FONT_SMALL);
        feature2.setForeground(Color.WHITE);
        panel.add(feature2, gbc);
        
        gbc.gridy = 4;
        JLabel feature3 = new JLabel("• Ekspor laporan sekali klik");
        feature3.setFont(UIUtilities.FONT_SMALL);
        feature3.setForeground(Color.WHITE);
        panel.add(feature3, gbc);
        
        return panel;
    }
    
    private JPanel createRightPanel() {
        JPanel wrapper = UIUtilities.createRoundedPanel(UIUtilities.CARD_BG, 24);
        wrapper.setPreferredSize(new Dimension(380, 520));
        wrapper.setLayout(new GridBagLayout());
        wrapper.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(12, 0, 12, 0);
        gbc.weightx = 1;
        
        JLabel welcomeLabel = new JLabel("Selamat Datang!");
        welcomeLabel.setFont(UIUtilities.FONT_TITLE);
        welcomeLabel.setForeground(UIUtilities.TEXT_PRIMARY);
        gbc.gridy = 0;
        wrapper.add(welcomeLabel, gbc);
        
        JLabel subtitleLabel = new JLabel("Masuk untuk mulai mengelola kost Anda");
        subtitleLabel.setFont(UIUtilities.FONT_REGULAR);
        subtitleLabel.setForeground(UIUtilities.TEXT_SECONDARY);
        gbc.gridy = 1;
        wrapper.add(subtitleLabel, gbc);
        
        errorLabel = new JLabel("");
        errorLabel.setFont(UIUtilities.FONT_SMALL);
        errorLabel.setForeground(UIUtilities.ERROR_COLOR);
        errorLabel.setVisible(false);
        gbc.gridy = 2;
        wrapper.add(errorLabel, gbc);
        
        gbc.anchor = GridBagConstraints.WEST;
        JLabel usernameLabel = new JLabel("Username");
        usernameLabel.setFont(UIUtilities.FONT_REGULAR);
        usernameLabel.setForeground(UIUtilities.TEXT_PRIMARY);
        gbc.gridy = 3;
        wrapper.add(usernameLabel, gbc);
        
        usernameField = UIUtilities.createModernTextField("");
        usernameField.setToolTipText("Masukkan username");
        gbc.gridy = 4;
        wrapper.add(usernameField, gbc);
        
        JLabel passwordLabel = new JLabel("Password");
        passwordLabel.setFont(UIUtilities.FONT_REGULAR);
        passwordLabel.setForeground(UIUtilities.TEXT_PRIMARY);
        gbc.gridy = 5;
        wrapper.add(passwordLabel, gbc);
        
        passwordField = UIUtilities.createModernPasswordField("");
        passwordField.setToolTipText("Masukkan password");
        gbc.gridy = 6;
        wrapper.add(passwordField, gbc);
        
        loginBtn = UIUtilities.createModernButton("LOGIN", UIUtilities.PRIMARY_COLOR, Color.WHITE);
        loginBtn.addActionListener(e -> handleLogin());
        gbc.gridy = 7;
        wrapper.add(loginBtn, gbc);
        
        JLabel dividerLabel = new JLabel("Belum punya akun?");
        dividerLabel.setFont(UIUtilities.FONT_SMALL);
        dividerLabel.setForeground(UIUtilities.TEXT_SECONDARY);
        dividerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridy = 8;
        wrapper.add(dividerLabel, gbc);
        
        registerBtn = UIUtilities.createModernButton("DAFTAR", UIUtilities.SECONDARY_COLOR, Color.WHITE);
        registerBtn.addActionListener(e -> handleRegister());
        gbc.gridy = 9;
        wrapper.add(registerBtn, gbc);
        
        return wrapper;
    }
    
    private void handleLogin() {
        try {
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword());
            
            // Validasi input
            if (!ExceptionHandler.validateInput(username, "Username", this)) return;
            if (!ExceptionHandler.validateInput(password, "Password", this)) return;
            
            // Tampilkan loading
            loginBtn.setEnabled(false);
            loginBtn.setText("Loading...");
            
            // Attempt login
            User user = AuthController.login(username, password);
            
            if (user != null) {
                currentUser = user;
                UIUtilities.showSuccessMessage(this, "Login Berhasil", 
                    "Selamat datang, " + user.getFullName() + "!");
                
                // Buka Dashboard
                new DashboardView(user).setVisible(true);
                this.dispose();
            } else {
                errorLabel.setText("Username atau password salah!");
                errorLabel.setVisible(true);
                passwordField.setText("");
            }
            
        } catch (IllegalArgumentException ex) {
            ExceptionHandler.handleException(ex, this);
        } catch (Exception ex) {
            ExceptionHandler.handleException(ex, this);
        } finally {
            loginBtn.setEnabled(true);
            loginBtn.setText("LOGIN");
        }
    }
    
    private void handleRegister() {
        new RegisterView(this).setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginView().setVisible(true));
    }
}
