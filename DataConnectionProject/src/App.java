import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement; // Diperlukan untuk PreparedStatement
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class App {
    public static void main(String[] args) {
        // --- KONFIGURASI KONEKSI ---
        // Ganti port, username, dan password sesuai konfigurasi MySQL Anda!
        String url = "jdbc:mysql://localhost:3306/akademik";
        String username = "usr kalian";
        String password = "pw kalian";

        // Variabel untuk Latihan 1
        String nimBaru = "245150700111028"; // GANTI DENGAN NIM ANDA
        String namaMahasiswa = "John Doe";

        // Variabel untuk Latihan 2
        String namaProdi = "Cyber Security";
        int idProdi = 999;
        int idStrata = 2; // S1 (Sarjana) - ID valid dari tabel strata 
        int idJurusan = 21; // ID Jurusan Informatika - ID valid dari tabel jurusan 

        // Gunakan try-with-resources agar objek SQL tertutup otomatis
        try (Connection conn = DriverManager.getConnection(url, username, password)) {

            System.out.println("Koneksi Berhasil!");

            // ==========================================================
            // LATIHAN 1: UPDATE Data Mahasiswa (dengan verifikasi SELECT yang dikoreksi)
            // ==========================================================
            System.out.println("\n--- Latihan 1: UPDATE Data Mahasiswa ---");

            // Query UPDATE
            String queryUpdate = "UPDATE mahasiswa SET NIM = ? WHERE nama = ?";

            try (PreparedStatement ps = conn.prepareStatement(queryUpdate)) {
                ps.setString(1, nimBaru);
                ps.setString(2, namaMahasiswa);

                int rowsAffected = ps.executeUpdate();
                System.out.println("✅ " + rowsAffected + " baris berhasil diupdate.");
            }

            // Tampilkan hasil UPDATE (Verifikasi - KODE INI YANG DIKOREKSI)
            String querySelectUpdated = "SELECT NIM, nama FROM mahasiswa WHERE nama = ?";

            try (PreparedStatement psSelect = conn.prepareStatement(querySelectUpdated)) {
                // Parameter diset DULU sebelum eksekusi query
                psSelect.setString(1, namaMahasiswa);

                // Execute query
                try (ResultSet rs = psSelect.executeQuery()) {
                    if (rs.next()) {
                        // NIM yang baru akan ditampilkan di sini
                        System.out.println("Data Setelah Update: NIM = " + rs.getString("NIM") + ", Nama = "
                                + rs.getString("nama"));
                    } else {
                        System.out.println("Data mahasiswa dengan nama '" + namaMahasiswa + "' tidak ditemukan.");
                    }
                }
            }

            // ==========================================================
            // LATIHAN 2: INSERT dan SELECT Data PROGRAM_STUDI
            // ==========================================================
            System.out.println("\n--- Latihan 2: INSERT dan SELECT Data Program Studi ---");

            // 1. INSERT Data Baru ke PROGRAM_STUDI
            String queryInsertProdi = "INSERT INTO PROGRAM_STUDI (ID_Program_Studi, ID_Strata, ID_Jurusan, Program_Studi) "
                    + "VALUES (?, ?, ?, ?)";

            try (PreparedStatement psInsertProdi = conn.prepareStatement(queryInsertProdi)) {
                psInsertProdi.setInt(1, idProdi);
                psInsertProdi.setInt(2, idStrata);
                psInsertProdi.setInt(3, idJurusan);
                psInsertProdi.setString(4, namaProdi);

                int rowsAffected = psInsertProdi.executeUpdate();
                System.out.println("✅ " + rowsAffected + " Program Studi '" + namaProdi + "' berhasil dimasukkan.");
            } catch (SQLException e) {
                if (e.getErrorCode() == 1062) {
                    System.out.println(
                            "⚠️ Program Studi '" + namaProdi + "' sudah ada (Duplicate Entry). Melanjutkan ke SELECT.");
                } else {
                    throw e;
                }
            }

            // 2. Tampilkan Data PROGRAM_STUDI (SELECT)
            String querySelectProdi = "SELECT ID_Program_Studi, Program_Studi, ID_Strata, ID_Jurusan FROM PROGRAM_STUDI WHERE Program_Studi = ?";

            try (PreparedStatement psSelectProdi = conn.prepareStatement(querySelectProdi)) {
                psSelectProdi.setString(1, namaProdi);
                try (ResultSet rsProdi = psSelectProdi.executeQuery()) {

                    System.out.println("\nData Program Studi 'Cyber Security':");
                    if (rsProdi.next()) {
                        System.out.println("   - ID_Program_Studi: " + rsProdi.getInt("ID_Program_Studi"));
                        System.out.println("   - Program_Studi: " + rsProdi.getString("Program_Studi"));
                        System.out.println("   - ID_Strata: " + rsProdi.getInt("ID_Strata"));
                        System.out.println("   - ID_Jurusan: " + rsProdi.getInt("ID_Jurusan"));
                    } else {
                        System.out.println("   - Data tidak ditemukan.");
                    }
                }
            }
            // ==========================================================

        } catch (SQLException e) {
            // --- PENANGANAN ERROR KONEKSI ATAU SQL UMUM ---
            System.out.println("\n❌ Terjadi Kesalahan SQL: " + e.getMessage());
        }
    }
}
