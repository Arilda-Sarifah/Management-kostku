package model;

public class Penghuni {
    private int id;
    private String nik;
    private String nama;
    private String noTelp;
    private int idKamar;
    private String tglMasuk;
    private String tglKeluar;
    
    public Penghuni() {}
    
    public Penghuni(int id, String nik, String nama, String noTelp, int idKamar, String tglMasuk, String tglKeluar) {
        this.id = id;
        this.nik = nik;
        this.nama = nama;
        this.noTelp = noTelp;
        this.idKamar = idKamar;
        this.tglMasuk = tglMasuk;
        this.tglKeluar = tglKeluar;
    }
    
    // Getter Setter
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    
    public String getNik() { return nik; }
    public void setNik(String nik) { this.nik = nik; }
    
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    
    public String getNoTelp() { return noTelp; }
    public void setNoTelp(String noTelp) { this.noTelp = noTelp; }
    
    public int getIdKamar() { return idKamar; }
    public void setIdKamar(int idKamar) { this.idKamar = idKamar; }
    
    public String getTglMasuk() { return tglMasuk; }
    public void setTglMasuk(String tglMasuk) { this.tglMasuk = tglMasuk; }
    
    public String getTglKeluar() { return tglKeluar; }
    public void setTglKeluar(String tglKeluar) { this.tglKeluar = tglKeluar; }
} 
