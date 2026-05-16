package view;

import controller.KamarController;
import model.Kamar;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import util.UIUtilities;

public class KamarView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNomor, txtTipe, txtHarga, txtCari;
    private JButton btnTambah, btnEdit, btnHapus, btnRefresh, btnCari;
    private int selectedId = -1;
    private DashboardView dashboard;
    private JComboBox<String> cmbTipe;
    
    public KamarView(DashboardView dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponents();
        loadTable();
    }
    
    private void initComponents() {
        // ========== PANEL UTAMA DENGAN BORDERLAYOUT ==========
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setOpaque(false);
        
        // ========== HEADER PANEL ==========
        JPanel headerPanel = UIUtilities.createCardPanel(20);
        headerPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel title = new JLabel("Manajemen Kamar");
        title.setFont(UIUtilities.FONT_SUBTITLE);
        title.setForeground(UIUtilities.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        headerPanel.add(title, gbc);

        JLabel subtitle = new JLabel("Kelola data kamar kost dengan mudah");
        subtitle.setFont(UIUtilities.FONT_REGULAR);
        subtitle.setForeground(UIUtilities.TEXT_SECONDARY);
        gbc.gridy = 1;
        headerPanel.add(subtitle, gbc);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 8));
        searchPanel.setOpaque(false);
        
        txtCari = UIUtilities.createModernTextField("");
        txtCari.setPreferredSize(new Dimension(250, 38));
        txtCari.setToolTipText("Cari berdasarkan nomor kamar atau tipe");
        txtCari.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        
        btnCari = UIUtilities.createModernButton("🔍 Cari", UIUtilities.SECONDARY_COLOR, Color.WHITE);
        btnRefresh = UIUtilities.createModernButton("⟳ Refresh", UIUtilities.ACCENT_COLOR, Color.WHITE);
        
        searchPanel.add(new JLabel("Cari:"));
        searchPanel.add(txtCari);
        searchPanel.add(btnCari);
        searchPanel.add(btnRefresh);

        gbc.gridy = 2;
        headerPanel.add(searchPanel, gbc);
        
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        
        // ========== PANEL TENGAH (TABEL + FORM) ==========
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 15, 0));
        centerPanel.setOpaque(false);
        
        // ========== 1. PANEL TABEL (KIRI) ==========
        String[] columns = {"ID", "Nomor Kamar", "Tipe Kamar", "Harga/Bulan", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        // ========== PERBAIKAN: Header tabel dengan background abu-abu dan font coklat tua ==========
        Color darkBrown = new Color(92, 64, 51); // Coklat tua
        Color lightBrown = new Color(160, 120, 100); // Coklat muda untuk background
        
        table.getTableHeader().setBackground(new Color(245, 240, 235)); // Background krem/putih keabuan
        table.getTableHeader().setForeground(darkBrown); // Font coklat tua
        table.getTableHeader().setOpaque(true);
        
        // Setting untuk header yang lebih jelas
        javax.swing.table.JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(245, 240, 235)); // Background abu-abu muda/krem
        header.setForeground(darkBrown); // Font coklat tua
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(35);
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));
        
        // Set lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(120);
        table.getColumnModel().getColumn(2).setPreferredWidth(130);
        table.getColumnModel().getColumn(3).setPreferredWidth(130);
        table.getColumnModel().getColumn(4).setPreferredWidth(90);
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        
        JPanel tableCard = UIUtilities.createCardPanel(15);
        tableCard.setLayout(new BorderLayout(0, 10));
        JLabel tableTitle = new JLabel("Daftar Kamar", UIUtilities.loadIcon("bed.png", 18, 18), JLabel.LEFT);
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableTitle.setForeground(UIUtilities.TEXT_PRIMARY);
        tableTitle.setIconTextGap(8);
        tableCard.add(tableTitle, BorderLayout.NORTH);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        
        centerPanel.add(tableCard);
        
        // ========== 2. PANEL FORM INPUT (KANAN) DENGAN SCROLL ==========
        JPanel formCard = UIUtilities.createCardPanel(15);
        formCard.setLayout(new BorderLayout());
        
        // Panel utama form dengan GridBagLayout
        JPanel formContent = new JPanel(new GridBagLayout());
        formContent.setOpaque(false);
        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.insets = new Insets(8, 12, 8, 12);
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        fgbc.weightx = 1;
        
        // Title Form
        JLabel formTitle = new JLabel("✏️ Form Kamar");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        formTitle.setForeground(UIUtilities.TEXT_PRIMARY);
        fgbc.gridx = 0;
        fgbc.gridy = 0;
        fgbc.gridwidth = 2;
        formContent.add(formTitle, fgbc);
        
        // Separator
        JSeparator separator = new JSeparator();
        fgbc.gridy = 1;
        formContent.add(separator, fgbc);
        
        fgbc.gridwidth = 1;
        
        // Nomor Kamar
        fgbc.gridx = 0;
        fgbc.gridy = 2;
        fgbc.weightx = 0.3;
        JLabel lblNomor = new JLabel("Nomor Kamar:");
        lblNomor.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formContent.add(lblNomor, fgbc);
        
        fgbc.gridx = 1;
        fgbc.weightx = 0.7;
        txtNomor = UIUtilities.createModernTextField("");
        txtNomor.setToolTipText("Contoh: A101, B202, C303");
        txtNomor.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtNomor.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formContent.add(txtNomor, fgbc);
        
        // Tipe Kamar
        fgbc.gridx = 0;
        fgbc.gridy = 3;
        JLabel lblTipe = new JLabel("Tipe Kamar:");
        lblTipe.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formContent.add(lblTipe, fgbc);
        
        fgbc.gridx = 1;
        String[] tipeKamar = {"Pilih Tipe", "Tipe A (Premium)", "Tipe B (Standar)", "Tipe C (Ekonomi)"};
        cmbTipe = new JComboBox<>(tipeKamar);
        cmbTipe.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbTipe.setPreferredSize(new Dimension(200, 40));
        cmbTipe.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        txtTipe = new JTextField();
        txtTipe.setVisible(false);
        formContent.add(cmbTipe, fgbc);
        
        // Harga/Bulan
        fgbc.gridx = 0;
        fgbc.gridy = 4;
        JLabel lblHarga = new JLabel("Harga/Bulan:");
        lblHarga.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formContent.add(lblHarga, fgbc);
        
        fgbc.gridx = 1;
        txtHarga = UIUtilities.createModernTextField("");
        txtHarga.setEditable(false);
        txtHarga.setBackground(new Color(250, 250, 250));
        txtHarga.setToolTipText("Harga akan terisi otomatis berdasarkan tipe kamar");
        txtHarga.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(150, 150, 150), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtHarga.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formContent.add(txtHarga, fgbc);
        
        // Info harga
        JPanel infoHargaPanel = new JPanel();
        infoHargaPanel.setLayout(new BoxLayout(infoHargaPanel, BoxLayout.Y_AXIS));
        infoHargaPanel.setOpaque(false);
        infoHargaPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));
        infoHargaPanel.setBackground(new Color(248, 248, 248));
        
        JLabel infoTitle = new JLabel("📌 Info Harga Kamar:");
        infoTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        infoTitle.setForeground(UIUtilities.TEXT_PRIMARY);
        infoTitle.setAlignmentX(Component.LEFT_ALIGNMENT);
        infoHargaPanel.add(infoTitle);
        infoHargaPanel.add(Box.createVerticalStrut(8));
        
        JLabel infoA = new JLabel("• Tipe A (Premium) : Rp 1.500.000 / bulan");
        infoA.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoA.setForeground(new Color(66, 133, 244));
        infoA.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel infoB = new JLabel("• Tipe B (Standar) : Rp 1.000.000 / bulan");
        infoB.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoB.setForeground(new Color(52, 168, 83));
        infoB.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        JLabel infoC = new JLabel("• Tipe C (Ekonomi) : Rp 500.000 / bulan");
        infoC.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        infoC.setForeground(new Color(251, 188, 5));
        infoC.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        infoHargaPanel.add(infoA);
        infoHargaPanel.add(Box.createVerticalStrut(4));
        infoHargaPanel.add(infoB);
        infoHargaPanel.add(Box.createVerticalStrut(4));
        infoHargaPanel.add(infoC);
        
        fgbc.gridx = 0;
        fgbc.gridy = 5;
        fgbc.gridwidth = 2;
        fgbc.insets = new Insets(10, 12, 10, 12);
        formContent.add(infoHargaPanel, fgbc);
        
        // Button Panel
        fgbc.gridx = 0;
        fgbc.gridy = 6;
        fgbc.gridwidth = 2;
        fgbc.insets = new Insets(20, 12, 15, 12);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        
        btnTambah = UIUtilities.createModernButton("➕ Tambah", new Color(46, 125, 50), Color.WHITE);
        btnEdit = UIUtilities.createModernButton("✏️ Edit", new Color(25, 118, 210), Color.WHITE);
        btnHapus = UIUtilities.createModernButton("🗑️ Hapus", new Color(198, 40, 40), Color.WHITE);
        
        btnTambah.setPreferredSize(new Dimension(120, 42));
        btnEdit.setPreferredSize(new Dimension(120, 42));
        btnHapus.setPreferredSize(new Dimension(120, 42));
        
        btnTambah.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnHapus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnHapus);
        formContent.add(buttonPanel, fgbc);
        
        // Bungkus formContent dengan JScrollPane
        JScrollPane formScrollPane = new JScrollPane(formContent);
        formScrollPane.setBorder(null);
        formScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        formScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        formCard.add(formScrollPane, BorderLayout.CENTER);
        
        centerPanel.add(formCard);
        
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        
        // Footer
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        footerPanel.setOpaque(false);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));
        JLabel footerLabel = new JLabel("💡 Tip: Klik baris pada tabel untuk mengisi form");
        footerLabel.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        footerLabel.setForeground(UIUtilities.TEXT_SECONDARY);
        footerPanel.add(footerLabel);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);
        
        add(mainPanel, BorderLayout.CENTER);
        
        // ========== EVENT LISTENERS ==========
        btnTambah.addActionListener(e -> tambahKamar());
        btnEdit.addActionListener(e -> editKamar());
        btnHapus.addActionListener(e -> hapusKamar());
        btnRefresh.addActionListener(e -> {
            loadTable();
            txtCari.setText("");
            clearForm();
            cmbTipe.setSelectedIndex(0);
        });
        btnCari.addActionListener(e -> cariKamar());
        
        txtCari.addActionListener(e -> cariKamar());
        
        // Auto fill harga
        cmbTipe.addActionListener(e -> {
            String selected = (String) cmbTipe.getSelectedItem();
            if (selected != null) {
                if (selected.equals("Tipe A (Premium)")) {
                    txtHarga.setText("1500000");
                    txtTipe.setText("Tipe A");
                } else if (selected.equals("Tipe B (Standar)")) {
                    txtHarga.setText("1000000");
                    txtTipe.setText("Tipe B");
                } else if (selected.equals("Tipe C (Ekonomi)")) {
                    txtHarga.setText("500000");
                    txtTipe.setText("Tipe C");
                } else {
                    txtHarga.setText("");
                    txtTipe.setText("");
                }
            }
        });
        
        // Selection listener
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    selectedId = (int) tableModel.getValueAt(row, 0);
                    String nomor = (String) tableModel.getValueAt(row, 1);
                    String tipe = (String) tableModel.getValueAt(row, 2);
                    String hargaStr = (String) tableModel.getValueAt(row, 3);
                    hargaStr = hargaStr.replace("Rp ", "").replace(",", "").trim();
                    
                    txtNomor.setText(nomor);
                    txtTipe.setText(tipe);
                    txtHarga.setText(hargaStr);
                    
                    if (tipe.equals("Tipe A")) {
                        cmbTipe.setSelectedIndex(1);
                    } else if (tipe.equals("Tipe B")) {
                        cmbTipe.setSelectedIndex(2);
                    } else if (tipe.equals("Tipe C")) {
                        cmbTipe.setSelectedIndex(3);
                    } else {
                        cmbTipe.setSelectedIndex(0);
                    }
                }
            }
        });
        
        // Renderer untuk status
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (column == 0 || column == 1) {
                    setHorizontalAlignment(CENTER);
                } else if (column == 3) {
                    setHorizontalAlignment(RIGHT);
                } else {
                    setHorizontalAlignment(LEFT);
                }
                
                if (!isSelected) {
                    String status = (String) tableModel.getValueAt(row, 4);
                    if ("TERISI".equals(status)) {
                        c.setBackground(new Color(200, 230, 200));
                        c.setForeground(new Color(27, 94, 32));
                    } else if ("KOSONG".equals(status)) {
                        c.setBackground(new Color(255, 235, 200));
                        c.setForeground(new Color(194, 119, 0));
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                }
                return c;
            }
        });
    }
    
    private void loadTable() {
        tableModel.setRowCount(0);
        List<Kamar> list = KamarController.getAllKamar();
        for (Kamar k : list) {
            tableModel.addRow(new Object[]{
                k.getId(), 
                k.getNomorKamar(), 
                k.getTipe(), 
                String.format("Rp %,d", (long)k.getHarga()), 
                k.getStatus()
            });
        }
        if (dashboard != null) dashboard.loadDashboardData();
    }
    
    private void cariKamar() {
        String keyword = txtCari.getText().trim();
        if (keyword.isEmpty()) {
            loadTable();
            return;
        }
        tableModel.setRowCount(0);
        List<Kamar> list = KamarController.cariKamar(keyword);
        for (Kamar k : list) {
            tableModel.addRow(new Object[]{
                k.getId(), 
                k.getNomorKamar(), 
                k.getTipe(), 
                String.format("Rp %,d", (long)k.getHarga()), 
                k.getStatus()
            });
        }
    }
    
    private void tambahKamar() {
        try {
            String nomor = txtNomor.getText().trim();
            String tipe = txtTipe.getText().trim();
            String hargaStr = txtHarga.getText().trim().replaceAll("[,.]", "");
            
            if (nomor.isEmpty() || tipe.isEmpty() || hargaStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, 
                    "⚠️ Isi semua field dengan lengkap!", 
                    "Peringatan", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            double harga = Double.parseDouble(hargaStr);
            
            if (KamarController.tambahKamar(nomor, tipe, harga)) {
                JOptionPane.showMessageDialog(this, 
                    "✅ Kamar berhasil ditambahkan!", 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Gagal menambahkan kamar!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "❌ Harga harus berupa angka!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void editKamar() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ Pilih kamar yang akan diedit terlebih dahulu!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            String nomor = txtNomor.getText().trim();
            String tipe = txtTipe.getText().trim();
            String hargaStr = txtHarga.getText().trim().replaceAll("[,.]", "");
            double harga = Double.parseDouble(hargaStr);
            
            if (KamarController.updateKamar(selectedId, nomor, tipe, harga)) {
                JOptionPane.showMessageDialog(this, 
                    "✅ Data kamar berhasil diupdate!", 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadTable();
                clearForm();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "❌ Harga harus berupa angka!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void hapusKamar() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ Pilih kamar yang akan dihapus terlebih dahulu!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String nomorKamar = txtNomor.getText().trim();
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus kamar " + nomorKamar + "?\nData yang telah dihapus tidak dapat dikembalikan!", 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (KamarController.hapusKamar(selectedId)) {
                JOptionPane.showMessageDialog(this, 
                    "✅ Kamar berhasil dihapus!", 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Gagal menghapus kamar!\nPastikan kamar tidak sedang dihuni.", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void clearForm() {
        txtNomor.setText("");
        txtTipe.setText("");
        txtHarga.setText("");
        selectedId = -1;
        cmbTipe.setSelectedIndex(0);
    }
}