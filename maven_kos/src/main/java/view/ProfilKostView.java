package view;

import controller.ProfilKostController;
import model.ProfilKost;
import javax.swing.*;
import java.awt.*;
import util.UIUtilities;

public class ProfilKostView extends JPanel {
    private JTextField txtNama, txtAlamat, txtPemilik, txtNoTelp, txtEmail;
    private JTextArea txtDeskripsi;
    private JButton btnSimpan;
    private JLabel lblPreviewNama, lblPreviewAlamat, lblPreviewPemilik, lblPreviewKontak, lblPreviewDeskripsi;
    private ProfilKost profil;

    public ProfilKostView(DashboardView dashboard) {
        setLayout(new BorderLayout(14, 14));
        setBorder(BorderFactory.createEmptyBorder(14, 14, 14, 14));
        initComponents();
        loadProfil();
    }

    private void initComponents() {
        JPanel mainCard = UIUtilities.createCardPanel(20);
        mainCard.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.BOTH;

        JPanel formCard = UIUtilities.createCardPanel(18);
        formCard.setLayout(new GridBagLayout());
        GridBagConstraints fgbc = new GridBagConstraints();
        fgbc.insets = new Insets(10, 12, 10, 12);
        fgbc.fill = GridBagConstraints.HORIZONTAL;
        fgbc.weightx = 1;

        JLabel title = new JLabel("Profil Kost");
        title.setFont(UIUtilities.FONT_SUBTITLE);
        title.setForeground(UIUtilities.TEXT_PRIMARY);
        fgbc.gridx = 0;
        fgbc.gridy = 0;
        fgbc.gridwidth = 2;
        formCard.add(title, fgbc);

        JLabel subtitle = new JLabel("Simpan detail kost dan lihat pratinjau profil secara langsung.");
        subtitle.setFont(UIUtilities.FONT_REGULAR);
        subtitle.setForeground(UIUtilities.TEXT_SECONDARY);
        fgbc.gridy = 1;
        formCard.add(subtitle, fgbc);

        fgbc.gridwidth = 1;
        txtNama = UIUtilities.createModernTextField("");
        txtAlamat = UIUtilities.createModernTextField("");
        txtPemilik = UIUtilities.createModernTextField("");
        txtNoTelp = UIUtilities.createModernTextField("");
        txtEmail = UIUtilities.createModernTextField("");
        txtDeskripsi = UIUtilities.createModernTextArea(5, 1);

        addFormRow(formCard, fgbc, 2, "Nama Kost    :", txtNama);
        addFormRow(formCard, fgbc, 3, "Alamat       :", txtAlamat);
        addFormRow(formCard, fgbc, 4, "Pemilik      :", txtPemilik);
        addFormRow(formCard, fgbc, 5, "No. Telepon  :", txtNoTelp);
        addFormRow(formCard, fgbc, 6, "Email        :", txtEmail);

        fgbc.gridx = 0;
        fgbc.gridy = 7;
        fgbc.anchor = GridBagConstraints.NORTHWEST;
        formCard.add(new JLabel("Deskripsi:"), fgbc);
        fgbc.gridx = 1;
        fgbc.weighty = 1;
        JScrollPane scrollDesc = new JScrollPane(txtDeskripsi);
        scrollDesc.setBorder(BorderFactory.createEmptyBorder());
        formCard.add(scrollDesc, fgbc);

        // ========== PERBAIKAN BUTTON ==========
        btnSimpan = UIUtilities.createModernButton("Simpan Profil", UIUtilities.PRIMARY_COLOR, Color.WHITE);
        btnSimpan.setPreferredSize(new Dimension(180, 44));
        btnSimpan.setMaximumSize(new Dimension(180, 44));
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(btnSimpan);
        
        // Panel untuk memastikan button tidak terpotong
        JPanel buttonWrapper = new JPanel(new BorderLayout());
        buttonWrapper.setOpaque(false);
        buttonWrapper.add(buttonPanel, BorderLayout.WEST);
        buttonWrapper.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0)); // Tambahkan padding vertical

        // Susun layout utama dengan lebih baik
        // Gunakan BorderLayout untuk mainCard agar button selalu terlihat
        mainCard.setLayout(new BorderLayout(15, 15));
        
        // Panel kiri untuk form
        JPanel leftPanel = new JPanel(new BorderLayout());
        leftPanel.setOpaque(false);
        leftPanel.add(formCard, BorderLayout.CENTER);
        leftPanel.add(buttonWrapper, BorderLayout.SOUTH);  // Button di bagian bawah form
        
        // Panel kanan untuk preview
        JPanel previewPanel = createPreviewPanel();
        previewPanel.setPreferredSize(new Dimension(360, 0));
        
        mainCard.add(leftPanel, BorderLayout.CENTER);
        mainCard.add(previewPanel, BorderLayout.EAST);
        
        add(mainCard, BorderLayout.CENTER);
        
        btnSimpan.addActionListener(e -> simpanProfil());
    }

    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0;
        panel.add(new JLabel(label), gbc);
        gbc.gridx = 1;
        gbc.weightx = 1;
        panel.add(field, gbc);
    }

    private JPanel createPreviewPanel() {
        JPanel previewPanel = UIUtilities.createCardPanel(18);
        previewPanel.setLayout(new BoxLayout(previewPanel, BoxLayout.Y_AXIS));
        previewPanel.setPreferredSize(new Dimension(360, 0));

        JLabel title = new JLabel("Preview Profil");
        title.setFont(UIUtilities.FONT_SUBTITLE);
        title.setForeground(UIUtilities.TEXT_PRIMARY);
        title.setAlignmentX(Component.LEFT_ALIGNMENT);
        previewPanel.add(title);
        previewPanel.add(Box.createVerticalStrut(18));

        lblPreviewNama = createPreviewLabel("Nama       : -");
        lblPreviewAlamat = createPreviewLabel("Alamat     : -");
        lblPreviewPemilik = createPreviewLabel("Pemilik    : -");
        lblPreviewKontak = createPreviewLabel("Kontak     : -");
        lblPreviewDeskripsi = createPreviewLabel("Deskripsi  : -");

        previewPanel.add(lblPreviewNama);
        previewPanel.add(Box.createVerticalStrut(12));
        previewPanel.add(lblPreviewAlamat);
        previewPanel.add(Box.createVerticalStrut(12));
        previewPanel.add(lblPreviewPemilik);
        previewPanel.add(Box.createVerticalStrut(12));
        previewPanel.add(lblPreviewKontak);
        previewPanel.add(Box.createVerticalStrut(12));
        previewPanel.add(lblPreviewDeskripsi);
        
        // Tambahkan spacer di bagian bawah
        previewPanel.add(Box.createVerticalGlue());

        return previewPanel;
    }

    private JLabel createPreviewLabel(String title) {
        JLabel label = new JLabel(title);
        label.setFont(UIUtilities.FONT_REGULAR);
        label.setForeground(UIUtilities.TEXT_SECONDARY);
        label.setAlignmentX(Component.LEFT_ALIGNMENT);
        return label;
    }

    private void loadProfil() {
        profil = ProfilKostController.getProfil();
        txtNama.setText(profil.getNama());
        txtAlamat.setText(profil.getAlamat());
        txtPemilik.setText(profil.getPemilik());
        txtNoTelp.setText(profil.getNoTelp());
        txtEmail.setText(profil.getEmail());
        txtDeskripsi.setText(profil.getDeskripsi());
        refreshPreview();
    }

    private void simpanProfil() {
        profil.setNama(txtNama.getText().trim());
        profil.setAlamat(txtAlamat.getText().trim());
        profil.setPemilik(txtPemilik.getText().trim());
        profil.setNoTelp(txtNoTelp.getText().trim());
        profil.setEmail(txtEmail.getText().trim());
        profil.setDeskripsi(txtDeskripsi.getText().trim());

        if (profil.getNama().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nama Kost tidak boleh kosong!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (ProfilKostController.simpanProfil(profil)) {
            JOptionPane.showMessageDialog(this, "Profil Kost berhasil disimpan!");
            refreshPreview();
        }
    }

    private void refreshPreview() {
        lblPreviewNama.setText("Nama       : " + safe(profil.getNama()));
        lblPreviewAlamat.setText("Alamat     : " + safe(profil.getAlamat()));
        lblPreviewPemilik.setText("Pemilik    : " + safe(profil.getPemilik()));
        lblPreviewKontak.setText("Kontak     : " + safe(profil.getNoTelp()) + " | " + safe(profil.getEmail()));
        lblPreviewDeskripsi.setText("Deskripsi  : " + safe(profil.getDeskripsi()));
    }

    private String safe(String value) {
        return value == null || value.trim().isEmpty() ? "-" : value;
    }
}