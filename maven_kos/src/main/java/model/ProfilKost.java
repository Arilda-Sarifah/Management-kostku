package model;

public class ProfilKost {
    private int id;
    private String nama;
    private String alamat;
    private String pemilik;
    private String noTelp;
    private String email;
    private String deskripsi;

    public ProfilKost() {}

    public ProfilKost(int id, String nama, String alamat, String pemilik, String noTelp, String email, String deskripsi) {
        this.id = id;
        this.nama = nama;
        this.alamat = alamat;
        this.pemilik = pemilik;
        this.noTelp = noTelp;
        this.email = email;
        this.deskripsi = deskripsi;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public String getAlamat() { return alamat; }
    public void setAlamat(String alamat) { this.alamat = alamat; }
    public String getPemilik() { return pemilik; }
    public void setPemilik(String pemilik) { this.pemilik = pemilik; }
    public String getNoTelp() { return noTelp; }
    public void setNoTelp(String noTelp) { this.noTelp = noTelp; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDeskripsi() { return deskripsi; }
    public void setDeskripsi(String deskripsi) { this.deskripsi = deskripsi; }
}