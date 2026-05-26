package model;

public class Pengeluaran {
    private int id;
    private String tanggal;
    private String deskripsi;
    private double jumlah;
    private String kategori;

    public Pengeluaran() {}

    public Pengeluaran(int id, String tanggal, String deskripsi, double jumlah, String kategori) {
        this.id = id;
        this.tanggal = tanggal;
        this.deskripsi = deskripsi;
        this.jumlah = jumlah;
        this.kategori = kategori;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getTanggal() { return tanggal; }
    public void setTanggal(String tanggal) { this.tanggal = tanggal; }
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
    public double getJumlah() { return jumlah; }
    public void setJumlah(double jumlah) { this.jumlah = jumlah; }
    public String getKategori() { return kategori; }
    public void setKategori(String kategori) { this.kategori = kategori; }
}