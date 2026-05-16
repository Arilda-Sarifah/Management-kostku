# SETUP GUIDE - Kost Management System

## Prasyarat
- **Java Development Kit (JDK)** 11 atau lebih tinggi
- **MySQL Server** 5.7 atau lebih tinggi
- **IDE** (IntelliJ IDEA, Eclipse, VS Code dengan Java extension)

---

## Step 1: Verifikasi Instalasi Java

Buka Command Prompt / Terminal dan ketik:

```bash
java -version
javac -version
```

Jika belum terinstall, download dari: https://www.oracle.com/java/technologies/downloads/

---

## Step 2: Instalasi & Konfigurasi MySQL

### Windows
1. Download MySQL dari: https://dev.mysql.com/downloads/mysql/
2. Jalankan installer
3. Pilih "Server Machine" setup type
4. Default port: 3306
5. Konfigurasikan MySQL sebagai Windows Service
6. Username: `root`, Password: (biarkan kosong untuk testing)

### Verify MySQL Installation
```bash
mysql --version
```

### Start MySQL Server
```bash
# Windows
net start MySQL80

# macOS
brew services start mysql

# Linux
sudo service mysql start
```

---

## Step 3: Setup Database

### Option A: Menggunakan MySQL Command Line

1. Buka MySQL Command Line Client
2. Login: `mysql -u root` (tanpa password atau dengan password Anda)
3. Jalankan SQL script:

```bash
mysql -u root < database_setup.sql
```

Atau copy-paste script dari `database_setup.sql` di MySQL client.

### Option B: Menggunakan Aplikasi (Otomatis)
Aplikasi akan membuat tabel secara otomatis saat first run.

---

## Step 4: Konfigurasi Database Connection

Edit file: `src/model/DatabaseConnection.java`

```java
private static final String URL = "jdbc:mysql://localhost:3306/db_kostku";
private static final String USER = "root";
private static final String PASSWORD = ""; // Ubah dengan password MySQL Anda
```

**Contoh dengan password:**
```java
private static final String PASSWORD = "mysql123456";
```

---

## Step 5: Kompilasi Aplikasi

### Menggunakan Command Line

```bash
# Navigate ke project root directory
cd KostManagementSystem

# Compile semua Java files
javac -d bin -cp lib/mysql-connector-j-9.7.0/mysql-connector-j-9.7.0.jar src/MainApp.java src/**/*.java
```

### Menggunakan IDE

#### IntelliJ IDEA
1. Open Project → Pilih folder `KostManagementSystem`
2. File → Project Structure
3. Library → Tambahkan `mysql-connector-j-9.7.0.jar`
4. Build → Make Project (Ctrl+F9)

#### Eclipse
1. New → Java Project
2. Configure Build Path → Add External JARs
3. Tambahkan `mysql-connector-j-9.7.0.jar`
4. Project → Build All

#### VS Code
1. Install Extension: Extension Pack for Java
2. Buka Integrated Terminal
3. Kompile dengan command line di atas

---

## Step 6: Menjalankan Aplikasi

### Menggunakan Command Line

```bash
java -cp bin:lib/mysql-connector-j-9.7.0/mysql-connector-j-9.7.0.jar MainApp
```

**Windows (gunakan semicolon):**
```bash
java -cp bin;lib/mysql-connector-j-9.7.0/mysql-connector-j-9.7.0.jar MainApp
```

### Menggunakan IDE

- **IntelliJ**: Run → Run 'MainApp'
- **Eclipse**: Run → Run As → Java Application
- **VS Code**: Buka MainApp.java → Run (Code Lens)

---

## Step 7: First Time Login

1. Aplikasi akan menampilkan **LoginView**
2. Klik **"DAFTAR AKUN BARU"** untuk membuat akun
3. Isi form:
   - **Nama Lengkap**: Nama Anda
   - **Username**: Username unik (minimal 3 karakter)
   - **Email**: Email valid
   - **Password**: Minimal 6 karakter
   - **Konfirmasi Password**: Ulangi password

4. Klik **"DAFTAR"**
5. Setelah berhasil, login dengan username dan password yang terbuat
6. Dashboard akan menampilkan aplikasi utama

---

## Troubleshooting

### Error: "MySQL JDBC Driver tidak ditemukan"
- Pastikan `mysql-connector-j-9.7.0.jar` berada di folder `lib/`
- Verify classpath saat compile dan run

### Error: "Gagal terhubung ke database"
- MySQL server tidak berjalan
  ```bash
  # Cek status
  mysql -u root -e "SELECT 1"
  ```
- Database `db_kostku` belum dibuat
  ```bash
  mysql -u root -e "CREATE DATABASE db_kostku;"
  ```
- Username/password salah di `DatabaseConnection.java`

### Error: "Duplicate entry for key 'username'"
- Username sudah terdaftar
- Gunakan username yang berbeda saat register

### Error: "Email tidak valid"
- Format email harus: `user@domain.com`
- Contoh: `john@example.com`

### Error: "Password minimal 6 karakter"
- Gunakan password dengan minimal 6 karakter alfanumerik

### UI Tidak Tampil dengan Benar
- Ukuran layar terlalu kecil (minimal 1200x700)
- Update Graphics Driver
- Coba dengan Java versi yang lebih baru

---

## Create JAR File (Opsional)

Untuk membuat executable JAR file:

```bash
# Create manifest file
echo "Main-Class: MainApp" > manifest.txt

# Create JAR
jar cmf manifest.txt KostManager.jar -C bin . lib/mysql-connector-j-9.7.0/mysql-connector-j-9.7.0.jar

# Run JAR
java -jar KostManager.jar
```

---

## Development Tips

### Mengubah Tema Warna
Edit `src/util/UIUtilities.java`:
```java
public static final Color PRIMARY_COLOR = new Color(76, 175, 80);       // Hijau
public static final Color SECONDARY_COLOR = new Color(255, 152, 0);     // Oranye
```

### Debug Mode
Tambahkan di `MainApp.java`:
```java
System.out.println("Debug: Database connection successful");
```

### Enable Debug Logging
```bash
java -cp bin:lib/mysql-connector-j-9.7.0/mysql-connector-j-9.7.0.jar -Xmx512m -Xms256m MainApp
```

---

## System Requirements

| Komponen | Minimum | Recommended |
|----------|---------|-------------|
| RAM | 512 MB | 2 GB |
| Disk Space | 500 MB | 1 GB |
| Display Resolution | 1024x768 | 1920x1080 |
| Java Version | 11 | 17+ |
| MySQL Version | 5.7 | 8.0+ |

---

## FAQ

**Q: Apakah harus connect ke internet?**  
A: Tidak, aplikasi berjalan offline setelah database setup.

**Q: Bisa multi-user access secara bersamaan?**  
A: Ya, dengan multiple instances atau melalui network jika dikonfigurasi.

**Q: Bagaimana backup data?**  
A: Gunakan `mysqldump`:
```bash
mysqldump -u root db_kostku > backup.sql
```

**Q: Bisa deploy ke server?**  
A: Ya, bisa di-deploy ke server dengan Java dan MySQL installed.

---

## Support

Jika ada masalah, cek:
1. File `error_log.txt` untuk error messages
2. `DOKUMENTASI_APLIKASI.md` untuk fitur details
3. `RINGKASAN_PERUBAHAN.md` untuk fitur baru

---

**Setup Berhasil!** 🎉

Aplikasi siap digunakan. Selamat menikmati Kost Management System!
