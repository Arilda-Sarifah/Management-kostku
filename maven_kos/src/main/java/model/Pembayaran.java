package model;

public class Pembayaran extends BaseEntity implements Reportable {
    private int idPenghuni;
    private String bulan;
    private int tahun;
    private int lamaBulan;
    private double jumlah;
    private double totalTagihan;
    private double kurangBayar;
    private String tglBayar;
    private String status;
    private String keterangan;
    
    // Constructor
    public Pembayaran() {}
    
    public Pembayaran(int id, int idPenghuni, String bulan, int tahun, int lamaBulan, 
                      double jumlah, double totalTagihan, double kurangBayar, 
                      String tglBayar, String status, String keterangan) {
        this.id = id;
        this.idPenghuni = idPenghuni;
        this.bulan = bulan;
        this.tahun = tahun;
        this.lamaBulan = lamaBulan;
        this.jumlah = jumlah;
        this.totalTagihan = totalTagihan;
        this.kurangBayar = kurangBayar;
        this.tglBayar = tglBayar;
        this.status = status;
        this.keterangan = keterangan;
    }
    
    // Getters and Setters
    @Override
    public int getId() { return id; }
    @Override
    public void setId(int id) { this.id = id; }
    
    public int getIdPenghuni() { return idPenghuni; }
    public void setIdPenghuni(int idPenghuni) { this.idPenghuni = idPenghuni; }
    
    public String getBulan() { return bulan; }
    public void setBulan(String bulan) { this.bulan = bulan; }
    
    public int getTahun() { return tahun; }
    public void setTahun(int tahun) { this.tahun = tahun; }
    
    public int getLamaBulan() { return lamaBulan; }
    public void setLamaBulan(int lamaBulan) { this.lamaBulan = lamaBulan; }
    
    public double getJumlah() { return jumlah; }
    public void setJumlah(double jumlah) { this.jumlah = jumlah; }
    
    public double getTotalTagihan() { return totalTagihan; }
    public void setTotalTagihan(double totalTagihan) { this.totalTagihan = totalTagihan; }
    
    public double getKurangBayar() { return kurangBayar; }
    public void setKurangBayar(double kurangBayar) { this.kurangBayar = kurangBayar; }
    
    public String getTglBayar() { return tglBayar; }
    public void setTglBayar(String tglBayar) { this.tglBayar = tglBayar; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public String getKeterangan() { return keterangan; }
    public void setKeterangan(String keterangan) { this.keterangan = keterangan; }

    @Override
    public String toReportString() {
        return String.format("%d,%d,%s,%d,%d,%.0f,%.0f,%.0f,%s,%s", 
                id, idPenghuni, bulan, tahun, lamaBulan, jumlah, totalTagihan, kurangBayar, tglBayar, status);
    }
}