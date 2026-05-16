# KOST MANAGEMENT SYSTEM

## Gambaran Umum
Aplikasi Sistem Manajemen Kost adalah platform desktop berbasis Java Swing yang dirancang untuk mengelola operasional kos-kosan dengan efisien. Aplikasi ini dilengkapi dengan fitur authentication modern, desain UI yang menarik, dan error handling yang komprehensif.

## Fitur Utama

### 1. Authentication System
- **Login**: Pengguna dapat login dengan username dan password yang aman (SHA-256 hashing)
- **Register**: Mendaftar akun baru dengan validasi data yang ketat
- **Session Management**: Manajemen session pengguna yang aman
- **Password Hashing**: Password disimpan dengan enkripsi SHA-256

### 2. Desain UI Modern
- **Tema Warna**: Hijau (#4CAF50) dan Oranye (#FF9800) dengan desain minimalis
- **Responsive Design**: Antarmuka yang dapat menyesuaikan dengan ukuran layar
- **Animasi**: Berbagai animasi halus untuk meningkatkan UX
- **Dark Mode Support**: Sidebar dan top bar dengan tema gelap

### 3. Dashboard Analytics
- Total Kamar
- Kamar Terisi vs Kosong
- Penghuni Aktif
- Pendapatan Bulan Ini
- Status Sistem Real-time

### 4. Manajemen Kamar
- Tambah/Edit/Hapus kamar
- Kategorisasi tipe kamar
- Tracking status kamar (Kosong/Terisi)
- Manajemen harga kamar

### 5. Manajemen Penghuni
- Data penghuni lengkap
- Riwayat penghuni
- Kontak informasi
- Status penghuni aktif/tidak aktif

### 6. Pembayaran & Keuangan
- Pencatatan pembayaran sewa
- Laporan pembayaran
- Tracking pengeluaran
- Rekapitulasi keuangan bulanan

### 7. Error Handling
- Exception handling komprehensif
- Validasi input data
- Pesan error yang informatif
- Log file untuk tracking error

## Teknologi yang Digunakan

- **Java 11+**: Bahasa pemrograman utama
- **Swing**: Framework untuk UI desktop
- **MySQL**: Database backend
- **JDBC**: Database connectivity

## Struktur Project

```
KostManagementSystem/
├── src/
│   ├── MainApp.java                          # Entry point aplikasi
│   ├── model/
│   │   ├── User.java                         # Model pengguna
│   │   ├── DatabaseConnection.java           # Koneksi database
│   │   ├── Kamar.java                        # Model kamar
│   │   ├── Penghuni.java                     # Model penghuni
│   │   ├── Pembayaran.java                   # Model pembayaran
│   │   ├── Pengeluaran.java                  # Model pengeluaran
│   │   └── ProfilKost.java                   # Model profil kost
│   ├── controller/
│   │   ├── AuthController.java               # Logic autentikasi
│   │   ├── KamarController.java              # Logic manajemen kamar
│   │   ├── PenghuniController.java           # Logic manajemen penghuni
│   │   ├── PembayaranController.java         # Logic pembayaran
│   │   ├── PengeluaranController.java        # Logic pengeluaran
│   │   ├── ProfilKostController.java         # Logic profil
│   │   └── LaporanController.java            # Logic laporan
│   ├── view/
│   │   ├── LoginView.java                    # UI login
│   │   ├── RegisterView.java                 # UI register
│   │   ├── DashboardView.java                # UI dashboard
│   │   ├── KamarView.java                    # UI manajemen kamar
│   │   ├── PenghuniView.java                 # UI manajemen penghuni
│   │   ├── PembayaranView.java               # UI pembayaran
│   │   ├── PengeluaranView.java              # UI pengeluaran
│   │   ├── ProfilKostView.java               # UI profil kost
│   │   └── LaporanView.java                  # UI laporan
│   └── util/
│       ├── UIUtilities.java                  # Utility UI & styling
│       ├── AnimationUtilities.java           # Utility animasi
│       └── ExceptionHandler.java             # Error handling
└── lib/
    └── mysql-connector-j-9.7.0/              # MySQL driver
```

## Setup & Installation

### Prerequisites
- Java Development Kit (JDK) 11 atau lebih tinggi
- MySQL Server 5.7 atau lebih tinggi
- IDE: IntelliJ IDEA, Eclipse, atau VS Code dengan extension Java

### Langkah-langkah Setup

1. **Clone/Download Project**
   ```bash
   git clone <repo-url>
   cd KostManagementSystem
   ```

2. **Setup Database**
   ```sql
   CREATE DATABASE db_kostku;
   ```
   
   Atau biarkan aplikasi membuat tabel secara otomatis saat pertama kali dijalankan.

3. **Update Database Configuration**
   Edit file `src/model/DatabaseConnection.java`:
   ```java
   private static final String URL = "jdbc:mysql://localhost:3306/db_kostku";
   private static final String USER = "root";
   private static final String PASSWORD = "your_password"; // Masukkan password MySQL Anda
   ```

4. **Kompile Project**
   ```bash
   javac -d bin -cp lib/mysql-connector-j-9.7.0/mysql-connector-j-9.7.0.jar src/**/*.java
   ```

5. **Jalankan Aplikasi**
   ```bash
   java -cp bin:lib/mysql-connector-j-9.7.0/mysql-connector-j-9.7.0.jar MainApp
   ```

## Cara Penggunaan

### 1. Login & Register
- **Pertama Kali**: Klik "DAFTAR AKUN BARU" untuk membuat akun
- **Validasi**: Masukkan data yang valid (email, password minimal 6 karakter)
- **Login**: Gunakan username dan password yang terdaftar

### 2. Dashboard
- Melihat ringkasan informasi kost
- Quick stats: Total kamar, status occupancy, pendapatan

### 3. Manajemen Kamar
- Tambah kamar baru dengan nomor, tipe, dan harga
- Edit informasi kamar
- Hapus kamar (jika belum terisi)

### 4. Manajemen Penghuni
- Input data penghuni baru
- Tracking tanggal masuk/keluar
- Kontak informasi penghuni

### 5. Pembayaran
- Catat pembayaran sewa penghuni
- Tracking status pembayaran
- Laporan pembayaran per bulan

### 6. Laporan
- Laporan keuangan bulanan
- Export data ke file

## Fitur Keamanan

1. **Password Hashing**: SHA-256 encryption
2. **Input Validation**: Validasi semua input dari user
3. **SQL Injection Prevention**: Menggunakan prepared statements
4. **Session Management**: Track current logged-in user
5. **Error Logging**: Semua error dicatat ke file log

## Customization

### Mengubah Warna Tema
Edit file `src/util/UIUtilities.java`:
```java
public static final Color PRIMARY_COLOR = new Color(76, 175, 80);       // Ubah warna hijau
public static final Color SECONDARY_COLOR = new Color(255, 152, 0);     // Ubah warna oranye
```

### Mengubah Font
```java
public static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 24);
```

### Menambah Menu Baru
Edit `DashboardView.java` dan tambahkan menu item baru di bagian `createSidebar()`:
```java
String[] menus = {"📊 Dashboard", "🚪 Kamar Baru", ...};
```

## Troubleshooting

### Database Connection Error
- Pastikan MySQL server berjalan
- Cek konfigurasi database di DatabaseConnection.java
- Pastikan database `db_kostku` sudah dibuat

### Class Not Found Error
- Pastikan MySQL driver berada di lib folder
- Update classpath dengan benar saat kompile

### UI Tidak Menampil dengan Baik
- Update Swing Look and Feel di MainApp.java
- Pastikan ukuran layar cukup (minimal 1200x700)

## Kontribusi

Untuk menambah fitur atau melaporkan bug:
1. Fork repository
2. Buat branch fitur baru
3. Commit perubahan
4. Push ke branch
5. Buat Pull Request

## Lisensi

Project ini menggunakan lisensi MIT.

## Support & Contact

Untuk bantuan lebih lanjut, silakan buka issue di repository atau hubungi developer.

---

**Version**: 1.0.0  
**Last Updated**: 2026  
**Status**: Active Development
