package controller;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class KamarController {
    
    // CREATE
    public static boolean tambahKamar(String nomorKamar, String tipe, double harga) {
        String sql = "INSERT INTO kamar (nomor_kamar, tipe, harga, status) VALUES (?, ?, ?, 'KOSONG')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nomorKamar);
            ps.setString(2, tipe);
            ps.setDouble(3, harga);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error tambah kamar: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // READ ALL
    public static List<Kamar> getAllKamar() {
        List<Kamar> list = new ArrayList<>();
        String sql = "SELECT * FROM kamar ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Kamar k = new Kamar();
                k.setId(rs.getInt("id"));
                k.setNomorKamar(rs.getString("nomor_kamar"));
                k.setTipe(rs.getString("tipe"));
                k.setHarga(rs.getDouble("harga"));
                k.setStatus(rs.getString("status"));
                list.add(k);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error load kamar: " + e.getMessage());
        }
        return list;
    }
    
    // READ kamar kosong saja
    public static List<Kamar> getKamarKosong() {
        List<Kamar> list = new ArrayList<>();
        String sql = "SELECT * FROM kamar WHERE status = 'KOSONG' ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Kamar k = new Kamar();
                k.setId(rs.getInt("id"));
                k.setNomorKamar(rs.getString("nomor_kamar"));
                k.setTipe(rs.getString("tipe"));
                k.setHarga(rs.getDouble("harga"));
                k.setStatus(rs.getString("status"));
                list.add(k);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return list;
    }
    
    // UPDATE
    public static boolean updateKamar(int id, String nomorKamar, String tipe, double harga) {
        String sql = "UPDATE kamar SET nomor_kamar=?, tipe=?, harga=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nomorKamar);
            ps.setString(2, tipe);
            ps.setDouble(3, harga);
            ps.setInt(4, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error update: " + e.getMessage());
            return false;
        }
    }
    
    // DELETE
    public static boolean hapusKamar(int id) {
        // Cek apakah ada penghuni aktif
        String cekSql = "SELECT COUNT(*) FROM penghuni WHERE id_kamar=? AND tgl_keluar IS NULL";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement psCek = conn.prepareStatement(cekSql)) {
            psCek.setInt(1, id);
            ResultSet rs = psCek.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(null, "Kamar sedang terisi, tidak bisa dihapus!");
                return false;
            }
            String sql = "DELETE FROM kamar WHERE id=?";
            try (PreparedStatement psDel = conn.prepareStatement(sql)) {
                psDel.setInt(1, id);
                return psDel.executeUpdate() > 0;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error hapus: " + e.getMessage());
            return false;
        }
    }
    
    // Update status kamar
    public static boolean updateStatusKamar(int idKamar, String status) {
        String sql = "UPDATE kamar SET status=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, idKamar);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    // Cari kamar berdasarkan nomor
    public static List<Kamar> cariKamar(String keyword) {
        List<Kamar> list = new ArrayList<>();
        String sql = "SELECT * FROM kamar WHERE nomor_kamar LIKE ? OR tipe LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Kamar k = new Kamar();
                k.setId(rs.getInt("id"));
                k.setNomorKamar(rs.getString("nomor_kamar"));
                k.setTipe(rs.getString("tipe"));
                k.setHarga(rs.getDouble("harga"));
                k.setStatus(rs.getString("status"));
                list.add(k);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return list;
    }
}