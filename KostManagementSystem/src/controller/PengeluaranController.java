package controller;

import model.Pengeluaran;
import model.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class PengeluaranController {
    public static boolean tambahPengeluaran(String tanggal, String deskripsi, double jumlah, String kategori) {
        String sql = "INSERT INTO pengeluaran (tanggal, deskripsi, jumlah, kategori) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, tanggal);
            ps.setString(2, deskripsi);
            ps.setDouble(3, jumlah);
            ps.setString(4, kategori);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error tambah pengeluaran: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static List<Pengeluaran> getAllPengeluaran() {
        List<Pengeluaran> list = new ArrayList<>();
        String sql = "SELECT * FROM pengeluaran ORDER BY tanggal DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Pengeluaran p = new Pengeluaran();
                p.setId(rs.getInt("id"));
                p.setTanggal(rs.getString("tanggal"));
                p.setDeskripsi(rs.getString("deskripsi"));
                p.setJumlah(rs.getDouble("jumlah"));
                p.setKategori(rs.getString("kategori"));
                list.add(p);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error load pengeluaran: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return list;
    }

    public static boolean hapusPengeluaran(int id) {
        String sql = "DELETE FROM pengeluaran WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error hapus pengeluaran: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}