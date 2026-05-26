package view;

import controller.KamarController;
import controller.LaporanController;
import controller.PenghuniController;
import controller.ProfilKostController;
import model.User;
import util.UIUtilities;
import util.ExceptionHandler;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DashboardView extends JFrame {
    
    private JPanel contentPanel;
    private JLabel lblTotalKamar, lblKamarTerisi, lblKamarKosong, lblPenghuniAktif, lblPendapatan;
    private JLabel lblProfilNama, lblProfilAlamat, lblProfilPemilik, lblProfilKontak;
    private JLabel lblUsername;
    private CardLayout cardLayout;
    private User currentUser;
    private JLabel timeLabel;
    private Timer timer;
    
    public DashboardView(User user) {
        this.currentUser = user;
        setTitle("Sistem Manajemen Kost - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(1200, 700));
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        // Tambah menu bar untuk memenuhi materi Swing Menu
        setJMenuBar(createAppMenuBar());
        
        initUI();
        loadDashboardData();
        startClock();
    }

    private JMenuBar createAppMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu fileMenu = new JMenu("File");
        JMenuItem exportCsv = new JMenuItem("Export Laporan (CSV)");
        exportCsv.addActionListener(e -> {
            // Tampilkan panel laporan dan trigger ekspor
            try {
                this.showPanel("laporan");
                // cari LaporanView di contentPanel dan panggil aksi ekspor jika tersedia
                for (Component c : contentPanel.getComponents()) {
                    if (c instanceof view.LaporanView) {
                        ((view.LaporanView) c).exportCsvReport();
                        break;
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal export laporan: " + ex.getMessage());
            }
        });
        JMenuItem initDb = new JMenuItem("Initialize DB");
        initDb.addActionListener(e -> {
            try {
                model.DatabaseConnection.initializeTables();
                JOptionPane.showMessageDialog(this, "Inisialisasi tabel selesai.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Gagal inisialisasi DB: " + ex.getMessage());
            }
        });
        JMenuItem exit = new JMenuItem("Exit");
        exit.addActionListener(e -> dispose());
        fileMenu.add(exportCsv);
        fileMenu.add(initDb);
        fileMenu.addSeparator();
        fileMenu.add(exit);

        JMenu helpMenu = new JMenu("Help");
        JMenuItem about = new JMenuItem("About");
        about.addActionListener(e -> JOptionPane.showMessageDialog(this, "Sistem Manajemen Kost\nImplementasi materi PBO."));
        helpMenu.add(about);

        menuBar.add(fileMenu);
        menuBar.add(helpMenu);
        return menuBar;
    }
    
    private void initUI() {
        setLayout(new BorderLayout());
        
        JPanel topBar = createTopBar();
        add(topBar, BorderLayout.NORTH);
        
        JPanel sidebar = createSidebar();
        add(sidebar, BorderLayout.WEST);
        
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(UIUtilities.LIGHT_BG);
        
        contentPanel.add(createDashboardPanel(), "dashboard");
        contentPanel.add(new KamarView(this), "kamar");
        contentPanel.add(new PenghuniView(this), "penghuni");
        contentPanel.add(new PembayaranView(this), "pembayaran");
        contentPanel.add(new ProfilKostView(this), "profil");
        contentPanel.add(new PengeluaranView(this), "keuangan");
        contentPanel.add(new LaporanView(this), "laporan");
        
        add(contentPanel, BorderLayout.CENTER);
        
        JPanel statusBar = createStatusBar();
        add(statusBar, BorderLayout.SOUTH);
    }
    
    private JPanel createTopBar() {
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setPreferredSize(new Dimension(getWidth(), 70));
        topBar.setBackground(UIUtilities.CARD_BG);
        topBar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, UIUtilities.BORDER_COLOR));
        
        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 18));
        logoPanel.setOpaque(false);
        JLabel logoIcon = new JLabel(UIUtilities.loadIcon("dashboard.png", 26, 26));
        JLabel logoLabel = new JLabel("Kost Manager");
        logoLabel.setFont(UIUtilities.FONT_SUBTITLE);
        logoLabel.setForeground(UIUtilities.TEXT_PRIMARY);
        logoPanel.add(logoIcon);
        logoPanel.add(logoLabel);
        topBar.add(logoPanel, BorderLayout.WEST);
        
        JPanel userPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 16, 18));
        userPanel.setOpaque(false);
        JLabel welcomeLabel = new JLabel("Selamat datang,");
        welcomeLabel.setFont(UIUtilities.FONT_REGULAR);
        welcomeLabel.setForeground(UIUtilities.TEXT_SECONDARY);
        userPanel.add(welcomeLabel);
        
        lblUsername = new JLabel(currentUser.getFullName());
        lblUsername.setFont(UIUtilities.FONT_HEADING);
        lblUsername.setForeground(UIUtilities.TEXT_PRIMARY);
        userPanel.add(lblUsername);
        
        JButton logoutBtn = UIUtilities.createModernButton("Logout", UIUtilities.SECONDARY_COLOR, Color.WHITE);
        logoutBtn.setPreferredSize(new Dimension(100, 36));
        logoutBtn.addActionListener(e -> handleLogout());
        userPanel.add(logoutBtn);
        
        topBar.add(userPanel, BorderLayout.EAST);
        
        return topBar;
    }
    
    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(UIUtilities.DARK_BG);
        sidebar.setPreferredSize(new Dimension(260, getHeight()));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(24, 16, 24, 16));
        
        JLabel navTitle = new JLabel("Menu Utama");
        navTitle.setFont(UIUtilities.FONT_SUBTITLE);
        navTitle.setForeground(Color.WHITE);
        navTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        sidebar.add(navTitle);
        sidebar.add(Box.createVerticalStrut(20));
        
        String[] iconFiles = {"dashboard.png", "home.png", "bed.png", "multiple-users-silhouette.png", "payment-method.png", "save-money.png", "report.png"};
        String[] menus = {"Dashboard", "Profil Kost", "Kamar", "Penghuni", "Pembayaran", "Keuangan", "Laporan"};
        String[] cardNames = {"dashboard", "profil", "kamar", "penghuni", "pembayaran", "keuangan", "laporan"};
        
        for (int i = 0; i < menus.length; i++) {
            JButton menuBtn = createMenuButton(menus[i], UIUtilities.loadIcon(iconFiles[i], 20, 20), cardNames[i]);
            sidebar.add(menuBtn);
            sidebar.add(Box.createVerticalStrut(8));
        }
        
        sidebar.add(Box.createVerticalGlue());
        
        JButton exitBtn = UIUtilities.createModernButton("Logout", UIUtilities.ERROR_COLOR, Color.WHITE);
        exitBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        exitBtn.setMaximumSize(new Dimension(220, 44));
        exitBtn.addActionListener(e -> handleLogout());
        sidebar.add(exitBtn);
        sidebar.add(Box.createVerticalStrut(12));
        
        return sidebar;
    }
    
    private JButton createMenuButton(String text, ImageIcon icon, String cardName) {
        JButton btn = new JButton(text, icon) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                if (getModel().isRollover()) {
                    g2.setColor(UIUtilities.SECONDARY_COLOR);
                } else {
                    g2.setColor(UIUtilities.DARK_BG);
                }
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 14, 14);
                
                super.paintComponent(g);
            }
        };
        
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(220, 46));
        btn.setFocusPainted(false);
        btn.setBackground(UIUtilities.DARK_BG);
        btn.setForeground(Color.WHITE);
        btn.setFont(UIUtilities.FONT_REGULAR);
        btn.setBorder(BorderFactory.createEmptyBorder(12, 18, 12, 18));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(12);
        btn.addActionListener(e -> {
            cardLayout.show(contentPanel, cardName);
            if (cardName.equals("dashboard")) {
                setTitle("Sistem Manajemen Kost - Dashboard");
            } else {
                setTitle("Sistem Manajemen Kost - " + text.trim());
            }
        });
        
        return btn;
    }
    
    private JPanel createDashboardPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UIUtilities.LIGHT_BG);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 25, 25, 25));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));
        
        JLabel header = new JLabel("Dashboard Overview");
        header.setFont(UIUtilities.FONT_TITLE);
        header.setForeground(UIUtilities.TEXT_PRIMARY);
        headerPanel.add(header, BorderLayout.WEST);
        
        JLabel dateLabel = new JLabel("📅 " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy", new Locale("id", "ID"))));
        dateLabel.setFont(UIUtilities.FONT_SMALL);
        dateLabel.setForeground(UIUtilities.TEXT_SECONDARY);
        headerPanel.add(dateLabel, BorderLayout.EAST);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // Content panel dengan GridLayout untuk card statistik
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridBagLayout());
        centerPanel.setBackground(UIUtilities.LIGHT_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        
        // Baris 1: Statistik Utama
        lblTotalKamar = createValueLabel("0");
        gbc.gridx = 0;
        gbc.gridy = 0;
        centerPanel.add(createDashboardCard(" Total Kamar", lblTotalKamar, new Color(66, 133, 244), UIUtilities.loadIcon("bed.png", 28, 28), "Semua kamar yang tersedia"), gbc);
        
        lblKamarTerisi = createValueLabel("0");
        gbc.gridx = 1;
        centerPanel.add(createDashboardCard(" Kamar Terisi", lblKamarTerisi, new Color(52, 168, 83), UIUtilities.loadIcon("bed.png", 28, 28), "Kamar yang sudah dihuni"), gbc);
        
        lblKamarKosong = createValueLabel("0");
        gbc.gridx = 2;
        centerPanel.add(createDashboardCard(" Kamar Kosong", lblKamarKosong, new Color(251, 188, 5), UIUtilities.loadIcon("bed.png", 28, 28), "Kamar yang tersedia"), gbc);
        
        lblPenghuniAktif = createValueLabel("0");
        gbc.gridx = 3;
        centerPanel.add(createDashboardCard(" Penghuni Aktif", lblPenghuniAktif, new Color(234, 67, 53), UIUtilities.loadIcon("multiple-users-silhouette.png", 28, 28), "Jumlah penghuni saat ini"), gbc);
        
        // Baris 2: Keuangan & Status
        lblPendapatan = createValueLabel("Rp 0");
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        centerPanel.add(createDashboardCard(" Pendapatan Bulan Ini", lblPendapatan, new Color(139, 195, 74), UIUtilities.loadIcon("save-money.png", 28, 28), "Total pendapatan bulan berjalan"), gbc);
        
        JLabel lblStatus = new JLabel("● Online");
        lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblStatus.setForeground(new Color(52, 168, 83));
        gbc.gridx = 2;
        gbc.gridwidth = 2;
        centerPanel.add(createDashboardCard(" Status Sistem", lblStatus, new Color(52, 168, 83), UIUtilities.loadIcon("dashboard.png", 28, 28), "Koneksi database aktif"), gbc);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Panel Informasi Tipe Kamar (di bagian bawah)
        JPanel infoPanel = createRoomTypeInfoPanel();
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        
        return mainPanel;
    }
    
    private JLabel createValueLabel(String initialValue) {
        JLabel label = new JLabel(initialValue);
        label.setFont(new Font("Segoe UI", Font.BOLD, 28));
        label.setForeground(UIUtilities.TEXT_PRIMARY);
        return label;
    }
    
    private JPanel createDashboardCard(String title, JLabel valueLabel, Color accentColor, Icon icon, String description) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UIUtilities.CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 16, 16);
                
                // Left accent border
                g2.setColor(accentColor);
                g2.fillRoundRect(0, 0, 6, getHeight(), 3, 3);
                
                g2.setColor(UIUtilities.BORDER_COLOR);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 16, 16);
            }
        };
        
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));
        card.setOpaque(false);
        
        // Icon dan title dalam satu baris
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel iconLabel = new JLabel(icon);
        headerPanel.add(iconLabel, BorderLayout.WEST);
        
        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(UIUtilities.FONT_SMALL);
        titleLabel.setForeground(UIUtilities.TEXT_SECONDARY);
        headerPanel.add(titleLabel, BorderLayout.CENTER);
        
        card.add(headerPanel);
        card.add(Box.createVerticalStrut(12));
        card.add(valueLabel);
        card.add(Box.createVerticalStrut(8));
        
        // Tooltip description
        card.setToolTipText(description);
        
        return card;
    }
    
    private JPanel createRoomTypeInfoPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(UIUtilities.LIGHT_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(1, 0, 0, 0, UIUtilities.BORDER_COLOR),
            BorderFactory.createEmptyBorder(20, 0, 0, 0)
        ));
        
        // Header
        JLabel infoTitle = new JLabel("Informasi Tipe Kamar", UIUtilities.loadIcon("report.png", 18, 18), JLabel.LEFT);
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        infoTitle.setForeground(UIUtilities.TEXT_PRIMARY);
        infoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoTitle.setIconTextGap(8);
        panel.add(infoTitle);
        panel.add(Box.createVerticalStrut(12));
        
        // Cards untuk tipe kamar
        JPanel cardsPanel = new JPanel(new GridLayout(1, 3, 15, 0));
        cardsPanel.setBackground(UIUtilities.LIGHT_BG);
        cardsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        cardsPanel.add(createRoomTypeCard(
            "Tipe A", 
            "🌟 Premium", 
            new Color(66, 133, 244),
            new String[]{"✓ AC", "✓ Kamar Mandi Dalam", "✓ Kasur", "✓ Lemari", "✓ Meja Belajar"},
            "Rp 1.500.000"
        ));
        
        cardsPanel.add(createRoomTypeCard(
            "Tipe B", 
            "💎 Standar", 
            new Color(52, 168, 83),
            new String[]{"✓ Kasur", "✓ Kamar Mandi Dalam", "✓ Lemari"},
            "Rp 1.000.000"
        ));
        
        cardsPanel.add(createRoomTypeCard(
            "Tipe C", 
            "📦 Ekonomi", 
            new Color(251, 188, 5),
            new String[]{"✓ Kasur", "✓ Lemari", "✓ Kamar Mandi Luar (Umum)"},
            "Rp 500.000"
        ));
        
        panel.add(cardsPanel);
        
        return panel;
    }
    
    private JPanel createRoomTypeCard(String type, String subType, Color color, String[] facilities, String price) {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UIUtilities.CARD_BG);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                
                // Top border accent
                g2.setColor(color);
                g2.fillRoundRect(0, 0, getWidth(), 5, 5, 5);
                
                g2.setColor(UIUtilities.BORDER_COLOR);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
            }
        };
        
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        card.setOpaque(false);
        card.setPreferredSize(new Dimension(0, 180));
        
        // Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        JLabel typeLabel = new JLabel(type);
        typeLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        typeLabel.setForeground(color);
        headerPanel.add(typeLabel, BorderLayout.WEST);
        
        JLabel subLabel = new JLabel(subType);
        subLabel.setFont(UIUtilities.FONT_SMALL);
        subLabel.setForeground(UIUtilities.TEXT_SECONDARY);
        headerPanel.add(subLabel, BorderLayout.EAST);
        
        card.add(headerPanel);
        card.add(Box.createVerticalStrut(10));
        
        // Facilities
        for (String facility : facilities) {
            JLabel facilityLabel = new JLabel(facility);
            facilityLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            facilityLabel.setForeground(UIUtilities.TEXT_SECONDARY);
            facilityLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
            card.add(facilityLabel);
            card.add(Box.createVerticalStrut(4));
        }
        
        card.add(Box.createVerticalStrut(8));
        
        // Price
        JPanel pricePanel = new JPanel(new BorderLayout());
        pricePanel.setOpaque(false);
        pricePanel.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIUtilities.BORDER_COLOR));
        
        JLabel priceLabel = new JLabel(price);
        priceLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        priceLabel.setForeground(color);
        priceLabel.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));
        priceLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        card.add(priceLabel);
        
        return card;
    }
    
    private JPanel createStatusBar() {
        JPanel statusBar = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        statusBar.setBackground(UIUtilities.DARK_BG);
        
        JLabel statusLabel = new JLabel("✅ Status: Terhubung ke database MySQL");
        statusLabel.setFont(UIUtilities.FONT_SMALL);
        statusLabel.setForeground(UIUtilities.SUCCESS_COLOR);
        statusBar.add(statusLabel);
        
        timeLabel = new JLabel("⏰ " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
        timeLabel.setFont(UIUtilities.FONT_SMALL);
        timeLabel.setForeground(Color.WHITE);
        statusBar.add(timeLabel);
        
        JLabel versionLabel = new JLabel("📌 Version 2.0");
        versionLabel.setFont(UIUtilities.FONT_SMALL);
        versionLabel.setForeground(UIUtilities.TEXT_SECONDARY);
        statusBar.add(versionLabel);
        
        return statusBar;
    }
    
    private void startClock() {
        timer = new Timer(1000, e -> {
            if (timeLabel != null) {
                timeLabel.setText("⏰ " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss")));
            }
        });
        timer.start();
    }
    
    public void loadDashboardData() {
        try {
            // Load data kamar
            java.util.List<?> allKamar = KamarController.getAllKamar();
            int totalKamar = allKamar != null ? allKamar.size() : 0;
            java.util.List<?> kamarKosongList = KamarController.getKamarKosong();
            int kamarKosong = kamarKosongList != null ? kamarKosongList.size() : 0;
            int kamarTerisi = Math.max(0, totalKamar - kamarKosong);

            lblTotalKamar.setText(String.valueOf(totalKamar));
            lblKamarTerisi.setText(String.valueOf(kamarTerisi));
            lblKamarKosong.setText(String.valueOf(kamarKosong));
            
            // Load data penghuni
            java.util.List<?> penghuniAktifList = PenghuniController.getPenghuniAktif();
            int penghuniAktif = penghuniAktifList != null ? penghuniAktifList.size() : 0;
            lblPenghuniAktif.setText(String.valueOf(penghuniAktif));
            
            // Load data pendapatan
            double pendapatan = LaporanController.getTotalPendapatanBulanIni();
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
            lblPendapatan.setText(formatter.format(pendapatan));
            
        } catch (Exception ex) {
            ExceptionHandler.handleException(ex, this);
            // Set default values
            lblTotalKamar.setText("0");
            lblKamarTerisi.setText("0");
            lblKamarKosong.setText("0");
            lblPenghuniAktif.setText("0");
            lblPendapatan.setText("Rp 0");
        }
    }
    
    private void handleLogout() {
        int result = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin logout?", 
            "Konfirmasi Logout", 
            JOptionPane.YES_NO_OPTION);
        
        if (result == JOptionPane.YES_OPTION) {
            if (timer != null) {
                timer.stop();
            }
            this.dispose();
            new LoginView().setVisible(true);
        }
    }
    
    // Getter methods untuk digunakan oleh view lain
    public CardLayout getCardLayout() {
        return cardLayout;
    }
    
    public JPanel getContentPanel() {
        return contentPanel;
    }
    
    public void showPanel(String panelName) {
        cardLayout.show(contentPanel, panelName);
    }
}