package model;

/**
 * Minimal interface untuk objek yang bisa direport.
 */
public interface Reportable {
    /**
     * Mengembalikan representasi baris untuk laporan/ekspor.
     */
    String toReportString();
}
