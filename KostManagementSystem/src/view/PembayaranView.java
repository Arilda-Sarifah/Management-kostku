package view;

import controller.PembayaranController;
import controller.PenghuniController;
import model.Penghuni;
import util.UIUtilities;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PembayaranView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JComboBox<String> cmbPenghuni, cmbBulan;
    private JTextField txtTahun, txtJumlah, txtTglBayar, txtLamaBulan;
    private JButton btnBayar, btnHapus, btnRefresh, btnInvoice;
    private DashboardView dashboard;
    private JScrollPane scrollPane;
    private JLabel lblInfoData;
    
    public PembayaranView(DashboardView dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout(10, 10));
        setBackground(UIUtilities.LIGHT_BG);
        
        // Inisialisasi komponen
        initComponents();
        
        // Load data ke tabel
        loadTable();
    }
    
    private void initComponents() {
    // ========== PANEL FORM INPUT (atas) ==========
    JPanel formPanel = new JPanel(new GridBagLayout());
    formPanel.setBackground(UIUtilities.CARD_BG);
    formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 8, 5, 8);  // PERBAIKAN: kurangi vertical inset
    gbc.anchor = GridBagConstraints.WEST;
    gbc.fill = GridBagConstraints.HORIZONTAL;
    
    // Title
    JLabel titleLabel = new JLabel("FORM PEMBAYARAN");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    formPanel.add(titleLabel, gbc);
    
    // Penghuni
    gbc.gridy = 1;
    gbc.gridwidth = 1;
    gbc.gridx = 0;
    formPanel.add(new JLabel("Penghuni:"), gbc);
    gbc.gridx = 1;
    cmbPenghuni = new JComboBox<>();
    cmbPenghuni.setPreferredSize(new Dimension(200, 28));  // PERBAIKAN: kurangi tinggi
    loadPenghuniCombo();
    formPanel.add(cmbPenghuni, gbc);
    
    // Bulan
    gbc.gridy = 2;
    gbc.gridx = 0;
    formPanel.add(new JLabel("Bulan:"), gbc);
    gbc.gridx = 1;
    String[] bulanList = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", 
                          "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
    cmbBulan = new JComboBox<>(bulanList);
    cmbBulan.setPreferredSize(new Dimension(200, 28));
    formPanel.add(cmbBulan, gbc);
    
    // Tahun
    gbc.gridy = 3;
    gbc.gridx = 0;
    formPanel.add(new JLabel("Tahun:"), gbc);
    gbc.gridx = 1;
    txtTahun = new JTextField(String.valueOf(LocalDate.now().getYear()));
    txtTahun.setPreferredSize(new Dimension(200, 28));
    formPanel.add(txtTahun, gbc);
    
    // Jumlah Bayar
    gbc.gridy = 4;
    gbc.gridx = 0;
    formPanel.add(new JLabel("Jumlah Bayar (Rp):"), gbc);
    gbc.gridx = 1;
    txtJumlah = new JTextField();
    txtJumlah.setPreferredSize(new Dimension(200, 28));
    formPanel.add(txtJumlah, gbc);
    
    // Durasi
    gbc.gridy = 5;
    gbc.gridx = 0;
    formPanel.add(new JLabel("Durasi (bulan):"), gbc);
    gbc.gridx = 1;
    txtLamaBulan = new JTextField("1");
    txtLamaBulan.setPreferredSize(new Dimension(200, 28));
    formPanel.add(txtLamaBulan, gbc);
    
    // Tanggal Bayar
    gbc.gridy = 6;
    gbc.gridx = 0;
    formPanel.add(new JLabel("Tanggal Bayar:"), gbc);
    gbc.gridx = 1;
    txtTglBayar = new JTextField(LocalDate.now().toString());
    txtTglBayar.setPreferredSize(new Dimension(200, 28));
    formPanel.add(txtTglBayar, gbc);
    
    // Buttons
    gbc.gridy = 7;
    gbc.gridx = 0;
    gbc.gridwidth = 2;
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
    buttonPanel.setBackground(UIUtilities.CARD_BG);
    
    btnBayar = new JButton("Catat Pembayaran");
    btnBayar.setBackground(UIUtilities.PRIMARY_COLOR);
    btnBayar.setForeground(Color.BLACK);
    btnBayar.setFocusPainted(false);
    btnBayar.setPreferredSize(new Dimension(150, 32));  // PERBAIKAN: kurangi tinggi
    
    btnInvoice = new JButton("Cetak Invoice");
    btnInvoice.setBackground(UIUtilities.SUCCESS_COLOR);
    btnInvoice.setForeground(Color.BLACK);
    btnInvoice.setFocusPainted(false);
    btnInvoice.setPreferredSize(new Dimension(130, 32));
    
    btnHapus = new JButton("Hapus Data");
    btnHapus.setBackground(UIUtilities.ERROR_COLOR);
    btnHapus.setForeground(Color.BLACK);
    btnHapus.setFocusPainted(false);
    btnHapus.setPreferredSize(new Dimension(120, 32));
    
    btnRefresh = new JButton("Refresh");
    btnRefresh.setBackground(UIUtilities.DARK_BG);
    btnRefresh.setForeground(Color.BLACK);
    btnRefresh.setFocusPainted(false);
    btnRefresh.setPreferredSize(new Dimension(100, 32));
    
    buttonPanel.add(btnBayar);
    buttonPanel.add(btnInvoice);
    buttonPanel.add(btnHapus);
    buttonPanel.add(btnRefresh);
    formPanel.add(buttonPanel, gbc);
    
    // ========== PERBAIKAN UTAMA: Atur ukuran form panel ==========
    // Jangan set preferred size yang terlalu besar
    
    // ========== PANEL TABEL ==========
    String[] columns = {"ID", "Penghuni", "Bulan", "Tahun", "Lama", "Jumlah", 
                       "Total Tagihan", "Kurang", "Tgl Bayar", "Status", "Keterangan"};
    
    tableModel = new DefaultTableModel(columns, 0) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    table = new JTable(tableModel);
    table.setFont(new Font("Arial", Font.PLAIN, 12));
    table.setRowHeight(28);  // PERBAIKAN: kurangi row height
    
    // ... (setting tabel lainnya sama seperti sebelumnya) ...
    
    // ScrollPane untuk tabel
    scrollPane = new JScrollPane(table);
    scrollPane.setBorder(BorderFactory.createTitledBorder("Riwayat Pembayaran"));
    scrollPane.getVerticalScrollBar().setUnitIncrement(16);
    
    // Label info
    lblInfoData = new JLabel("Loading data...");
    lblInfoData.setFont(new Font("Arial", Font.ITALIC, 11));
    lblInfoData.setForeground(UIUtilities.TEXT_SECONDARY);
    
    JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    infoPanel.setBackground(UIUtilities.CARD_BG);
    infoPanel.add(lblInfoData);
    
    JPanel tablePanel = new JPanel(new BorderLayout());
    tablePanel.add(scrollPane, BorderLayout.CENTER);
    tablePanel.add(infoPanel, BorderLayout.SOUTH);
    
    // ========== SUSUN LAYOUT DENGAN BORDERLAYOUT ==========
    // Gunakan BorderLayout sederhana, BUKAN JSplitPane
    
    // Bungkus formPanel dengan JScrollPane agar bisa di-scroll jika terlalu tinggi
    JScrollPane formScrollPane = new JScrollPane(formPanel);
    formScrollPane.setBorder(null);
    formScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    formScrollPane.setPreferredSize(new Dimension(0, 280));  // PERBAIKAN: batasi tinggi form area
    
    // Panel utama
    JPanel mainPanel = new JPanel(new BorderLayout(0, 5));
    mainPanel.add(formScrollPane, BorderLayout.NORTH);
    mainPanel.add(tablePanel, BorderLayout.CENTER);
    
    // Bungkus dengan master scroll (opsional)
    JScrollPane masterScroll = new JScrollPane(mainPanel);
    masterScroll.setBorder(null);
    masterScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    
    add(masterScroll, BorderLayout.CENTER);
    
    // ========== EVENT LISTENERS ==========
    btnBayar.addActionListener(e -> catatPembayaran());
    btnInvoice.addActionListener(e -> cetakInvoice());
    btnHapus.addActionListener(e -> hapusPembayaran());
    btnRefresh.addActionListener(e -> refreshData());
    
    cmbPenghuni.addActionListener(e -> {
        if (cmbPenghuni.getSelectedIndex() > 0) {
            autoFillJumlah();
        }
    });
}
    
    // ========== PERBAIKAN 5: Method untuk menyesuaikan lebar kolom ==========
    private void adjustTableColumnWidths() {
        if (table == null || table.getColumnCount() == 0) return;
        
        int totalWidth = table.getParent().getWidth();
        if (totalWidth > 0) {
            // Set lebar kolom relatif terhadap lebar total
            table.getColumnModel().getColumn(0).setPreferredWidth((int)(totalWidth * 0.05)); // ID
            table.getColumnModel().getColumn(1).setPreferredWidth((int)(totalWidth * 0.12)); // Penghuni
            table.getColumnModel().getColumn(2).setPreferredWidth((int)(totalWidth * 0.08)); // Bulan
            table.getColumnModel().getColumn(3).setPreferredWidth((int)(totalWidth * 0.06)); // Tahun
            table.getColumnModel().getColumn(4).setPreferredWidth((int)(totalWidth * 0.05)); // Lama
            table.getColumnModel().getColumn(5).setPreferredWidth((int)(totalWidth * 0.10)); // Jumlah
            table.getColumnModel().getColumn(6).setPreferredWidth((int)(totalWidth * 0.10)); // Total
            table.getColumnModel().getColumn(7).setPreferredWidth((int)(totalWidth * 0.08)); // Kurang
            table.getColumnModel().getColumn(8).setPreferredWidth((int)(totalWidth * 0.10)); // Tgl Bayar
            table.getColumnModel().getColumn(9).setPreferredWidth((int)(totalWidth * 0.08)); // Status
            table.getColumnModel().getColumn(10).setPreferredWidth((int)(totalWidth * 0.18)); // Keterangan
        }
    }
    
    // ========== FITUR REFRESH YANG DITINGKATKAN ==========
    private void refreshData() {
        setButtonsEnabled(false);
        
        JDialog progressDialog = new JDialog();
        progressDialog.setTitle("Refresh Data");
        progressDialog.setModal(true);
        progressDialog.setSize(300, 100);
        progressDialog.setLocationRelativeTo(this);
        progressDialog.setUndecorated(true);
        
        JPanel progressPanel = new JPanel(new BorderLayout());
        progressPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        JLabel progressLabel = new JLabel("Memuat ulang data pembayaran...", SwingConstants.CENTER);
        JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressPanel.add(progressLabel, BorderLayout.CENTER);
        progressPanel.add(progressBar, BorderLayout.SOUTH);
        progressDialog.add(progressPanel);
        
        SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
            @Override
            protected Void doInBackground() throws Exception {
                Thread.sleep(100);
                loadTable();
                loadPenghuniCombo();
                return null;
            }
            
            @Override
            protected void done() {
                progressDialog.dispose();
                setButtonsEnabled(true);
                
                int rowCount = tableModel.getRowCount();
                String message;
                if (rowCount <= 1 && tableModel.getRowCount() > 0 && 
                    tableModel.getValueAt(0, 0).toString().equals("-")) {
                    message = "Refresh selesai! Tidak ada data pembayaran.";
                } else {
                    message = String.format("Refresh selesai! Total data: %d baris", rowCount);
                }
                
                JOptionPane.showMessageDialog(PembayaranView.this, 
                    message, 
                    "Refresh Berhasil", 
                    JOptionPane.INFORMATION_MESSAGE);
                
                if (dashboard != null) {
                    dashboard.loadDashboardData();
                }
                
                adjustTableColumnWidths();
            }
        };
        
        worker.execute();
        progressDialog.setVisible(true);
    }
    
    private void setButtonsEnabled(boolean enabled) {
        btnBayar.setEnabled(enabled);
        btnInvoice.setEnabled(enabled);
        btnHapus.setEnabled(enabled);
        btnRefresh.setEnabled(enabled);
    }
    
    private void loadPenghuniCombo() {
        cmbPenghuni.removeAllItems();
        cmbPenghuni.addItem("-- Pilih Penghuni --");
        
        try {
            List<Penghuni> list = PenghuniController.getPenghuniAktif();
            for (Penghuni p : list) {
                cmbPenghuni.addItem(p.getId() + " - " + p.getNama());
            }
            
            if (lblInfoData != null) {
                lblInfoData.setText("Load combo: " + list.size() + " penghuni aktif");
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error load penghuni: " + e.getMessage());
            if (lblInfoData != null) {
                lblInfoData.setText("Error load penghuni");
            }
        }
    }
    
    private void autoFillJumlah() {
        if (cmbPenghuni.getSelectedIndex() > 0) {
            try {
                String selected = (String) cmbPenghuni.getSelectedItem();
                int idPenghuni = Integer.parseInt(selected.split(" - ")[0]);
                double harga = PembayaranController.getHargaKamarByPenghuni(idPenghuni);
                txtJumlah.setText(String.format("%.0f", harga));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
    private void loadTable() {
        tableModel.setRowCount(0);
        
        try {
            List<Object[]> list = PembayaranController.getAllPembayaran();
            
            if (list == null || list.isEmpty()) {
                tableModel.addRow(new Object[]{"-", "Tidak ada data", "-", "-", "-", "-", "-", "-", "-", "-", "-"});
                if (lblInfoData != null) {
                    lblInfoData.setText("Total data: 0");
                }
            } else {
                for (Object[] row : list) {
                    tableModel.addRow(row);
                }
                if (lblInfoData != null) {
                    lblInfoData.setText("Total data: " + list.size());
                }
            }
            
            table.repaint();
            adjustTableColumnWidths();
            
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error load data: " + e.getMessage());
            tableModel.addRow(new Object[]{"Error", e.getMessage(), "", "", "", "", "", "", "", "", ""});
            if (lblInfoData != null) {
                lblInfoData.setText("Error loading data");
            }
        }
    }
    
    private void catatPembayaran() {
        try {
            if (cmbPenghuni.getSelectedIndex() == 0) {
                JOptionPane.showMessageDialog(this, "Pilih penghuni terlebih dahulu!");
                return;
            }
            
            String selected = (String) cmbPenghuni.getSelectedItem();
            int idPenghuni = Integer.parseInt(selected.split(" - ")[0]);
            String bulan = (String) cmbBulan.getSelectedItem();
            int tahun = Integer.parseInt(txtTahun.getText().trim());
            int lamaBulan = Integer.parseInt(txtLamaBulan.getText().trim());
            double jumlah = Double.parseDouble(txtJumlah.getText().trim().replace(",", ""));
            String tglBayar = txtTglBayar.getText().trim();
            
            if (lamaBulan <= 0) {
                JOptionPane.showMessageDialog(this, "Durasi bulan minimal 1!");
                return;
            }
            
            if (PembayaranController.tambahPembayaran(idPenghuni, bulan, tahun, lamaBulan, jumlah, tglBayar)) {
                JOptionPane.showMessageDialog(this, "Pembayaran berhasil!");
                refreshData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal mencatat pembayaran!");
            }
            
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Input angka tidak valid!");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void cetakInvoice() {
        int row = table.getSelectedRow();
        
        if (row == -1) {
            JOptionPane.showMessageDialog(this, 
                "Silakan pilih data pembayaran yang akan dicetak!\n" +
                "Klik pada baris tabel terlebih dahulu.", 
                "Peringatan", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        try {
            String id = tableModel.getValueAt(row, 0).toString();
            if (id.equals("-") || id.equals("Error")) {
                JOptionPane.showMessageDialog(this, "Data tidak valid untuk dicetak!");
                return;
            }
            
            String penghuni = tableModel.getValueAt(row, 1).toString();
            String bulan = tableModel.getValueAt(row, 2).toString();
            String tahun = tableModel.getValueAt(row, 3).toString();
            String lamaBulan = tableModel.getValueAt(row, 4).toString();
            String jumlah = tableModel.getValueAt(row, 5).toString();
            String totalTagihan = tableModel.getValueAt(row, 6).toString();
            String kurangBayar = tableModel.getValueAt(row, 7).toString();
            String tglBayar = tableModel.getValueAt(row, 8).toString();
            String status = tableModel.getValueAt(row, 9).toString();
            String keterangan = tableModel.getValueAt(row, 10).toString();
            
            StringBuilder invoice = new StringBuilder();
            invoice.append("========================================\n");
            invoice.append("          INVOICE PEMBAYARAN           \n");
            invoice.append("========================================\n\n");
            invoice.append("No. Invoice : INV-").append(String.format("%06d", Integer.parseInt(id))).append("\n");
            invoice.append("Tanggal     : ").append(tglBayar).append("\n");
            invoice.append("----------------------------------------\n");
            invoice.append("Penghuni    : ").append(penghuni).append("\n");
            invoice.append("Periode     : ").append(bulan).append(" ").append(tahun).append("\n");
            invoice.append("Durasi      : ").append(lamaBulan).append(" bulan\n");
            invoice.append("----------------------------------------\n");
            invoice.append("Total Tagihan : Rp ").append(formatNumber(totalTagihan)).append("\n");
            invoice.append("Jumlah Bayar  : Rp ").append(formatNumber(jumlah)).append("\n");
            invoice.append("Kurang Bayar  : Rp ").append(formatNumber(kurangBayar)).append("\n");
            invoice.append("----------------------------------------\n");
            invoice.append("Status        : ").append(status).append("\n");
            invoice.append("Keterangan    : ").append(keterangan).append("\n");
            invoice.append("========================================\n");
            invoice.append("     Terima kasih atas pembayarannya!\n");
            invoice.append("========================================\n");
            
            JTextArea textArea = new JTextArea(invoice.toString());
            textArea.setEditable(false);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            
            JScrollPane scrollPaneInv = new JScrollPane(textArea);
            scrollPaneInv.setPreferredSize(new Dimension(500, 400));
            
            JOptionPane.showMessageDialog(this, scrollPaneInv, "Invoice - " + penghuni, JOptionPane.INFORMATION_MESSAGE);
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private String formatNumber(String value) {
        try {
            double num = Double.parseDouble(value);
            return String.format("%,.0f", num);
        } catch (Exception e) {
            return value;
        }
    }
    
    private void hapusPembayaran() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Pilih data yang akan dihapus!");
            return;
        }
        
        String idStr = tableModel.getValueAt(row, 0).toString();
        if (idStr.equals("-") || idStr.equals("Error")) {
            JOptionPane.showMessageDialog(this, "Data tidak valid untuk dihapus!");
            return;
        }
        
        int id = Integer.parseInt(idStr);
        String penghuni = tableModel.getValueAt(row, 1).toString();
        
        int confirm = JOptionPane.showConfirmDialog(this,
            "Hapus pembayaran?\nID: " + id + "\nPenghuni: " + penghuni,
            "Konfirmasi", JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            if (PembayaranController.hapusPembayaran(id)) {
                JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");
                refreshData();
            } else {
                JOptionPane.showMessageDialog(this, "Gagal menghapus data!");
            }
        }
    }
    
    private void clearForm() {
        cmbPenghuni.setSelectedIndex(0);
        cmbBulan.setSelectedIndex(LocalDate.now().getMonthValue() - 1);
        txtTahun.setText(String.valueOf(LocalDate.now().getYear()));
        txtJumlah.setText("");
        txtLamaBulan.setText("1");
        txtTglBayar.setText(LocalDate.now().toString());
    }
}