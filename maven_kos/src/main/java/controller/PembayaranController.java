package controller;

import model.*;
import java.sql.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;

public class PembayaranController {
    
    private static String formatCurrency(double value) {
        return NumberFormat.getCurrencyInstance(new Locale("id", "ID")).format(value);
    }
    
    // CREATE atau UPDATE pembayaran (jika sudah ada)
    public static boolean tambahPembayaran(int idPenghuni, String bulan, int tahun, int lamaBulan, double jumlah, String tglBayar) {
        double hargaSewa = getHargaKamarByPenghuni(idPenghuni);
        double totalTagihan = hargaSewa * lamaBulan;
        
        // Cek apakah sudah ada pembayaran untuk penghuni dan bulan/tahun ini
        Pembayaran existing = getPembayaranExisting(idPenghuni, bulan, tahun);
        
        if (existing != null) {
            // Update pembayaran yang sudah ada
            return updatePembayaran(existing.getId(), idPenghuni, bulan, tahun, lamaBulan, jumlah, tglBayar, hargaSewa);
        } else {
            // Buat pembayaran baru
            return insertPembayaran(idPenghuni, bulan, tahun, lamaBulan, jumlah, tglBayar, hargaSewa);
        }
    }
    
    // Insert pembayaran baru
    private static boolean insertPembayaran(int idPenghuni, String bulan, int tahun, int lamaBulan, double jumlah, String tglBayar, double hargaSewa) {
        double totalTagihan = hargaSewa * lamaBulan;
        double kurangBayar = totalTagihan - jumlah;
        String status;
        String keterangan;
        
        if (kurangBayar <= 0) {
            status = "LUNAS";
            if (kurangBayar < 0) {
                keterangan = "Kelebihan bayar " + formatCurrency(Math.abs(kurangBayar));
                kurangBayar = 0;
            } else {
                keterangan = "Lunas";
            }
        } else {
            status = "BELUM";
            keterangan = "Kurang " + formatCurrency(kurangBayar);
        }

        String sql = "INSERT INTO pembayaran (id_penghuni, bulan, tahun, lama_bulan, jumlah, total_tagihan, kurang_bayar, tgl_bayar, status, keterangan) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPenghuni);
            ps.setString(2, bulan);
            ps.setInt(3, tahun);
            ps.setInt(4, lamaBulan);
            ps.setDouble(5, jumlah);
            ps.setDouble(6, totalTagihan);
            ps.setDouble(7, kurangBayar);
            ps.setString(8, tglBayar);
            ps.setString(9, status);
            ps.setString(10, keterangan);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return false;
        }
    }
    
    // Update pembayaran yang sudah ada
    private static boolean updatePembayaran(int idPembayaran, int idPenghuni, String bulan, int tahun, int lamaBulan, double jumlahTambahan, String tglBayar, double hargaSewa) {
        // Ambil data pembayaran yang sudah ada
        Pembayaran existing = getPembayaranById(idPembayaran);
        if (existing == null) return false;
        
        // Hitung total baru
        double jumlahBaru = existing.getJumlah() + jumlahTambahan;
        double totalTagihan = hargaSewa * lamaBulan;
        double kurangBayar = totalTagihan - jumlahBaru;
        
        String status;
        String keterangan;
        if (kurangBayar <= 0) {
            status = "LUNAS";
            if (kurangBayar < 0) {
                keterangan = "Kelebihan bayar " + formatCurrency(Math.abs(kurangBayar));
                kurangBayar = 0;
            } else {
                keterangan = "Lunas";
            }
        } else {
            status = "BELUM";
            keterangan = "Kurang " + formatCurrency(kurangBayar);
        }
        
        String sql = "UPDATE pembayaran SET jumlah = ?, total_tagihan = ?, kurang_bayar = ?, tgl_bayar = ?, status = ?, keterangan = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setDouble(1, jumlahBaru);
            ps.setDouble(2, totalTagihan);
            ps.setDouble(3, kurangBayar);
            ps.setString(4, tglBayar);
            ps.setString(5, status);
            ps.setString(6, keterangan);
            ps.setInt(7, idPembayaran);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error update pembayaran: " + e.getMessage());
            return false;
        }
    }
    
    // Cek apakah sudah ada pembayaran untuk periode tersebut
    private static Pembayaran getPembayaranExisting(int idPenghuni, String bulan, int tahun) {
        String sql = "SELECT * FROM pembayaran WHERE id_penghuni = ? AND bulan = ? AND tahun = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPenghuni);
            ps.setString(2, bulan);
            ps.setInt(3, tahun);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Pembayaran p = new Pembayaran();
                p.setId(rs.getInt("id"));
                p.setIdPenghuni(rs.getInt("id_penghuni"));
                p.setBulan(rs.getString("bulan"));
                p.setTahun(rs.getInt("tahun"));
                p.setLamaBulan(rs.getInt("lama_bulan"));
                p.setJumlah(rs.getDouble("jumlah"));
                p.setTotalTagihan(rs.getDouble("total_tagihan"));
                p.setKurangBayar(rs.getDouble("kurang_bayar"));
                p.setTglBayar(rs.getString("tgl_bayar"));
                p.setStatus(rs.getString("status"));
                p.setKeterangan(rs.getString("keterangan"));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // Get pembayaran by ID
    private static Pembayaran getPembayaranById(int id) {
        String sql = "SELECT * FROM pembayaran WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                Pembayaran p = new Pembayaran();
                p.setId(rs.getInt("id"));
                p.setIdPenghuni(rs.getInt("id_penghuni"));
                p.setBulan(rs.getString("bulan"));
                p.setTahun(rs.getInt("tahun"));
                p.setLamaBulan(rs.getInt("lama_bulan"));
                p.setJumlah(rs.getDouble("jumlah"));
                p.setTotalTagihan(rs.getDouble("total_tagihan"));
                p.setKurangBayar(rs.getDouble("kurang_bayar"));
                p.setTglBayar(rs.getString("tgl_bayar"));
                p.setStatus(rs.getString("status"));
                p.setKeterangan(rs.getString("keterangan"));
                return p;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    // READ ALL dengan JOIN nama penghuni
    public static List<Object[]> getAllPembayaran() {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT p.id, pn.nama, p.bulan, p.tahun, p.lama_bulan, p.jumlah, p.total_tagihan, p.kurang_bayar, p.tgl_bayar, p.status, p.keterangan " +
                     "FROM pembayaran p JOIN penghuni pn ON p.id_penghuni = pn.id ORDER BY p.tahun DESC, FIELD(p.bulan, 'Januari','Februari','Maret','April','Mei','Juni','Juli','Agustus','September','Oktober','November','Desember')";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Object[] row = new Object[11];
                row[0] = rs.getInt("id");
                row[1] = rs.getString("nama");
                row[2] = rs.getString("bulan");
                row[3] = rs.getInt("tahun");
                row[4] = rs.getInt("lama_bulan");
                row[5] = rs.getDouble("jumlah");
                row[6] = rs.getDouble("total_tagihan");
                row[7] = rs.getDouble("kurang_bayar");
                row[8] = rs.getString("tgl_bayar");
                row[9] = rs.getString("status");
                row[10] = rs.getString("keterangan");
                list.add(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return list;
    }
    
    // Get pembayaran per penghuni
    public static List<Object[]> getPembayaranByPenghuni(int idPenghuni) {
        List<Object[]> list = new ArrayList<>();
        String sql = "SELECT id, bulan, tahun, lama_bulan, jumlah, total_tagihan, kurang_bayar, tgl_bayar, status, keterangan FROM pembayaran WHERE id_penghuni=? ORDER BY tahun DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPenghuni);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Object[] row = new Object[10];
                row[0] = rs.getInt("id");
                row[1] = rs.getString("bulan");
                row[2] = rs.getInt("tahun");
                row[3] = rs.getInt("lama_bulan");
                row[4] = rs.getDouble("jumlah");
                row[5] = rs.getDouble("total_tagihan");
                row[6] = rs.getDouble("kurang_bayar");
                row[7] = rs.getString("tgl_bayar");
                row[8] = rs.getString("status");
                row[9] = rs.getString("keterangan");
                list.add(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
        }
        return list;
    }
    
    // Cek apakah sudah bayar lunas untuk bulan tertentu
    public static boolean sudahBayar(int idPenghuni, String bulan, int tahun) {
        String sql = "SELECT jumlah, total_tagihan FROM pembayaran WHERE id_penghuni=? AND bulan=? AND tahun=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPenghuni);
            ps.setString(2, bulan);
            ps.setInt(3, tahun);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                double jumlah = rs.getDouble("jumlah");
                double totalTagihan = rs.getDouble("total_tagihan");
                return jumlah >= totalTagihan;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // Get sisa tagihan
    public static double getSisaTagihan(int idPenghuni, String bulan, int tahun) {
        String sql = "SELECT total_tagihan - jumlah AS sisa FROM pembayaran WHERE id_penghuni=? AND bulan=? AND tahun=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPenghuni);
            ps.setString(2, bulan);
            ps.setInt(3, tahun);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("sisa");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Belum ada pembayaran
    }
    
    // DELETE
    public static boolean hapusPembayaran(int id) {
        String sql = "DELETE FROM pembayaran WHERE id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage());
            return false;
        }
    }
    
    // Total pendapatan bulan ini
    public static double getTotalPendapatanBulanIni(String bulan, int tahun) {
        String sql = "SELECT SUM(jumlah) FROM pembayaran WHERE bulan=? AND tahun=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, bulan);
            ps.setInt(2, tahun);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    public static double getHargaKamarByPenghuni(int idPenghuni) {
        String sql = "SELECT k.harga FROM penghuni p JOIN kamar k ON p.id_kamar = k.id WHERE p.id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idPenghuni);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getDouble("harga");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}