 package controller;

import model.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class LaporanController {
    
    // Laporan penghuni aktif dengan tagihan
    public static DefaultTableModel getLaporanPenghuniAktif() {
        String[] columns = {"ID", "NIK", "Nama", "No Telp", "Nomor Kamar", "Harga Kamar", "Status Bayar Bulan Ini"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        
        String sql = "SELECT p.id, p.nik, p.nama, p.no_telp, k.nomor_kamar, k.harga " +
                     "FROM penghuni p " +
                     "JOIN kamar k ON p.id_kamar = k.id " +
                     "WHERE p.tgl_keluar IS NULL";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            java.time.LocalDate now = java.time.LocalDate.now();
            String bulanIni = getNamaBulan(now.getMonthValue());
            int tahunIni = now.getYear();
            
            while (rs.next()) {
                int idPenghuni = rs.getInt("id");
                String nik = rs.getString("nik");
                String nama = rs.getString("nama");
                String noTelp = rs.getString("no_telp");
                String nomorKamar = rs.getString("nomor_kamar");
                double harga = rs.getDouble("harga");
                
                // Cek status bayar bulan ini
                String statusBayar = cekStatusBayar(idPenghuni, bulanIni, tahunIni);
                
                Object[] row = {idPenghuni, nik, nama, noTelp, nomorKamar, harga, statusBayar};
                model.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return model;
    }
    
    private static String cekStatusBayar(int idPenghuni, String bulan, int tahun) {
        String sql = "SELECT status FROM pembayaran WHERE id_penghuni=? AND bulan=? AND tahun=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPenghuni);
            ps.setString(2, bulan);
            ps.setInt(3, tahun);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return "BELUM BAYAR";
    }
    
    private static String getNamaBulan(int month) {
        String[] bulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", 
                          "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        return bulan[month - 1];
    }
    
    // Laporan statistik dashboard
    public static int getJumlahKamarTerisi() {
        String sql = "SELECT COUNT(*) FROM kamar WHERE status='TERISI'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public static int getJumlahKamarKosong() {
        String sql = "SELECT COUNT(*) FROM kamar WHERE status='KOSONG'";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public static int getJumlahPenghuniAktif() {
        String sql = "SELECT COUNT(*) FROM penghuni WHERE tgl_keluar IS NULL";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) return rs.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public static double getTotalPendapatanBulanIni() {
        java.time.LocalDate now = java.time.LocalDate.now();
        String bulan = getNamaBulan(now.getMonthValue());
        int tahun = now.getYear();
        return PembayaranController.getTotalPendapatanBulanIni(bulan, tahun);
    }
    
    public static double getTotalPengeluaranBulanIni() {
        java.time.LocalDate now = java.time.LocalDate.now();
        String sql = "SELECT SUM(jumlah) FROM pengeluaran WHERE MONTH(tanggal)=? AND YEAR(tanggal)=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, now.getMonthValue());
            ps.setInt(2, now.getYear());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getDouble(1);
        } catch (SQLException e) {
            // Jika tabel pengeluaran belum ada, ignore dan kembalikan 0
        }
        return 0;
    }
    
    public static double getLabaRugiBulanIni() {
        return getTotalPendapatanBulanIni() - getTotalPengeluaranBulanIni();
    }
}
