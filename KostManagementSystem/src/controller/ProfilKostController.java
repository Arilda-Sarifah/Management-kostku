package controller;

import model.ProfilKost;
import model.DatabaseConnection;
import java.sql.*;
import javax.swing.JOptionPane;

public class ProfilKostController {
    public static ProfilKost getProfil() {
        String sql = "SELECT * FROM profil_kost LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                ProfilKost profil = new ProfilKost();
                profil.setId(rs.getInt("id"));
                profil.setNama(rs.getString("nama"));
                profil.setAlamat(rs.getString("alamat"));
                profil.setPemilik(rs.getString("pemilik"));
                profil.setNoTelp(rs.getString("no_telp"));
                profil.setEmail(rs.getString("email"));
                profil.setDeskripsi(rs.getString("deskripsi"));
                return profil;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error load profil: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        return new ProfilKost();
    }

    public static boolean simpanProfil(ProfilKost profil) {
        if (profil.getId() > 0) {
            String sql = "UPDATE profil_kost SET nama=?, alamat=?, pemilik=?, no_telp=?, email=?, deskripsi=? WHERE id=?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, profil.getNama());
                ps.setString(2, profil.getAlamat());
                ps.setString(3, profil.getPemilik());
                ps.setString(4, profil.getNoTelp());
                ps.setString(5, profil.getEmail());
                ps.setString(6, profil.getDeskripsi());
                ps.setInt(7, profil.getId());
                return ps.executeUpdate() > 0;
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error update profil: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } else {
            String sql = "INSERT INTO profil_kost (nama, alamat, pemilik, no_telp, email, deskripsi) VALUES (?, ?, ?, ?, ?, ?)";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, profil.getNama());
                ps.setString(2, profil.getAlamat());
                ps.setString(3, profil.getPemilik());
                ps.setString(4, profil.getNoTelp());
                ps.setString(5, profil.getEmail());
                ps.setString(6, profil.getDeskripsi());
                int affected = ps.executeUpdate();
                if (affected > 0) {
                    ResultSet rs = ps.getGeneratedKeys();
                    if (rs.next()) {
                        profil.setId(rs.getInt(1));
                    }
                    return true;
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(null, "Error simpan profil: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        }
        return false;
    }
}