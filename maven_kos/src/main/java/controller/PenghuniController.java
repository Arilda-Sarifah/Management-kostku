 package controller;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PenghuniController {
    
    // CREATE
    public static boolean tambahPenghuni(String nik, String nama, String noTelp, int idKamar, String tglMasuk) {
        String sql = "INSERT INTO penghuni (nik, nama, no_telp, id_kamar, tgl_masuk) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nik);
            ps.setString(2, nama);
            ps.setString(3, noTelp);
            ps.setInt(4, idKamar);
            ps.setString(5, tglMasuk);
            
            boolean berhasil = ps.executeUpdate() > 0;
            if (berhasil && idKamar > 0) {
                // Update status kamar menjadi TERISI
                KamarController.updateStatusKamar(idKamar, "TERISI");
            }
            return berhasil;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return false;
        }
    }
    
    // READ ALL
    public static List<Penghuni> getAllPenghuni() {
        List<Penghuni> list = new ArrayList<>();
        String sql = "SELECT * FROM penghuni ORDER BY id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Penghuni p = new Penghuni();
                p.setId(rs.getInt("id"));
                p.setNik(rs.getString("nik"));
                p.setNama(rs.getString("nama"));
                p.setNoTelp(rs.getString("no_telp"));
                p.setIdKamar(rs.getInt("id_kamar"));
                p.setTglMasuk(rs.getString("tgl_masuk"));
                p.setTglKeluar(rs.getString("tgl_keluar"));
                list.add(p);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return list;
    }
    
    // READ penghuni aktif (belum keluar)
    public static List<Penghuni> getPenghuniAktif() {
        List<Penghuni> list = new ArrayList<>();
        String sql = "SELECT p.*, k.nomor_kamar FROM penghuni p " +
                     "LEFT JOIN kamar k ON p.id_kamar = k.id " +
                     "WHERE p.tgl_keluar IS NULL ORDER BY p.id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Penghuni p = new Penghuni();
                p.setId(rs.getInt("id"));
                p.setNik(rs.getString("nik"));
                p.setNama(rs.getString("nama"));
                p.setNoTelp(rs.getString("no_telp"));
                p.setIdKamar(rs.getInt("id_kamar"));
                p.setTglMasuk(rs.getString("tgl_masuk"));
                p.setTglKeluar(rs.getString("tgl_keluar"));
                list.add(p);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return list;
    }
    
    // UPDATE
    public static boolean updatePenghuni(int id, String nik, String nama, String noTelp, int idKamar, String tglMasuk) {
        // Dapatkan id_kamar lama
        int oldKamar = getIdKamarByPenghuni(id);
        String sql = "UPDATE penghuni SET nik=?, nama=?, no_telp=?, id_kamar=?, tgl_masuk=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nik);
            ps.setString(2, nama);
            ps.setString(3, noTelp);
            ps.setInt(4, idKamar);
            ps.setString(5, tglMasuk);
            ps.setInt(6, id);
            
            boolean berhasil = ps.executeUpdate() > 0;
            if (berhasil) {
                // Update status kamar
                if (oldKamar != idKamar) {
                    if (oldKamar > 0) KamarController.updateStatusKamar(oldKamar, "KOSONG");
                    if (idKamar > 0) KamarController.updateStatusKamar(idKamar, "TERISI");
                }
            }
            return berhasil;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return false;
        }
    }
    
    // DELETE (soft delete dengan tgl_keluar)
    public static boolean keluarkanPenghuni(int idPenghuni, String tglKeluar) {
        int idKamar = getIdKamarByPenghuni(idPenghuni);
        String sql = "UPDATE penghuni SET tgl_keluar=? WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tglKeluar);
            ps.setInt(2, idPenghuni);
            boolean berhasil = ps.executeUpdate() > 0;
            if (berhasil && idKamar > 0) {
                KamarController.updateStatusKamar(idKamar, "KOSONG");
            }
            return berhasil;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return false;
        }
    }
    
    // Helper: ambil id_kamar dari penghuni
    private static int getIdKamarByPenghuni(int idPenghuni) {
        String sql = "SELECT id_kamar FROM penghuni WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPenghuni);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("id_kamar");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Cari penghuni
    public static List<Penghuni> cariPenghuni(String keyword) {
        List<Penghuni> list = new ArrayList<>();
        String sql = "SELECT * FROM penghuni WHERE nama LIKE ? OR nik LIKE ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, "%" + keyword + "%");
            ps.setString(2, "%" + keyword + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Penghuni p = new Penghuni();
                p.setId(rs.getInt("id"));
                p.setNik(rs.getString("nik"));
                p.setNama(rs.getString("nama"));
                p.setNoTelp(rs.getString("no_telp"));
                p.setIdKamar(rs.getInt("id_kamar"));
                p.setTglMasuk(rs.getString("tgl_masuk"));
                p.setTglKeluar(rs.getString("tgl_keluar"));
                list.add(p);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return list;
    }
    
    public static Penghuni getPenghuniById(int id) {
        String sql = "SELECT * FROM penghuni WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Penghuni p = new Penghuni();
                p.setId(rs.getInt("id"));
                p.setNik(rs.getString("nik"));
                p.setNama(rs.getString("nama"));
                p.setNoTelp(rs.getString("no_telp"));
                p.setIdKamar(rs.getInt("id_kamar"));
                p.setTglMasuk(rs.getString("tgl_masuk"));
                p.setTglKeluar(rs.getString("tgl_keluar"));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
