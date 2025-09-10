/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package pk_electroniccity;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author lenovo
 */
public class db_connect {
    public static Connection connect(){
        Connection conn = null;
        try {
        // URL KONEKSI JDBC
        String url = "jdbc:mysql://localhost:3306/db_spksaw";
        String user = "root";
        String password = "";
        
        //MEMUAT DRIVER MYSQL
        Class.forName("com.mysql.cj.jdbc.Driver");
        
        //MEMBUAT KONEKSI
        conn = DriverManager.getConnection(url, user, password);
        System.out.println("Koneksi Berhasil");
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Koneksi Gagal : " + e.getMessage());
        }
        return conn;
    }
}
