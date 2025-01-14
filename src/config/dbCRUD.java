/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;
import java.awt.HeadlessException;
import java.io.File;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager; 
import net.sf.jasperreports.engine.JasperFillManager; 
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JRDesignQuery; 
import net.sf.jasperreports.engine.design.JasperDesign; 
import net.sf.jasperreports.engine.xml.JRXmlLoader; 
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author USER
 */
public class dbCRUD {
    String jdbcUrl = "jdbc:mysql://localhost:3306/2210010415_pbo2";
    String username = "root";
    String password = "";
    private String jdbcURL;

    public dbCRUD() {
        try (Connection Koneksi = DriverManager.getConnection(jdbcUrl, username, password)) {
            Driver mysqlDriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);

            System.out.println("Berhasil!");
        } catch (SQLException error) {
            System.err.println(error.toString());
        }
    }
    
    public dbCRUD(String url, String user, String pass){
        
        try(Connection Koneksi = DriverManager.getConnection(url, user, pass)) {
            Driver mysqldriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(mysqldriver);
            
            System.out.println("Berhasil");
        } catch (Exception error) {
            System.err.println(error.toString());
        }
        
    }
    
    public Connection getKoneksi() {
    Connection koneksi = null;
    try {
        String url = "jdbc:mysql://localhost:3306/2210010415_pbo2"; // Ganti sesuai konfigurasi Anda
        String user = "root"; // Ganti dengan username database Anda
        String password = ""; // Ganti dengan password database Anda

        koneksi = DriverManager.getConnection(url, user, password);
    } catch (SQLException e) {
        System.out.println("Koneksi gagal: " + e.getMessage());
    }
    return koneksi;
}


    public boolean duplicateKey(String table, String PrimaryKey, String value){
        boolean hasil=false;
        
        try {
            Statement sts = getKoneksi().createStatement();
            ResultSet rs = sts.executeQuery("SELECT*FROM "+table+" WHERE "+PrimaryKey+" ='"+value+"'");
            hasil = rs.next();
            
            rs.close();
            sts.close();
            getKoneksi().close();
            
        } catch (Exception e) {
            System.err.println(e.toString());
        }     
        return hasil;
    }
      
    public String getTabelField (String[] TabelField)
    {
        String hasilnya="";
        int deteksiIndexAkhir=TabelField.length-1;
        try{
            for (int i = 0; i < TabelField.length; i++){
                if (i==deteksiIndexAkhir){
                    hasilnya=hasilnya+TabelField[i];                    
                }else{
                    hasilnya=hasilnya+TabelField[i]+',';
                }
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return "("+hasilnya+")";
    }
    
    public String getIsiTabel(String[] IsiTabelnya){
        String hasilnya="";
        int DeteksiTabel=IsiTabelnya.length-1;
        try{
               for (int i = 0; i < IsiTabelnya.length;i++){
                if (i==DeteksiTabel){
                    hasilnya=hasilnya+"'"+IsiTabelnya[i]+"'";
                } else{
                    hasilnya=hasilnya+"'"+IsiTabelnya[i]+"',";
               }
            } 
        }catch (Exception e){
                System.out.println(e.toString());
        }
        return "("+hasilnya+")";
    }

    public void SimpanDinamisBibit(String id_bibit, String[] nama_bibit, String[] jenis_bibit, int[] jumlah) {
    try {
        if (duplicateKey("bibit", "id_bibit", id_bibit)) {
            JOptionPane.showMessageDialog(null, "id_bibit sudah terdaftar");
        } else { 
            String SQL = "INSERT INTO bibit (id_bibit, nama_bibit, jenis_bibit, jumlah) VALUES (?, ?, ?, ?)";
            PreparedStatement simpan = getKoneksi().prepareStatement(SQL);

            for (int i = 0; i < nama_bibit.length; i++) {
                simpan.setString(1, id_bibit);
                simpan.setString(2, nama_bibit[i]);
                simpan.setString(3, jenis_bibit[i]);
                simpan.setInt(4, jumlah[i]);

                simpan.addBatch(); // Tambahkan ke batch
            }
                simpan.executeBatch(); // Eksekusi batch
                System.out.println("Data Berhasil Disimpan");
                simpan.close();
                getKoneksi().close();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    public void UpdateDinamisBibit(String id_bibit, String[] nama_bibit, String[] jenis_bibit, int[] jumlah) {
        try {
            if (!duplicateKey("bibit", "id_bibit", id_bibit)) {
                JOptionPane.showMessageDialog(null, "id_bibit tidak ditemukan");
            } else {
                String SQL = "UPDATE bibit SET nama_bibit = ?, jenis_bibit = ?, jumlah = ? WHERE id_bibit = ?";
                PreparedStatement update = getKoneksi().prepareStatement(SQL);

                for (int i = 0; i < nama_bibit.length; i++) {
                    update.setString(1, nama_bibit[i]);
                    update.setString(2, jenis_bibit[i]);
                    update.setInt(3, jumlah[i]);
                    update.setString(4, id_bibit);

                    update.addBatch(); // Tambahkan ke batch
                }

                update.executeBatch(); // Eksekusi batch
                System.out.println("Data Berhasil Diupdate");
                update.close();
                getKoneksi().close();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    public void HapusDinamisBibit(String id_bibit) {
        try {
            // Cek apakah id_bibit ada di database
            if (!duplicateKey("bibit", "id_bibit", id_bibit)) {
                JOptionPane.showMessageDialog(null, "id_bibit tidak ditemukan");
                return;
            }
            // Query SQL untuk menghapus data
            String SQL = "DELETE FROM bibit WHERE id_bibit = ?";
            PreparedStatement delete = getKoneksi().prepareStatement(SQL);
            // Set parameter id_bibit
            delete.setString(1, id_bibit);
            // Eksekusi query
            int rowsAffected = delete.executeUpdate();

            if (rowsAffected > 0) {
                System.out.println("Data dengan id_bibit " + id_bibit + " berhasil dihapus.");
            } else {
                System.out.println("Data dengan id_bibit " + id_bibit + " tidak ditemukan.");
            }

            delete.close();
            getKoneksi().close();
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan: " + e.toString());
        }
    }
    
                                                            //FUNGSI KELOMPOK//
    
    public void SimpanDinamisKelompok(String id_kelompok, String nama_kelompok, String desa, String tahun_bentuk, String jumlah_anggota, String luas_lahan, String ketua_kelompok) {
        try {
            if (duplicateKey("kelompok", "id_kelompok", id_kelompok)) {
                JOptionPane.showMessageDialog(null, "kelompok sudah terdaftar");
            } else { 
                String SQL = "INSERT INTO kelompok_tani (id_kelompok, nama_kelompok, desa, tahun_bentuk, jumlah_anggota, luas_lahan, ketua_kelompok) VALUES (?, ?, ?, ?, ?, ?, ?)";
                PreparedStatement simpan = getKoneksi().prepareStatement(SQL);
                simpan.setString(1, id_kelompok);
                simpan.setString(2, nama_kelompok);
                simpan.setString(3, desa);
                simpan.setString(4, tahun_bentuk);
                simpan.setString(5, jumlah_anggota);
                simpan.setString(6, luas_lahan);
                simpan.setString(7, ketua_kelompok);

                System.out.println("Data Berhasil Disimpan");
                simpan.executeUpdate();
                simpan.close();
                getKoneksi().close();
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
    public void UpdateDinamisKelompok(String id_kelompok, String nama_kelompok, String desa, String tahun_bentuk, String jumlah_anggota, String luas_lahan, String ketua_kelompok) {
        try {
            // Periksa apakah id_kelompok ada
            if (!duplicateKey("kelompok_tani", "id_kelompok", id_kelompok)) {
                JOptionPane.showMessageDialog(null, "id_kelompok tidak ditemukan");
            } else {
                String SQL = "UPDATE kelompok_tani SET nama_kelompok = ?, desa = ?, tahun_bentuk = ?, jumlah_anggota = ?, luas_lahan = ?, ketua_kelompok = ? WHERE id_kelompok = ?";
                PreparedStatement ubah = getKoneksi().prepareStatement(SQL);

                // Set parameter
                ubah.setString(1, nama_kelompok);
                ubah.setString(2, desa);
                ubah.setString(3, tahun_bentuk);
                ubah.setString(4, jumlah_anggota);
                ubah.setString(5, luas_lahan);
                ubah.setString(6, ketua_kelompok);
                ubah.setString(7, id_kelompok);

                // Eksekusi perintah update
                ubah.executeUpdate();
                System.out.println("Data berhasil diubah");

                // Tutup statement dan koneksi
                ubah.close();
                getKoneksi().close();
            }
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan: " + e.toString());
        }
    }
    
    public void HapusDinamisKelompok(String id_kelompok) {
    try {
        // Periksa apakah id_kelompok ada
        if (!duplicateKey("kelompok_tani", "id_kelompok", id_kelompok)) {
            JOptionPane.showMessageDialog(null, "id_kelompok tidak ditemukan");
        } else {
            String SQL = "DELETE FROM kelompok_tani WHERE id_kelompok = ?";
            PreparedStatement hapus = getKoneksi().prepareStatement(SQL);

            // Set parameter
            hapus.setString(1, id_kelompok);

            // Eksekusi perintah delete
            int rowsAffected = hapus.executeUpdate();
            if (rowsAffected > 0) {
                System.out.println("Data dengan id_kelompok " + id_kelompok + " berhasil dihapus");
            }

            // Tutup statement dan koneksi
            hapus.close();
            getKoneksi().close();
        }
    } catch (Exception e) {
        System.out.println("Terjadi kesalahan: " + e.toString());
    }
}




    public void UpdateDinamis(String NamaTabel, String PrimaryKey, String IsiPrimary,String[] Field, String[] Value){
        
        try {
           String SQLUbah = "UPDATE "+NamaTabel+" SET "+getFieldValueEdit(Field, Value)+" WHERE "+PrimaryKey+"='"+IsiPrimary+"'";
           Statement perintah = getKoneksi().createStatement();
           perintah.executeUpdate(SQLUbah);
           perintah.close();
           getKoneksi().close();
        } catch (SQLException e) {
            System.err.println(e.toString());
        }
        
    }

    public void HapusDinamis(String NamaTabel, String PK, String isi){
        try {
            String SQL="DELETE FROM "+NamaTabel+" WHERE "+PK+"='"+isi+"'";
            Statement perintah = getKoneksi().createStatement();
            perintah.executeUpdate(SQL);
            perintah.close();
            JOptionPane.showMessageDialog(null,"Data Berhasil Dihapus");
        } catch (HeadlessException | SQLException e) {
            System.out.println(e.toString());
        }
    }
    
    public void settingJudulTabel(JTable table, String[] judulKolom) {
    DefaultTableModel model = new DefaultTableModel(null, judulKolom);
    table.setModel(model);
    }

    public void settingLebarKolom(JTable table, int[] lebarKolom) {
    for (int i = 0; i < lebarKolom.length; i++) {
        table.getColumnModel().getColumn(i).setPreferredWidth(lebarKolom[i]);
    }
}

    public void tampilTabel(JTable table, String sql, String[] judulKolom) {
    try {
        Connection koneksi = getKoneksi();
        Statement stmt = koneksi.createStatement();
        ResultSet rs = stmt.executeQuery(sql);

        DefaultTableModel model = new DefaultTableModel(null, judulKolom);
        while (rs.next()) {
            Object[] row = new Object[judulKolom.length];
            for (int i = 0; i < judulKolom.length; i++) {
                row[i] = rs.getObject(i + 1); // Mengambil data per kolom
            }
            model.addRow(row);
        }

        table.setModel(model);
        rs.close();
        stmt.close();
        koneksi.close();
    } catch (SQLException e) {
        System.err.println("Error tampilTabel: " + e.getMessage());
    }
    
}

    public void tampilLaporan(String laporanFile, String SQL) {
    try {
        // Load file laporan JasperDesign
        File file = new File(laporanFile);
        JasperDesign jasDes = JRXmlLoader.load(file);

        // Set query SQL pada JasperDesign
        JRDesignQuery sqlQuery = new JRDesignQuery();
        sqlQuery.setText(SQL);
        jasDes.setQuery(sqlQuery);

        // Compile laporan ke JasperReport
        JasperReport jasperReport = JasperCompileManager.compileReport(jasDes);

        // Isi laporan dengan data dari database
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, null, getKoneksi());

        // Tampilkan laporan menggunakan JasperViewer
        JasperViewer.viewReport(jasperPrint, false);
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan saat menampilkan laporan: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
}

    

    public void SimpanDinamis(String bibit, String[] field, String[] value) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void UpdateDinamisBibit(String bibit, String id_bibit, String text, String[] fieldToUpdate, String[] newValue) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void SimpanDinamisBibit(String id_bibit, String nama_bibit, String jenis_bibit, int jumlah) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    private String getFieldValueEdit(String[] Field, String[] Value) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

   

   
}
