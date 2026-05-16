 
package view;

import controller.KamarController;
import controller.PenghuniController;
import model.Kamar;
import model.Penghuni;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class PenghuniView extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField txtNik, txtNama, txtNoTelp, txtCari;
    private JComboBox<String> cmbKamar;
    private JTextField txtTglMasuk;
    private JButton btnTambah, btnEdit, btnKeluar, btnRefresh, btnCari;
    private int selectedId = -1;
    private DashboardView dashboard;
    
    public PenghuniView(DashboardView dashboard) {
        this.dashboard = dashboard;
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        initComponents();
        loadTable();
    }
    
    private void initComponents() {
        // Search panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Cari:"));
        txtCari = new JTextField(15);
        searchPanel.add(txtCari);
        btnCari = new JButton("Cari");
        btnRefresh = new JButton("Refresh");
        searchPanel.add(btnCari);
        searchPanel.add(btnRefresh);
        
        // Form panel
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Data Penghuni"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("NIK:"), gbc);
        gbc.gridx = 1;
        txtNik = new JTextField(15);
        formPanel.add(txtNik, gbc);
        
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Nama Lengkap:"), gbc);
        gbc.gridx = 1;
        txtNama = new JTextField(15);
        formPanel.add(txtNama, gbc);
        
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("No Telepon:"), gbc);
        gbc.gridx = 1;
        txtNoTelp = new JTextField(15);
        formPanel.add(txtNoTelp, gbc);
        
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Pilih Kamar:"), gbc);
        gbc.gridx = 1;
        cmbKamar = new JComboBox<>();
        loadKamarCombo();
        formPanel.add(cmbKamar, gbc);
        
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Tanggal Masuk:"), gbc);
        gbc.gridx = 1;
        txtTglMasuk = new JTextField(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE), 15);
        formPanel.add(txtTglMasuk, gbc);
        
        // Button panel
        JPanel btnPanel = new JPanel(new FlowLayout());
        btnTambah = new JButton("Tambah Penghuni");
        btnEdit = new JButton("Edit Penghuni");
        btnKeluar = new JButton("Keluar");
        btnPanel.add(btnTambah);
        btnPanel.add(btnEdit);
        btnPanel.add(btnKeluar);
        
        // Table
        String[] columns = {"ID", "NIK", "Nama", "No Telp", "Kamar", "Tgl Masuk", "Tgl Keluar"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.getTableHeader().setReorderingAllowed(false);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Daftar Penghuni"));
        
        // Layout
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(formPanel, BorderLayout.CENTER);
        topPanel.add(btnPanel, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        
        // Events
        btnTambah.addActionListener(e -> tambahPenghuni());
        btnEdit.addActionListener(e -> editPenghuni());
        btnKeluar.addActionListener(e -> keluarkanPenghuni());
        btnRefresh.addActionListener(e -> {
            loadTable();
            txtCari.setText("");
            loadKamarCombo();
        });
        btnCari.addActionListener(e -> cariPenghuni());
        
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int row = table.getSelectedRow();
                if (row != -1) {
                    selectedId = (int) tableModel.getValueAt(row, 0);
                    txtNik.setText((String) tableModel.getValueAt(row, 1));
                    txtNama.setText((String) tableModel.getValueAt(row, 2));
                    txtNoTelp.setText((String) tableModel.getValueAt(row, 3));
                    txtTglMasuk.setText((String) tableModel.getValueAt(row, 5));
                    // Set combo box kamar
                    String kamar = (String) tableModel.getValueAt(row, 4);
                    if (kamar != null && !kamar.equals("-")) {
                        cmbKamar.setSelectedItem(kamar);
                    }
                }
            }
        });
    }
    
    private void loadKamarCombo() {
        cmbKamar.removeAllItems();
        cmbKamar.addItem("-- Pilih Kamar --");
        List<Kamar> list = KamarController.getKamarKosong();
        for (Kamar k : list) {
            cmbKamar.addItem(k.getNomorKamar() + " - " + k.getTipe() + " (Rp " + (long)k.getHarga() + ")");
        }
    }
    
    private void loadTable() {
        tableModel.setRowCount(0);
        List<Penghuni> list = PenghuniController.getAllPenghuni();
        for (Penghuni p : list) {
            String kamar = p.getIdKamar() > 0 ? getKamarByPenghuni(p.getIdKamar()) : "-";
            String tglKeluar = p.getTglKeluar() != null ? p.getTglKeluar() : "Aktif";
            tableModel.addRow(new Object[]{
                p.getId(), p.getNik(), p.getNama(), p.getNoTelp(), kamar, p.getTglMasuk(), tglKeluar
            });
        }
        if (dashboard != null) dashboard.loadDashboardData();
    }
    
    private String getKamarByPenghuni(int idKamar) {
        List<Kamar> list = KamarController.getAllKamar();
        for (Kamar k : list) {
            if (k.getId() == idKamar) return k.getNomorKamar();
        }
        return "-";
    }
    
    private void cariPenghuni() {
        String keyword = txtCari.getText().trim();
        if (keyword.isEmpty()) {
            loadTable();
            return;
        }
        tableModel.setRowCount(0);
        List<Penghuni> list = PenghuniController.cariPenghuni(keyword);
        for (Penghuni p : list) {
            String kamar = p.getIdKamar() > 0 ? getKamarByPenghuni(p.getIdKamar()) : "-";
            String tglKeluar = p.getTglKeluar() != null ? p.getTglKeluar() : "Aktif";
            tableModel.addRow(new Object[]{
                p.getId(), p.getNik(), p.getNama(), p.getNoTelp(), kamar, p.getTglMasuk(), tglKeluar
            });
        }
    }
    
    private void tambahPenghuni() {
        try {
            String nik = txtNik.getText().trim();
            String nama = txtNama.getText().trim();
            String noTelp = txtNoTelp.getText().trim();
            String selectedKamar = (String) cmbKamar.getSelectedItem();
            String tglMasuk = txtTglMasuk.getText().trim();
            
            if (nik.isEmpty() || nama.isEmpty() || noTelp.isEmpty() || selectedKamar.equals("-- Pilih Kamar --")) {
                JOptionPane.showMessageDialog(this, "Isi semua field dan pilih kamar!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            // Extract id kamar dari pilihan
            String nomorKamar = selectedKamar.split(" - ")[0];
            int idKamar = getIdKamarByNomor(nomorKamar);
            
            if (PenghuniController.tambahPenghuni(nik, nama, noTelp, idKamar, tglMasuk)) {
                JOptionPane.showMessageDialog(this, "Penghuni berhasil ditambahkan!");
                loadTable();
                loadKamarCombo();
                clearForm();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void editPenghuni() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih penghuni yang akan diedit!");
            return;
        }
        try {
            String nik = txtNik.getText().trim();
            String nama = txtNama.getText().trim();
            String noTelp = txtNoTelp.getText().trim();
            String selectedKamar = (String) cmbKamar.getSelectedItem();
            String tglMasuk = txtTglMasuk.getText().trim();
            
            int idKamar = 0;
            if (!selectedKamar.equals("-- Pilih Kamar --")) {
                String nomorKamar = selectedKamar.split(" - ")[0];
                idKamar = getIdKamarByNomor(nomorKamar);
            }
            
            if (PenghuniController.updatePenghuni(selectedId, nik, nama, noTelp, idKamar, tglMasuk)) {
                JOptionPane.showMessageDialog(this, "Data berhasil diupdate!");
                loadTable();
                loadKamarCombo();
                clearForm();
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
    
    private void keluarkanPenghuni() {
        if (selectedId == -1) {
            JOptionPane.showMessageDialog(this, "Pilih penghuni yang akan dikeluarkan!");
            return;
        }
        int confirm = JOptionPane.showConfirmDialog(this, "Yakin ingin mengeluarkan penghuni ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            String tglKeluar = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
            if (PenghuniController.keluarkanPenghuni(selectedId, tglKeluar)) {
                JOptionPane.showMessageDialog(this, "Penghuni berhasil dikeluarkan!");
                loadTable();
                loadKamarCombo();
                clearForm();
            }
        }
    }
    
    private int getIdKamarByNomor(String nomorKamar) {
        List<Kamar> list = KamarController.getAllKamar();
        for (Kamar k : list) {
            if (k.getNomorKamar().equals(nomorKamar)) return k.getId();
        }
        return 0;
    }
    
    private void clearForm() {
        txtNik.setText("");
        txtNama.setText("");
        txtNoTelp.setText("");
        cmbKamar.setSelectedIndex(0);
        txtTglMasuk.setText(LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE));
        selectedId = -1;
        txtCari.setText("");
    }
}