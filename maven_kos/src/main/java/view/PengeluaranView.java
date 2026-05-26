package view;

import controller.PengeluaranController;
import model.Pengeluaran;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import util.UIUtilities;

public class PengeluaranView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtTanggal, txtDeskripsi, txtJumlah;
    private JComboBox<String> cmbKategori;
    private JButton btnTambah, btnHapus, btnRefresh;
    private DashboardView dashboard;

    public PengeluaranView(DashboardView dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponents();
        loadTable();
    }

    private void initComponents() {
        // ========== PANEL UTAMA DENGAN SPLIT PANE (50:50) ==========
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.5); // 50% untuk form, 50% untuk tabel
        splitPane.setDividerSize(8);
        splitPane.setBorder(null);
        splitPane.setContinuousLayout(true);
        
        // ========== PANEL FORM CATAT PENGELUARAN (ATAS) ==========
        JPanel formCard = UIUtilities.createCardPanel(15);
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 12, 10, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        gbc.weightx = 1;
        gbc.weighty = 0;

        // Title
        JLabel title = new JLabel("📝 Catat Pengeluaran");
        title.setFont(new Font("Segoe UI", Font.BOLD, 16));
        title.setForeground(UIUtilities.TEXT_PRIMARY);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(15, 15, 15, 15);
        formCard.add(title, gbc);

        // Reset insets untuk field
        gbc.insets = new Insets(8, 12, 8, 12);
        gbc.gridwidth = 1;
        
        // ===== BARIS 1: Tanggal dan Deskripsi =====
        // Label Tanggal
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.weightx = 0.1;
        JLabel lblTanggal = new JLabel("Tanggal   :");
        lblTanggal.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formCard.add(lblTanggal, gbc);
        
        // Field Tanggal
        gbc.gridx = 1;
        gbc.weightx = 0.3;
        txtTanggal = UIUtilities.createModernTextField(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        txtTanggal.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtTanggal.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        txtTanggal.setPreferredSize(new Dimension(140, 38));
        formCard.add(txtTanggal, gbc);
        
        // Label Deskripsi
        gbc.gridx = 2;
        gbc.weightx = 0.1;
        JLabel lblDeskripsi = new JLabel("Deskripsi   :");
        lblDeskripsi.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formCard.add(lblDeskripsi, gbc);
        
        // Field Deskripsi
        gbc.gridx = 3;
        gbc.weightx = 0.5;
        txtDeskripsi = UIUtilities.createModernTextField("");
        txtDeskripsi.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtDeskripsi.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formCard.add(txtDeskripsi, gbc);

        // ===== BARIS 2: Jumlah, Kategori, dan Button =====
        gbc.gridy = 2;
        
        // Label Jumlah
        gbc.gridx = 0;
        gbc.weightx = 0.1;
        JLabel lblJumlah = new JLabel("Jumlah (Rp)   :");
        lblJumlah.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formCard.add(lblJumlah, gbc);
        
        // Field Jumlah
        gbc.gridx = 1;
        gbc.weightx = 0.3;
        txtJumlah = UIUtilities.createModernTextField("");
        txtJumlah.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        txtJumlah.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        formCard.add(txtJumlah, gbc);
        
        // Label Kategori
        gbc.gridx = 2;
        gbc.weightx = 0.1;
        JLabel lblKategori = new JLabel("Kategori   :");
        lblKategori.setFont(new Font("Segoe UI", Font.BOLD, 12));
        formCard.add(lblKategori, gbc);
        
        // Combo Kategori
        gbc.gridx = 3;
        gbc.weightx = 0.3;
        String[] kategoriList = {"Operasional", "Perbaikan", "Listrik", "Air", "Lainnya"};
        cmbKategori = new JComboBox<>(kategoriList);
        cmbKategori.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        cmbKategori.setPreferredSize(new Dimension(160, 38));
        cmbKategori.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(180, 180, 180), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        cmbKategori.setBackground(Color.WHITE);
        formCard.add(cmbKategori, gbc);
        
        // ===== BARIS 3: Button (dipindah ke baris sendiri agar rapi) =====
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 4;
        gbc.insets = new Insets(15, 12, 15, 12);
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.setOpaque(false);
        
        btnTambah = UIUtilities.createModernButton("➕ Tambah", new Color(46, 125, 50), Color.WHITE);
        btnHapus = UIUtilities.createModernButton("🗑️ Hapus", new Color(198, 40, 40), Color.WHITE);
        btnRefresh = UIUtilities.createModernButton("⟳ Refresh", new Color(66, 66, 66), Color.WHITE);
        
        btnTambah.setPreferredSize(new Dimension(130, 42));
        btnHapus.setPreferredSize(new Dimension(130, 42));
        btnRefresh.setPreferredSize(new Dimension(130, 42));
        
        btnTambah.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnHapus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        buttonPanel.add(btnTambah);
        buttonPanel.add(btnHapus);
        buttonPanel.add(btnRefresh);
        formCard.add(buttonPanel, gbc);
        
        // ========== PANEL DAFTAR PENGELUARAN (BAWAH) DENGAN SCROLL ==========
        String[] columns = {"ID", "Tanggal", "Deskripsi", "Jumlah", "Kategori"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(245, 240, 235));
        table.getTableHeader().setForeground(new Color(92, 64, 51));
        table.getTableHeader().setOpaque(true);
        
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(32);
        table.setSelectionBackground(new Color(52, 152, 219));
        table.setSelectionForeground(Color.WHITE);
        table.setShowGrid(true);
        table.setGridColor(new Color(220, 220, 220));
        
        // Set lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(50);
        table.getColumnModel().getColumn(1).setPreferredWidth(100);
        table.getColumnModel().getColumn(2).setPreferredWidth(300);
        table.getColumnModel().getColumn(3).setPreferredWidth(150);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        
        // JScrollPane untuk tabel (SCROLL hanya di sini)
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        
        JPanel tableCard = UIUtilities.createCardPanel(15);
        tableCard.setLayout(new BorderLayout(0, 10));
        JLabel tableTitle = new JLabel("Daftar Pengeluaran", UIUtilities.loadIcon("report.png", 18, 18), JLabel.LEFT);
        tableTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        tableTitle.setForeground(UIUtilities.TEXT_PRIMARY);
        tableTitle.setIconTextGap(8);
        tableCard.add(tableTitle, BorderLayout.NORTH);
        tableCard.add(scrollPane, BorderLayout.CENTER);
        
        // ========== SUSUN LAYOUT DENGAN SPLIT PANE 50:50 ==========
        splitPane.setTopComponent(formCard);
        splitPane.setBottomComponent(tableCard);
        
        // ========== MASTER SCROLL UNTUK SPLIT PANE (OPSIONAL) ==========
        // Split pane sudah memiliki kemampuan scroll internal
        add(splitPane, BorderLayout.CENTER);

        // ========== EVENT LISTENERS ==========
        btnTambah.addActionListener(e -> tambahPengeluaran());
        btnHapus.addActionListener(e -> hapusPengeluaran());
        btnRefresh.addActionListener(e -> loadTable());

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    txtTanggal.setText((String) tableModel.getValueAt(row, 1));
                    txtDeskripsi.setText((String) tableModel.getValueAt(row, 2));
                    txtJumlah.setText(String.valueOf(tableModel.getValueAt(row, 3)));
                    cmbKategori.setSelectedItem(tableModel.getValueAt(row, 4));
                }
            }
        });
        
        // Renderer untuk format jumlah Rupiah
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                if (column == 3) {
                    setHorizontalAlignment(RIGHT);
                    if (value instanceof Number) {
                        setText(String.format("Rp %,d", ((Number) value).longValue()));
                    }
                } else if (column == 0) {
                    setHorizontalAlignment(CENTER);
                } else {
                    setHorizontalAlignment(LEFT);
                }
                
                if (!isSelected) {
                    c.setBackground(Color.WHITE);
                }
                return c;
            }
        });
    }

    private void loadTable() {
        tableModel.setRowCount(0);
        List<Pengeluaran> list = PengeluaranController.getAllPengeluaran();
        for (Pengeluaran p : list) {
            tableModel.addRow(new Object[]{
                p.getId(), 
                p.getTanggal(), 
                p.getDeskripsi(), 
                p.getJumlah(), 
                p.getKategori()
            });
        }
        if (dashboard != null) dashboard.loadDashboardData();
    }

    private void tambahPengeluaran() {
        try {
            String tanggal = txtTanggal.getText().trim();
            String deskripsi = txtDeskripsi.getText().trim();
            double jumlah = Double.parseDouble(txtJumlah.getText().trim().replaceAll("[,.]", ""));
            String kategori = (String) cmbKategori.getSelectedItem();

            if (tanggal.isEmpty() || deskripsi.isEmpty() || jumlah <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "⚠️ Isi semua field dengan benar!", 
                    "Peringatan", 
                    JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            if (PengeluaranController.tambahPengeluaran(tanggal, deskripsi, jumlah, kategori)) {
                JOptionPane.showMessageDialog(this, 
                    "✅ Pengeluaran berhasil dicatat!", 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Gagal mencatat pengeluaran!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, 
                "❌ Jumlah harus berupa angka!", 
                "Error", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void hapusPengeluaran() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, 
                "⚠️ Pilih pengeluaran yang akan dihapus!", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int id = (int) tableModel.getValueAt(row, 0);
        String deskripsi = (String) tableModel.getValueAt(row, 2);
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Apakah Anda yakin ingin menghapus pengeluaran?\n" + deskripsi, 
            "Konfirmasi Hapus", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            if (PengeluaranController.hapusPengeluaran(id)) {
                JOptionPane.showMessageDialog(this, 
                    "✅ Pengeluaran berhasil dihapus!", 
                    "Sukses", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadTable();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "❌ Gagal menghapus pengeluaran!", 
                    "Error", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void clearForm() {
        txtTanggal.setText(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        txtDeskripsi.setText("");
        txtJumlah.setText("");
        cmbKategori.setSelectedIndex(0);
    }
}