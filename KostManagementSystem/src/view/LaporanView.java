package view;

import controller.LaporanController;
import util.UIUtilities;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;
import java.util.Locale;

public class LaporanView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnRefresh;
    private JLabel lblPendapatan, lblPengeluaran, lblLabaRugi;

    public LaporanView(DashboardView dashboard) {
        setLayout(new BorderLayout(12, 12));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setBackground(UIUtilities.LIGHT_BG);
        initComponents();
        loadReport();
    }

    private void initComponents() {
        // ========== PANEL TITLE (ATAS) ==========
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        JLabel title = new JLabel("Laporan Keuangan dan Status Pembayaran");
        title.setFont(UIUtilities.FONT_SUBTITLE);
        title.setForeground(UIUtilities.TEXT_PRIMARY);
        titlePanel.add(title, BorderLayout.WEST);

        btnRefresh = UIUtilities.createModernButton("Refresh Laporan", UIUtilities.PRIMARY_COLOR, Color.WHITE);
        btnRefresh.setPreferredSize(new Dimension(170, 38));
        titlePanel.add(btnRefresh, BorderLayout.EAST);

        // ========== PANEL SUMMARY (TENGAH ATAS) dengan tinggi tetap ==========
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 16, 0));
        summaryPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 20, 0));
        summaryPanel.setPreferredSize(new Dimension(0, 110));  // PERBAIKAN: tinggi tetap
        summaryPanel.add(createSummaryCard("Pendapatan Bulan Ini", "Rp 0"));
        summaryPanel.add(createSummaryCard("Pengeluaran Bulan Ini", "Rp 0"));
        summaryPanel.add(createSummaryCard("Laba / Rugi", "Rp 0"));

        // ========== PANEL TABEL (BAWAH) ==========
        String[] columns = {"ID", "NIK", "Nama", "No Telp", "Nomor Kamar", "Harga Kamar", "Status Bayar Bulan Ini"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        table.setRowHeight(30);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Set lebar kolom
        table.getColumnModel().getColumn(0).setPreferredWidth(50);   // ID
        table.getColumnModel().getColumn(1).setPreferredWidth(100);  // NIK
        table.getColumnModel().getColumn(2).setPreferredWidth(150);  // Nama
        table.getColumnModel().getColumn(3).setPreferredWidth(100);  // No Telp
        table.getColumnModel().getColumn(4).setPreferredWidth(100);  // Nomor Kamar
        table.getColumnModel().getColumn(5).setPreferredWidth(120);  // Harga Kamar
        table.getColumnModel().getColumn(6).setPreferredWidth(150);  // Status
        
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtilities.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBackground(UIUtilities.LIGHT_BG);

        // Panel wrapper untuk tabel dengan border title
        JPanel tableWrapper = new JPanel(new BorderLayout());
        tableWrapper.setOpaque(false);
        tableWrapper.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(UIUtilities.BORDER_COLOR), 
            "Laporan Penghuni Aktif", 
            0, 0, 
            UIUtilities.FONT_REGULAR, 
            UIUtilities.TEXT_SECONDARY));
        tableWrapper.add(scrollPane, BorderLayout.CENTER);

        // ========== PERBAIKAN: Susun dengan Panel Tengah berisi Vertical Layout ==========
        // Buat panel tengah yang berisi summary dan tabel secara vertikal
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.add(summaryPanel);
        centerPanel.add(Box.createVerticalStrut(10)); // Spacer
        centerPanel.add(tableWrapper);
        
        // ========== SUSUN LAYOUT UTAMA ==========
        add(titlePanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        
        // ========== EVENT LISTENER ==========
        btnRefresh.addActionListener(e -> loadReport());
    }

    private JPanel createSummaryCard(String label, String value) {
        JPanel card = UIUtilities.createRoundedPanel(UIUtilities.CARD_BG, 18);
        card.setLayout(new BorderLayout());
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(UIUtilities.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(12, 15, 12, 15)));  // PERBAIKAN: padding lebih kecil
        
        JLabel labelComponent = new JLabel(label);
        labelComponent.setFont(UIUtilities.FONT_HEADING);
        labelComponent.setForeground(UIUtilities.TEXT_SECONDARY);
        
        JLabel valueComponent = new JLabel(value);
        valueComponent.setFont(new Font("Segoe UI", Font.BOLD, 20));  // PERBAIKAN: ukuran font sedikit lebih kecil
        valueComponent.setForeground(UIUtilities.TEXT_PRIMARY);
        
        card.add(labelComponent, BorderLayout.NORTH);
        card.add(valueComponent, BorderLayout.CENTER);
        
        // Simpan referensi untuk update nanti
        if (lblPendapatan == null && label.equals("Pendapatan Bulan Ini")) {
            lblPendapatan = valueComponent;
        }
        if (lblPengeluaran == null && label.equals("Pengeluaran Bulan Ini")) {
            lblPengeluaran = valueComponent;
        }
        if (lblLabaRugi == null && label.equals("Laba / Rugi")) {
            lblLabaRugi = valueComponent;
        }
        
        return card;
    }

    private void loadReport() {
        // Bersihkan tabel
        tableModel.setRowCount(0);
        
        // Load data dari controller
        DefaultTableModel model = LaporanController.getLaporanPenghuniAktif();
        if (model != null) {
            for (int i = 0; i < model.getRowCount(); i++) {
                tableModel.addRow(new Object[]{
                    model.getValueAt(i, 0),
                    model.getValueAt(i, 1),
                    model.getValueAt(i, 2),
                    model.getValueAt(i, 3),
                    model.getValueAt(i, 4),
                    model.getValueAt(i, 5),
                    model.getValueAt(i, 6)
                });
            }
        }
        
        // Load data keuangan
        double pendapatan = LaporanController.getTotalPendapatanBulanIni();
        double pengeluaran = LaporanController.getTotalPengeluaranBulanIni();
        double labaRugi = LaporanController.getLabaRugiBulanIni();
        
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        
        if (lblPendapatan != null) lblPendapatan.setText(formatter.format(pendapatan));
        if (lblPengeluaran != null) lblPengeluaran.setText(formatter.format(pengeluaran));
        if (lblLabaRugi != null) lblLabaRugi.setText(formatter.format(labaRugi));
        
        // Warna berdasarkan nilai laba/rugi
        if (lblLabaRugi != null) {
            if (labaRugi >= 0) {
                lblLabaRugi.setForeground(new Color(46, 125, 50)); // Hijau untuk laba
            } else {
                lblLabaRugi.setForeground(new Color(198, 40, 40)); // Merah untuk rugi
            }
        }
        
        // Custom renderer untuk warna status
        table.setDefaultRenderer(Object.class, new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, 
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Alignment untuk kolom tertentu
                if (column == 5) { // Harga Kamar
                    setHorizontalAlignment(RIGHT);
                } else {
                    setHorizontalAlignment(LEFT);
                }
                
                if (!isSelected && tableModel.getRowCount() > row) {
                    String status = "";
                    try {
                        status = (String) tableModel.getValueAt(row, 6);
                    } catch (Exception e) {}
                    
                    if ("LUNAS".equals(status)) {
                        c.setBackground(new Color(200, 230, 200)); // Hijau muda
                        c.setForeground(Color.BLACK);
                    } else if ("BELUM LUNAS".equals(status) || "BELUM".equals(status)) {
                        c.setBackground(new Color(255, 200, 200)); // Merah muda
                        c.setForeground(Color.BLACK);
                    } else {
                        c.setBackground(Color.WHITE);
                    }
                } else if (isSelected) {
                    c.setBackground(table.getSelectionBackground());
                    c.setForeground(table.getSelectionForeground());
                }
                
                return c;
            }
        });
        
        // Refresh tampilan
        table.repaint();
        revalidate();
        repaint();
    }
}