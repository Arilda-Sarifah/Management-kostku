# 🏠 Sistem Management Kos - Maven Project

Aplikasi manajemen kost berbasis **Java Swing** yang telah dikonversi ke **Maven project**.
Dependency `mysql-connector` tidak lagi disimpan manual di folder `lib/`, melainkan dikelola otomatis oleh Maven.

---

## 📦 Struktur Project

```
SistemManagementKos/
│
├── pom.xml                          ← Konfigurasi Maven (pengganti folder lib/)
├── database_setup.sql               ← Script setup database MySQL
├── README.md
│
└── src/
    └── main/
        ├── java/                    ← Semua source code Java
        │   ├── MainApp.java         ← Entry point (main class)
        │   │
        │   ├── model/               ← Layer DATA (POJO/Entity)
        │   │   ├── DatabaseConnection.java  ← Koneksi ke MySQL
        │   │   ├── BaseEntity.java          ← Abstract class induk entity
        │   │   ├── Reportable.java          ← Interface untuk laporan
        │   │   ├── User.java
        │   │   ├── Kamar.java
        │   │   ├── Penghuni.java
        │   │   ├── Pembayaran.java
        │   │   ├── Pengeluaran.java
        │   │   └── ProfilKost.java
        │   │
        │   ├── controller/          ← Layer LOGIKA BISNIS (akses database)
        │   │   ├── AuthController.java
        │   │   ├── KamarController.java
        │   │   ├── PenghuniController.java
        │   │   ├── PembayaranController.java
        │   │   ├── PengeluaranController.java
        │   │   ├── LaporanController.java
        │   │   └── ProfilKostController.java
        │   │
        │   ├── view/                ← Layer TAMPILAN (Swing UI)
        │   │   ├── LoginView.java
        │   │   ├── RegisterView.java
        │   │   ├── DashboardView.java
        │   │   ├── KamarView.java
        │   │   ├── PenghuniView.java
        │   │   ├── PembayaranView.java
        │   │   ├── PengeluaranView.java
        │   │   ├── LaporanView.java
        │   │   └── ProfilKostView.java
        │   │
        │   └── util/                ← Kelas Pembantu (Utilities)
        │       ├── UIUtilities.java
        │       ├── AnimationUtilities.java
        │       └── ExceptionHandler.java
        │
        └── resources/               ← Asset non-Java (icon, gambar)
            └── assets/
                └── icon/
                    ├── bed.png
                    ├── dashboard.png
                    ├── home.png
                    ├── multiple-users-silhouette.png
                    ├── payment-method.png
                    ├── report.png
                    └── save-money.png
```

---

## 🚀 Cara Menjalankan

### 1. Setup Database MySQL
```sql
-- Buka MySQL dan jalankan:
CREATE DATABASE db_kostku;
USE db_kostku;
-- lalu jalankan isi file database_setup.sql
```

### 2. Konfigurasi Koneksi Database
Edit file `src/main/java/model/DatabaseConnection.java`:
```java
private static String URL      = "jdbc:mysql://localhost:3306/db_kostku";
private static String USER     = "root";
private static String PASSWORD = "password_mysql_kamu";
```

Atau buat file `config.properties` di root project:
```properties
db.url=jdbc:mysql://localhost:3306/db_kostku
db.user=root
db.password=password_kamu
```

### 3. Build dan Run dengan Maven

**Di NetBeans:**
- File → Open Project → pilih folder ini
- Klik kanan project → Run

**Di terminal / command prompt:**
```bash
# Compile
mvn compile

# Jalankan langsung
mvn exec:java -Dexec.mainClass="MainApp"

# Build JAR (bisa dijalankan tanpa IDE)
mvn package
java -jar target/SistemManagementKos-1.0.0-jar-with-dependencies.jar
```

---

## 🔧 Dependency yang Digunakan

| Library | Versi | Fungsi |
|---|---|---|
| `mysql-connector-j` | 9.1.0 | Driver JDBC untuk koneksi ke MySQL |

Maven akan otomatis download dependency ini saat pertama kali build.
**Tidak perlu download JAR manual** seperti sebelumnya.

---

## 🏗️ Arsitektur MVC

Project ini menggunakan pola **Model-View-Controller (MVC)**:

| Layer | Package | Tanggung Jawab |
|---|---|---|
| **Model** | `model/` | Representasi data & koneksi database |
| **View** | `view/` | Tampilan UI dengan Java Swing |
| **Controller** | `controller/` | Logika bisnis & query SQL |
| **Util** | `util/` | Fungsi pembantu (UI, animasi, error handling) |
