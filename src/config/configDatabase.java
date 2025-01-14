/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package config;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import javax.swing.JOptionPane;



/**
 *
 * @author USER
 */
public class configDatabase {
    
    private String jdbcURL="jdbc:mysql://localhost:3306/2210010415_pbo2";
    private String username="root";
    private String password="";
    
    private DefaultTableModel Modelnya;
    private TableColumn kolomnya;
    
    public configDatabase(){}
    
    public Connection getKoneksiDB() throws SQLException {
        try {
            Driver mysqldriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(mysqldriver);
            System.out.println("Koneksi DB berhasil");
        } catch (SQLException e){
            System.out.println("Gagal konek ke DB: " + e.getMessage());
        }
        
        return DriverManager.getConnection(jdbcURL, username, password);
    }
    public boolean DuplicateKey (String NamaTabel, String PrimaryKey, String isiData){
        boolean hasil=false; //definisi awal
        try{
            String SQL = "SELECT * FROM " + NamaTabel + " WHERE " + PrimaryKey + " ='" + isiData + "'";
            Statement perintah = getKoneksiDB().createStatement();
            ResultSet hasilData = perintah.executeQuery(SQL);
        
            hasil=hasilData.next();//true
        } catch (Exception e){
          System.out.println("Error DuplicateKey: " + e.getMessage());
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
    public String getTabelFill (String[] Fill)
    {
        String result="";
        int deteksiIndex=Fill.length-1;
        try{
            for (int i = 0; i < Fill.length; i++){
                if (i==deteksiIndex){
                    result=result+Fill[i]+",";                    
                }else{
                    result=result+Fill[i]+',';
                }
            }
        }catch (Exception e){
            System.out.println(e.toString());
        }
        return "("+result+")";
    }
    public String getFieldValueEdit(String[] Field, String[] value){
        String hasil = "";
        int deteksi = Field.length-1;
        try {
            for (int i = 0; i < Field.length; i++) {
                if (i==deteksi){
                    hasil = hasil +Field[i]+" ='"+value[i]+"'";
                }else{
                   hasil = hasil +Field[i]+" ='"+value[i]+"',";  
                }
            }
        } catch (Exception e) {
            System.out.println(e.toString());
        }
        
        return hasil;
    }
    
    public void UbahDinamis(String NamaTabel, String PrimaryKey, String IsiPrimary,String[] Field, String[] Value){
        
        try {
           String SQLUbah = "UPDATE "+NamaTabel+" SET "+getFieldValueEdit(Field, Value)+" WHERE "+PrimaryKey+"='"+IsiPrimary+"'";
           Statement perintah = getKoneksiDB().createStatement();
           perintah.executeUpdate(SQLUbah);
           perintah.close();
           getKoneksiDB().close();
        } catch (Exception e) {
            System.err.println(e.toString());
        }
        
    }
    
     public void HapusDinamis(String NamaTabel, String PK, String isi){
        try {
            String SQL="DELETE FROM "+NamaTabel+" WHERE "+PK+"='"+isi+"'";
            Statement perintah = getKoneksiDB().createStatement();
            perintah.executeUpdate(SQL);
            perintah.close();
            JOptionPane.showMessageDialog(null,"Data Berhasil Dihapus");
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    
}
