-- SQL Script untuk membuat users table
-- Jalankan script ini di MySQL database Anda

USE db_kostku;

-- Create users table untuk authentication
CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    full_name VARCHAR(100) NOT NULL,
    role VARCHAR(20) DEFAULT 'user',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create kamar table
CREATE TABLE IF NOT EXISTS kamar (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nomor_kamar VARCHAR(50) UNIQUE NOT NULL,
    tipe VARCHAR(50),
    harga DECIMAL(10,2),
    status VARCHAR(20) DEFAULT 'KOSONG',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create penghuni table
CREATE TABLE IF NOT EXISTS penghuni (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nik VARCHAR(100) NOT NULL,
    nama VARCHAR(100) NOT NULL,
    no_telp VARCHAR(20),
    alamat TEXT,
    kamar_id INT,
    tanggal_masuk DATE,
    status VARCHAR(20) DEFAULT 'AKTIF',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (kamar_id) REFERENCES kamar(id) ON DELETE SET NULL,
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create pembayaran table
CREATE TABLE IF NOT EXISTS pembayaran (
    id INT AUTO_INCREMENT PRIMARY KEY,
    penghuni_id INT NOT NULL,
    bulan_tahun VARCHAR(7),
    jumlah DECIMAL(10,2),
    tanggal_bayar DATE,
    status VARCHAR(20) DEFAULT 'BELUM_BAYAR',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (penghuni_id) REFERENCES penghuni(id) ON DELETE CASCADE,
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create pengeluaran table
CREATE TABLE IF NOT EXISTS pengeluaran (
    id INT AUTO_INCREMENT PRIMARY KEY,
    deskripsi VARCHAR(255) NOT NULL,
    jumlah DECIMAL(10,2),
    tanggal DATE,
    kategori VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Create profil_kost table
CREATE TABLE IF NOT EXISTS profil_kost (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nama VARCHAR(100),
    alamat TEXT,
    pemilik VARCHAR(100),
    no_telp VARCHAR(20),
    email VARCHAR(100),
    deskripsi TEXT,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Insert default profil kost
INSERT INTO profil_kost (nama, alamat, pemilik, no_telp, email) 
VALUES ('Kost Saya', 'Jl. Belum Diisi', 'Nama Pemilik', '081234567890', 'kost@email.com')
ON DUPLICATE KEY UPDATE id=id;

-- Create index untuk performa
CREATE INDEX idx_created_at ON users(created_at);
CREATE INDEX idx_created_at ON kamar(created_at);
CREATE INDEX idx_created_at ON penghuni(created_at);
CREATE INDEX idx_created_at ON pembayaran(created_at);
CREATE INDEX idx_created_at ON pengeluaran(created_at);

COMMIT;
