package model;

public class Kamar {
    private int id;
    private String nomorKamar;
    private String tipe;
    private double harga;
    private String status;
    
    public Kamar() {}
    
    public Kamar(int id, String nomorKamar, String tipe, double harga, String status) {
        this.id = id;
        this.nomorKamar = nomorKamar;
        this.tipe = tipe;
        this.harga = harga;
        this.status = status;
    }
    
    // Getter dan Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNomorKamar() { return nomorKamar; }
    public void setNomorKamar(String nomorKamar) { this.nomorKamar = nomorKamar; }
    
    public String getTipe() { return tipe; }
    public void setTipe(String tipe) { this.tipe = tipe; }
    
    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    @Override
    public String toString() {
        return nomorKamar + " - " + tipe + " (Rp " + harga + ")";
    }
} 
