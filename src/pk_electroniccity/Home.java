/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pk_electroniccity;

import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.SQLException;
import pk_electroniccity.db_connect;
import java.sql.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.JLabel;
import javax.swing.table.TableModel;
import pk_electroniccity.E_Karyawan;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.text.DecimalFormat;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;

import java.awt.Desktop;
import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.DriverManager;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.data.JRTableModelDataSource;
import net.sf.jasperreports.view.JasperViewer;



/**
 *
 * @author lenovo
 */
public class Home extends javax.swing.JFrame {

    Connection connection;
    Statement statement;
    /**
     * Creates new form Home
     */
    public Home() {
            initComponents();
                       //TAMBAHKAN KODE INISIALISASI KONEKSI DI SINI
            try {
               // Membuat koneksi ke database
            connection = db_connect.connect();
            if (connection != null) {
            statement = connection.createStatement();
           }
           } catch (SQLException e) {
               e.printStackTrace();
           }
            
            ConnJabatan();
            ConnDivisi();
            ConnKaryawan();
            ConnKriteria();
            ConnPerhitungan();
            ConnNormalisasi();
            ConnRanking();
            loadNamaKriteria();
            loadCmbNikKaryawan();
            loadUsername();
            noedittxtfield();
               
    }

 private void noedittxtfield (){
     InputK1.setEnabled(false);
     InputK2.setEnabled(false);
     InputK3.setEnabled(false);
     InputK4.setEnabled(false);
     InputK5.setEnabled(false);
     InputNamaKaryawan.setEnabled(false);
     InputDivisiKaryawan.setEnabled(false);
     InputJabatanKaryawan.setEnabled(false);
     InputTglKaryawan.setEnabled(false);
 }
    
 public void setK5(String nilaipenjualan) {
    InputK5.setText(nilaipenjualan);
} 
    
 public void setK4(String nilaiteknis) {
    InputK4.setText(nilaiteknis);
} 
 
 public void setK3(String formatkerjasama) {
    InputK3.setText(formatkerjasama);
} 
 public void setK2(String formatkinerja) {
    InputK2.setText(formatkinerja);
}
    
 public void setK1(String nilai) {
    InputK1.setText(nilai);
}

private void loadUsername() {
    try (Connection conn = db_connect.connect()) {
        // Query untuk mengambil username
        String sql = "SELECT username FROM users";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        // Jika ditemukan data username, tampilkan pada input
        if (rs.next()) {
            String username = rs.getString("username");
            InputUsername.setText(username); // Set ke kolom input username
        } else {
            JOptionPane.showMessageDialog(null, "Username tidak ditemukan!");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal memuat username: " + e.getMessage());
    }
}

    
private boolean isNikExist(String nik, String periode) {
    boolean exists = false;
    try (Connection conn = db_connect.connect()) {
        String sql = "SELECT COUNT(*) AS count FROM perhitungan WHERE nik_perhitungan = ? AND periode = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, nik);
        stmt.setString(2, periode);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            exists = rs.getInt("count") > 0;
        }
        rs.close();
        stmt.close();
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal memeriksa NIK: " + e.getMessage());
    }
    return exists;
}

public void loadCmbNikKaryawan(){
    try (Connection conn = db_connect.connect()){
    CmbNikKaryawan.addItem("Pilih NIK");
    String sql = "SELECT nik_karyawan from karyawan";
    Statement stmt = conn.createStatement();
    ResultSet rs = stmt.executeQuery(sql);
        while (rs.next()) {
            CmbNikKaryawan.addItem(rs.getString("nik_karyawan"));   
        }
    }catch (Exception e){
        System.out.println("Data Combo Nik Karyawan Gagal Masuk" + e.getMessage());
    }
}

    private boolean isMaxRowsReached() {
    try (Connection conn = db_connect.connect()) {
        String sql = "SELECT COUNT(*) AS row_count FROM kriteria";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            int rowCount = rs.getInt("row_count");
            return rowCount >= 5; // Maksimal 5 baris
        }
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
    }
    return false;
}
// AWAL KONEKSI TABLE 
    
public void ConnJabatan() {
    DefaultTableModel tb = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
            }
        };
    
    tb.addColumn("JABATAN"); // MENAMBAH JUDUL KOLOM
    JT_Jabatan.setModel(tb);   

    try {
        // Koneksi ke database
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_spksaw", "root", ""); 
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM jabatan");

        // Mengambil data dari ResultSet dan menambahkannya ke tabel
        while (rs.next()) {
            tb.addRow(new Object[]{
                rs.getString("nama_jabatan")
            });
        }

        // Menutup koneksi setelah selesai
        rs.close();
        stmt.close();
        con.close();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "DATA JABATAN GAGAL MASUK" + e.getMessage());
    }
}

public void ConnDivisi() {
    DefaultTableModel tb = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column){
            return false;
            }
    };
   
   tb.addColumn("DIVISI"); // MENAMBAHKAN KOLOM
   JT_Divisi.setModel(tb);  
  
   try {
       //KONEKSI KE DATABASE
       Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_spksaw", "root", "");
       Statement stmt = con.createStatement();
       ResultSet rs = stmt.executeQuery("SELECT * FROM divisi");
       
       // mengambil data isi table
       while (rs.next()) {
       tb.addRow(new Object[]{
       rs.getString("nama_divisi")
       });
       }
       
       //tutup koneksi
       rs.close();
       stmt.close();
       con.close();
       
   }catch (Exception e) {
       JOptionPane.showMessageDialog(null, "DATA DIVISI GAGAL MASUK" + e.getMessage());
   }
    
}

public void ConnKaryawan() {
    DefaultTableModel tb = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
        return false;
        }
    };
        // MENAMBAHKAN KOLOM
        tb.addColumn("NIK"); 
        tb.addColumn("NAMA"); 
        tb.addColumn("DIVISI");
        tb.addColumn("JABATAN"); 
        tb.addColumn("TGL BERGABUNG");
        JT_Karayawan.setModel(tb);
        
        try {
            // KONEKSI KE DATABASE
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_spksaw", "root", "");
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM karyawan");
            
            // MENGAMBIL ISI TABLE
            while (rs.next()){
            tb.addRow(new Object[]{
            rs.getString("nik_karyawan"),
            rs.getString("nama_karyawan"),
            rs.getString("divisi_karyawan"),
            rs.getString("jabatan_karyawan"),
            rs.getString("tglmasuk_karyawan"),
            });
            }
            
            // TUTUP KONEKSI
            rs.close();
            stmt.close();
            con.close();
            
        }catch (Exception e) {
            JOptionPane.showMessageDialog(null, "DATA KARYAWAN GAGAL MASUK" + e.getMessage());
        }
                
}

public void ConnKriteria(){
    DefaultTableModel tb = new DefaultTableModel(){
    @Override
    public boolean isCellEditable(int row, int column){
    return false;
    }
    };
    
    //MEMBUAT KOLOM
    tb.addColumn("KODE");
    tb.addColumn("KRITERIA");
    tb.addColumn("BOBOT");
    JT_Kriteria.setModel(tb);
    
    try {
        // MEMBUAT KONEKSI
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_spksaw", "root", "");
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM kriteria");
        
        //MENGAMBIL ISI 
        while (rs.next()){
        tb.addRow(new Object[]{
        rs.getString("kode_kriteria"),
        rs.getString("nama_kriteria"),
        rs.getString("bobot_kriteria"),
        });
        }
        
        con.close();
        stmt.close();
        rs.close();
 
    }catch (Exception e) {
        JOptionPane.showMessageDialog(null, "DATA KRITERIA GAGAL MASUK" + e.getMessage());
    }
            

}

public void ConnPerhitungan(){
    DefaultTableModel tb = new DefaultTableModel(){
    @Override
    public boolean isCellEditable(int row, int column){
    return false;
    }
    };
    
    //MEMBUAT KOLOM
    tb.addColumn("NIK");
    tb.addColumn("NAMA");
    tb.addColumn("K1");
    tb.addColumn("K2");
    tb.addColumn("K3");
    tb.addColumn("K4");
    tb.addColumn("K5");
    tb.addColumn("PERIODE");
    JT_Perhitungan.setModel(tb);
   
    try {
        //MEMBUAT KONEKSI
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_spksaw", "root", "");
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM perhitungan");
        
        while (rs.next()) {
            tb.addRow(new Object[]{
                rs.getString("nik_perhitungan"),
                rs.getString("nama_perhitungan"),
                rs.getString("k1_perhitungan"),
                rs.getString("k2_perhitungan"),
                rs.getString("k3_perhitungan"),
                rs.getString("k4_perhitungan"),
                rs.getString("k5_perhitungan"),
                rs.getString("periode"),
            });
            
        }
        
        con.close();
        stmt.close();
        rs.close();
        
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "DATA PERHITUNGAN GAGAL MASUK" + e.getMessage());
    }
}

public void ConnNormalisasi() {
    DefaultTableModel tb = new DefaultTableModel(){
    @Override
    public boolean isCellEditable(int row, int column){
    return false;
    } 
    };
    
    //MEMBUAT KOLOM
    tb.addColumn("NIK");
    tb.addColumn("NAMA");
    tb.addColumn("K1");
    tb.addColumn("K2");
    tb.addColumn("K3");
    tb.addColumn("K4");
    tb.addColumn("K5");
    tb.addColumn("HASIL");
    tb.addColumn("PERIODE");
    JT_Normalisasi.setModel(tb);
    
    try {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_spksaw", "root", "");
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM normalisasi");
        
        while (rs.next()){
            tb.addRow(new Object[]{
                rs.getString("nik_normalisasi"),
                rs.getString("nama_normalisasi"),
                rs.getString("k1_normalisasi"),
                rs.getString("k2_normalisasi"),
                rs.getString("k3_normalisasi"),
                rs.getString("k4_normalisasi"),
                rs.getString("k5_normalisasi"),
                rs.getString("bobot_normalisasi"),
                rs.getString("periode"),
            });
        }
        
        con.close();
        stmt.close();
        rs.close();
        
    }catch (Exception e){
        JOptionPane.showMessageDialog(null, "DATA NORMALISASI GAGAL MASUK" + e.getMessage());
    }   
}

public void ConnRanking(){
    DefaultTableModel tb = new DefaultTableModel(){
    @Override
    public boolean isCellEditable(int row, int column){
    return false;
    }
    };
    
    //MEMBIUAT KOLOM
    tb.addColumn("PERIODE");
    tb.addColumn("NIK");
    tb.addColumn("NAMA");
    tb.addColumn("K1");
    tb.addColumn("K2");
    tb.addColumn("K3");
    tb.addColumn("K4");
    tb.addColumn("K5");
    tb.addColumn("BOBOT");
    tb.addColumn("RANKING");
    JT_Ranking.setModel(tb);
    
    try {
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_spksaw", "root", "");
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM ranking");
       
        while (rs.next()){
        tb.addRow(new Object[]{
            rs.getString("periode"),
            rs.getString("nik_ranking"),
            rs.getString("nama_ranking"),
            rs.getString("k1_ranking"),
            rs.getString("k2_ranking"),
            rs.getString("k3_ranking"),
            rs.getString("k4_ranking"),
            rs.getString("k5_ranking"),
            rs.getString("bobot_ranking"),
            rs.getString("status_ranking"),
        });
        }
        
        con.close();
        stmt.close();
        rs.close();
    } catch (Exception e){
        JOptionPane.showMessageDialog(null, "DATA RANKING GAGAL MASUK" + e.getMessage());
    }
    
}

// AKHIR KONEKSI TABLE

public void loadNamaKriteria() {
    String query = "SELECT kode_kriteria, nama_kriteria FROM kriteria"; // Query untuk mengambil kode dan nama kriteria
    try {
        // Validasi jika statement belum diinisialisasi
        if (statement == null) {
            System.out.println("Statement belum diinisialisasi!");
            return;
        }

        ResultSet resultSet = statement.executeQuery(query);

        // Loop melalui hasil query untuk menampilkan nama kriteria di label berdasarkan kode
        while (resultSet.next()) {
            String kodeKriteria = resultSet.getString("kode_kriteria");
            String namaKriteria = resultSet.getString("nama_kriteria");

            // Atur label sesuai dengan kode kriteria
            switch (kodeKriteria) {
                case "K1": 
                    LabelK1.setText(namaKriteria); 
                    break;
                case "K2": 
                    LabelK2.setText(namaKriteria); 
                    break;
                case "K3": 
                    LabelK3.setText(namaKriteria); 
                    break;
                case "K4": 
                    LabelK4.setText(namaKriteria); 
                    break;
                case "K5": 
                    LabelK5.setText(namaKriteria); 
                    break;
                default: 
                    System.out.println("Kode kriteria tidak dikenal: " + kodeKriteria);
                    break;
            }
        }
    } catch (SQLException e) {
        // Menangani error SQL
        e.printStackTrace();
    }
}

    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        P_Home = new javax.swing.JPanel();
        P_Menu = new javax.swing.JPanel();
        B_Karyawan = new javax.swing.JButton();
        B_Jabatan = new javax.swing.JButton();
        B_Kriteria = new javax.swing.JButton();
        B_Penilaian = new javax.swing.JButton();
        B_Perhitungan = new javax.swing.JButton();
        B_Ranking = new javax.swing.JButton();
        B_Pengaturan = new javax.swing.JButton();
        B_Keluar = new javax.swing.JButton();
        P_Isi = new javax.swing.JPanel();
        P_Awal = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        P_Karyawan = new javax.swing.JPanel();
        InputCariKaryawan = new javax.swing.JTextField();
        BT_CariKaryawan = new javax.swing.JButton();
        BT_Karyawan = new javax.swing.JButton();
        BU_Karyawan = new javax.swing.JButton();
        Print_Karyawan = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        JT_Karayawan = new javax.swing.JTable();
        BT_Refresh = new javax.swing.JToggleButton();
        P_Jabatan = new javax.swing.JPanel();
        InputCariDivisiJabatan = new javax.swing.JTextField();
        jButton2 = new javax.swing.JButton();
        BT_Jabatan = new javax.swing.JButton();
        BU_Jabatan = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        JT_Divisi = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        JT_Jabatan = new javax.swing.JTable();
        BT_Divisi = new javax.swing.JButton();
        BU_Divisi = new javax.swing.JButton();
        BT_Refresh1 = new javax.swing.JToggleButton();
        jButton9 = new javax.swing.JButton();
        P_Kriteria = new javax.swing.JPanel();
        jTextField3 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        BT_Kriteriia = new javax.swing.JButton();
        BU_Kriteria = new javax.swing.JButton();
        jScrollPane5 = new javax.swing.JScrollPane();
        JT_Kriteria = new javax.swing.JTable();
        BT_RefreshKriteria = new javax.swing.JButton();
        P_Penilaian = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        CmbNikKaryawan = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        InputNamaKaryawan = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        InputDivisiKaryawan = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        InputJabatanKaryawan = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        InputTglKaryawan = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        LabelK1 = new javax.swing.JLabel();
        LabelK2 = new javax.swing.JLabel();
        LabelK3 = new javax.swing.JLabel();
        LabelK4 = new javax.swing.JLabel();
        LabelK5 = new javax.swing.JLabel();
        BtSimpanPenilaian = new javax.swing.JButton();
        InputBulanPenilaian = new com.toedter.calendar.JMonthChooser();
        InputTahunPenilaian = new com.toedter.calendar.JYearChooser();
        InputK1 = new javax.swing.JTextField();
        InputK2 = new javax.swing.JTextField();
        InputK3 = new javax.swing.JTextField();
        InputK4 = new javax.swing.JTextField();
        InputK5 = new javax.swing.JTextField();
        BT_Kedisiplinan = new javax.swing.JButton();
        BT_Kinerja = new javax.swing.JButton();
        BT_Kerjasamatim = new javax.swing.JButton();
        BT_Kemampuanteknis = new javax.swing.JButton();
        BT_Pencapaianpenjualan = new javax.swing.JButton();
        P_Perhitungan = new javax.swing.JPanel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        JT_Perhitungan = new javax.swing.JTable();
        jLabel23 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jScrollPane6 = new javax.swing.JScrollPane();
        JT_Normalisasi = new javax.swing.JTable();
        NORMALISASI = new javax.swing.JButton();
        RefereshPerhitungan = new javax.swing.JButton();
        CariPeriodePerhitunganNormalisasi = new javax.swing.JButton();
        InputBulan1 = new com.toedter.calendar.JMonthChooser();
        InputTahun1 = new com.toedter.calendar.JYearChooser();
        BtHapus = new javax.swing.JButton();
        P_Ranking = new javax.swing.JPanel();
        jLabel25 = new javax.swing.JLabel();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        CariRanking = new javax.swing.JButton();
        jScrollPane7 = new javax.swing.JScrollPane();
        JT_Ranking = new javax.swing.JTable();
        PrintRanking = new javax.swing.JButton();
        TarikData = new javax.swing.JButton();
        jButton11 = new javax.swing.JButton();
        InputBulan2 = new com.toedter.calendar.JMonthChooser();
        InputTahun2 = new com.toedter.calendar.JYearChooser();
        P_Pengaturan = new javax.swing.JPanel();
        jLabel28 = new javax.swing.JLabel();
        jLabel29 = new javax.swing.JLabel();
        InputUsername = new javax.swing.JTextField();
        jLabel30 = new javax.swing.JLabel();
        BtSimpanAkun = new javax.swing.JButton();
        jLabel31 = new javax.swing.JLabel();
        jLabel32 = new javax.swing.JLabel();
        InputPwLama = new javax.swing.JPasswordField();
        InputKPwBaru = new javax.swing.JPasswordField();
        InputPwBaru = new javax.swing.JPasswordField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        P_Home.setBackground(new java.awt.Color(255, 255, 255));
        P_Home.setPreferredSize(new java.awt.Dimension(1250, 650));

        P_Menu.setBackground(new java.awt.Color(255, 255, 255));
        P_Menu.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 3, 0, new java.awt.Color(0, 114, 198)));
        P_Menu.setPreferredSize(new java.awt.Dimension(1250, 100));

        B_Karyawan.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        B_Karyawan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk_electroniccity/icon/karyawan.png"))); // NOI18N
        B_Karyawan.setText("KARYAWAN");
        B_Karyawan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 2, new java.awt.Color(0, 114, 198)));
        B_Karyawan.setContentAreaFilled(false);
        B_Karyawan.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        B_Karyawan.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        B_Karyawan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        B_Karyawan.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        B_Karyawan.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        B_Karyawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_KaryawanActionPerformed(evt);
            }
        });

        B_Jabatan.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        B_Jabatan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk_electroniccity/icon/jabatan.png"))); // NOI18N
        B_Jabatan.setText("JABATAN");
        B_Jabatan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 2, new java.awt.Color(0, 114, 198)));
        B_Jabatan.setContentAreaFilled(false);
        B_Jabatan.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        B_Jabatan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        B_Jabatan.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        B_Jabatan.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        B_Jabatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_JabatanActionPerformed(evt);
            }
        });

        B_Kriteria.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        B_Kriteria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk_electroniccity/icon/kriteria.png"))); // NOI18N
        B_Kriteria.setText("KRITERIA");
        B_Kriteria.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 2, new java.awt.Color(0, 114, 198)));
        B_Kriteria.setContentAreaFilled(false);
        B_Kriteria.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        B_Kriteria.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        B_Kriteria.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        B_Kriteria.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        B_Kriteria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_KriteriaActionPerformed(evt);
            }
        });

        B_Penilaian.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        B_Penilaian.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk_electroniccity/icon/penilaian.png"))); // NOI18N
        B_Penilaian.setText("PENILAIAN");
        B_Penilaian.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 2, new java.awt.Color(0, 114, 198)));
        B_Penilaian.setContentAreaFilled(false);
        B_Penilaian.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        B_Penilaian.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        B_Penilaian.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        B_Penilaian.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        B_Penilaian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_PenilaianActionPerformed(evt);
            }
        });

        B_Perhitungan.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        B_Perhitungan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk_electroniccity/icon/perhitungan.png"))); // NOI18N
        B_Perhitungan.setText("PERHITUNGAN");
        B_Perhitungan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 2, new java.awt.Color(0, 114, 198)));
        B_Perhitungan.setContentAreaFilled(false);
        B_Perhitungan.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        B_Perhitungan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        B_Perhitungan.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        B_Perhitungan.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        B_Perhitungan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_PerhitunganActionPerformed(evt);
            }
        });

        B_Ranking.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        B_Ranking.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk_electroniccity/icon/ranking.png"))); // NOI18N
        B_Ranking.setText("RANGKING");
        B_Ranking.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 2, new java.awt.Color(0, 114, 198)));
        B_Ranking.setContentAreaFilled(false);
        B_Ranking.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        B_Ranking.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        B_Ranking.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        B_Ranking.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        B_Ranking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_RankingActionPerformed(evt);
            }
        });

        B_Pengaturan.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        B_Pengaturan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk_electroniccity/icon/pengaturan.png"))); // NOI18N
        B_Pengaturan.setText("PENGATURAN");
        B_Pengaturan.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 2, new java.awt.Color(0, 114, 198)));
        B_Pengaturan.setContentAreaFilled(false);
        B_Pengaturan.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        B_Pengaturan.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        B_Pengaturan.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        B_Pengaturan.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        B_Pengaturan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_PengaturanActionPerformed(evt);
            }
        });

        B_Keluar.setFont(new java.awt.Font("Verdana", 0, 10)); // NOI18N
        B_Keluar.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk_electroniccity/icon/logout.png"))); // NOI18N
        B_Keluar.setText("KELUAR");
        B_Keluar.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(0, 114, 198)));
        B_Keluar.setContentAreaFilled(false);
        B_Keluar.setHorizontalAlignment(javax.swing.SwingConstants.LEADING);
        B_Keluar.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        B_Keluar.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        B_Keluar.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        B_Keluar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                B_KeluarActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout P_MenuLayout = new javax.swing.GroupLayout(P_Menu);
        P_Menu.setLayout(P_MenuLayout);
        P_MenuLayout.setHorizontalGroup(
            P_MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_MenuLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(B_Karyawan, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(B_Jabatan, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(B_Kriteria, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(B_Penilaian, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(B_Perhitungan, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(B_Ranking, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(B_Pengaturan, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(B_Keluar, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(160, Short.MAX_VALUE))
        );
        P_MenuLayout.setVerticalGroup(
            P_MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_MenuLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(P_MenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(B_Pengaturan)
                    .addComponent(B_Ranking)
                    .addComponent(B_Keluar)
                    .addComponent(B_Perhitungan)
                    .addComponent(B_Penilaian)
                    .addComponent(B_Kriteria)
                    .addComponent(B_Jabatan)
                    .addComponent(B_Karyawan))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        P_Isi.setBackground(new java.awt.Color(255, 255, 255));

        P_Awal.setBackground(new java.awt.Color(255, 255, 255));
        P_Awal.setPreferredSize(new java.awt.Dimension(1250, 538));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk_electroniccity/icon/logo_eci.png"))); // NOI18N

        javax.swing.GroupLayout P_AwalLayout = new javax.swing.GroupLayout(P_Awal);
        P_Awal.setLayout(P_AwalLayout);
        P_AwalLayout.setHorizontalGroup(
            P_AwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_AwalLayout.createSequentialGroup()
                .addGap(517, 517, 517)
                .addComponent(jLabel2)
                .addContainerGap(524, Short.MAX_VALUE))
        );
        P_AwalLayout.setVerticalGroup(
            P_AwalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_AwalLayout.createSequentialGroup()
                .addGap(169, 169, 169)
                .addComponent(jLabel2)
                .addContainerGap(169, Short.MAX_VALUE))
        );

        P_Karyawan.setBackground(new java.awt.Color(255, 255, 255));
        P_Karyawan.setPreferredSize(new java.awt.Dimension(1250, 538));
        P_Karyawan.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        InputCariKaryawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InputCariKaryawanActionPerformed(evt);
            }
        });
        P_Karyawan.add(InputCariKaryawan, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 30, 200, -1));

        BT_CariKaryawan.setText("CARI");
        BT_CariKaryawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_CariKaryawanActionPerformed(evt);
            }
        });
        P_Karyawan.add(BT_CariKaryawan, new org.netbeans.lib.awtextra.AbsoluteConstraints(291, 30, -1, -1));

        BT_Karyawan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk_electroniccity/icon/tambah.png"))); // NOI18N
        BT_Karyawan.setBorder(null);
        BT_Karyawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_KaryawanActionPerformed(evt);
            }
        });
        P_Karyawan.add(BT_Karyawan, new org.netbeans.lib.awtextra.AbsoluteConstraints(1089, 30, -1, -1));

        BU_Karyawan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk_electroniccity/icon/edit.png"))); // NOI18N
        BU_Karyawan.setBorder(null);
        BU_Karyawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BU_KaryawanActionPerformed(evt);
            }
        });
        P_Karyawan.add(BU_Karyawan, new org.netbeans.lib.awtextra.AbsoluteConstraints(1170, 30, -1, -1));

        Print_Karyawan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk_electroniccity/icon/print (1).png"))); // NOI18N
        Print_Karyawan.setBorder(null);
        Print_Karyawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Print_KaryawanActionPerformed(evt);
            }
        });
        P_Karyawan.add(Print_Karyawan, new org.netbeans.lib.awtextra.AbsoluteConstraints(1160, 479, -1, -1));

        JT_Karayawan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"", null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, "", null}
            },
            new String [] {
                "NIK", "NAMA", "DIVISI", "JABATAN", "TGL BERGABUNG"
            }
        ));
        JT_Karayawan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                JT_KarayawanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(JT_Karayawan);

        P_Karyawan.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 66, 1150, 395));

        BT_Refresh.setText("REFRESH");
        BT_Refresh.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_RefreshActionPerformed(evt);
            }
        });
        P_Karyawan.add(BT_Refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(390, 30, -1, -1));

        P_Jabatan.setBackground(new java.awt.Color(255, 255, 255));
        P_Jabatan.setPreferredSize(new java.awt.Dimension(1250, 538));

        InputCariDivisiJabatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InputCariDivisiJabatanActionPerformed(evt);
            }
        });

        jButton2.setText("CARI DIVISI");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        BT_Jabatan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk_electroniccity/icon/tambah.png"))); // NOI18N
        BT_Jabatan.setBorder(null);
        BT_Jabatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_JabatanActionPerformed(evt);
            }
        });

        BU_Jabatan.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk_electroniccity/icon/edit.png"))); // NOI18N
        BU_Jabatan.setBorder(null);
        BU_Jabatan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BU_JabatanActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Verdana", 0, 24)); // NOI18N
        jLabel1.setText("DIVISI");

        JT_Divisi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "NO", "DIVISI"
            }
        ));
        jScrollPane2.setViewportView(JT_Divisi);

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 24)); // NOI18N
        jLabel3.setText("JABATAN");

        JT_Jabatan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        JT_Jabatan.setShowHorizontalLines(true);
        jScrollPane3.setViewportView(JT_Jabatan);

        BT_Divisi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk_electroniccity/icon/tambah.png"))); // NOI18N
        BT_Divisi.setBorder(null);
        BT_Divisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_DivisiActionPerformed(evt);
            }
        });

        BU_Divisi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk_electroniccity/icon/edit.png"))); // NOI18N
        BU_Divisi.setBorder(null);
        BU_Divisi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BU_DivisiActionPerformed(evt);
            }
        });

        BT_Refresh1.setText("REFRESH");
        BT_Refresh1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_Refresh1ActionPerformed(evt);
            }
        });

        jButton9.setText("CARI JABATAN");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout P_JabatanLayout = new javax.swing.GroupLayout(P_Jabatan);
        P_Jabatan.setLayout(P_JabatanLayout);
        P_JabatanLayout.setHorizontalGroup(
            P_JabatanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_JabatanLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(P_JabatanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(P_JabatanLayout.createSequentialGroup()
                        .addComponent(InputCariDivisiJabatan, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton2)
                        .addGap(18, 18, 18)
                        .addComponent(jButton9)
                        .addGap(18, 18, 18)
                        .addComponent(BT_Refresh1)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(P_JabatanLayout.createSequentialGroup()
                        .addGroup(P_JabatanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(P_JabatanLayout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(BT_Divisi)
                                .addGap(51, 51, 51)
                                .addComponent(BU_Divisi)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 234, Short.MAX_VALUE)
                        .addGroup(P_JabatanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(P_JabatanLayout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(BT_Jabatan)
                                .addGap(51, 51, 51)
                                .addComponent(BU_Jabatan)))
                        .addGap(62, 62, 62))))
        );
        P_JabatanLayout.setVerticalGroup(
            P_JabatanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_JabatanLayout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(P_JabatanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(InputCariDivisiJabatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2)
                    .addComponent(BT_Refresh1)
                    .addComponent(jButton9))
                .addGap(32, 32, 32)
                .addGroup(P_JabatanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(P_JabatanLayout.createSequentialGroup()
                        .addGroup(P_JabatanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel1)
                            .addGroup(P_JabatanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(BU_Divisi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(BT_Divisi)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(P_JabatanLayout.createSequentialGroup()
                        .addGroup(P_JabatanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel3)
                            .addGroup(P_JabatanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(BU_Jabatan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(BT_Jabatan)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(65, Short.MAX_VALUE))
        );

        P_Kriteria.setBackground(new java.awt.Color(255, 255, 255));
        P_Kriteria.setPreferredSize(new java.awt.Dimension(1250, 538));

        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField3ActionPerformed(evt);
            }
        });

        jButton3.setText("CARI");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        BT_Kriteriia.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk_electroniccity/icon/tambah.png"))); // NOI18N
        BT_Kriteriia.setBorder(null);
        BT_Kriteriia.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_KriteriiaActionPerformed(evt);
            }
        });

        BU_Kriteria.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk_electroniccity/icon/edit.png"))); // NOI18N
        BU_Kriteria.setBorder(null);
        BU_Kriteria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BU_KriteriaActionPerformed(evt);
            }
        });

        JT_Kriteria.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "NO", "KRITERIA", "BOBOT", "SANGAT BAIK", "BAIK SEKALI", "BAIK", "CUKUP", "KURANG"
            }
        ));
        jScrollPane5.setViewportView(JT_Kriteria);

        BT_RefreshKriteria.setText("REFRESH");
        BT_RefreshKriteria.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_RefreshKriteriaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout P_KriteriaLayout = new javax.swing.GroupLayout(P_Kriteria);
        P_Kriteria.setLayout(P_KriteriaLayout);
        P_KriteriaLayout.setHorizontalGroup(
            P_KriteriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, P_KriteriaLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(P_KriteriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(P_KriteriaLayout.createSequentialGroup()
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(41, 41, 41)
                        .addComponent(jButton3)
                        .addGap(18, 18, 18)
                        .addComponent(BT_RefreshKriteria)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BT_Kriteriia)
                        .addGap(62, 62, 62)
                        .addComponent(BU_Kriteria))
                    .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 1150, Short.MAX_VALUE))
                .addGap(50, 50, 50))
        );
        P_KriteriaLayout.setVerticalGroup(
            P_KriteriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_KriteriaLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(P_KriteriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(P_KriteriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(BU_Kriteria, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(BT_Kriteriia))
                    .addGroup(P_KriteriaLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jButton3)
                        .addComponent(BT_RefreshKriteria)))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, 373, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(87, Short.MAX_VALUE))
        );

        P_Penilaian.setBackground(new java.awt.Color(255, 255, 255));
        P_Penilaian.setPreferredSize(new java.awt.Dimension(1250, 538));

        jLabel4.setFont(new java.awt.Font("Verdana", 0, 24)); // NOI18N
        jLabel4.setText("FORM NILAI");

        jPanel2.setBackground(new java.awt.Color(0, 114, 198));
        jPanel2.setToolTipText("");
        jPanel2.setPreferredSize(new java.awt.Dimension(1200, 406));

        jLabel5.setBackground(new java.awt.Color(255, 255, 255));
        jLabel5.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("BULAN");

        jLabel6.setBackground(new java.awt.Color(255, 255, 255));
        jLabel6.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("DATA KARYAWAN");

        jLabel7.setBackground(new java.awt.Color(255, 255, 255));
        jLabel7.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("PERIODE");

        jLabel8.setBackground(new java.awt.Color(255, 255, 255));
        jLabel8.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("TAHUN");

        jLabel9.setBackground(new java.awt.Color(255, 255, 255));
        jLabel9.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("NAMA");

        CmbNikKaryawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CmbNikKaryawanActionPerformed(evt);
            }
        });

        jLabel10.setBackground(new java.awt.Color(255, 255, 255));
        jLabel10.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("NIK");

        InputNamaKaryawan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InputNamaKaryawanActionPerformed(evt);
            }
        });

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("DIVISI");

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("JABATAN");

        jLabel13.setBackground(new java.awt.Color(255, 255, 255));
        jLabel13.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("TGL BERGABUNG");

        jLabel14.setBackground(new java.awt.Color(255, 255, 255));
        jLabel14.setFont(new java.awt.Font("Verdana", 0, 18)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setText("PENILAIAN");

        LabelK1.setBackground(new java.awt.Color(255, 255, 255));
        LabelK1.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        LabelK1.setForeground(new java.awt.Color(255, 255, 255));
        LabelK1.setText("Lable 1");

        LabelK2.setBackground(new java.awt.Color(255, 255, 255));
        LabelK2.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        LabelK2.setForeground(new java.awt.Color(255, 255, 255));
        LabelK2.setText("Lable 1");

        LabelK3.setBackground(new java.awt.Color(255, 255, 255));
        LabelK3.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        LabelK3.setForeground(new java.awt.Color(255, 255, 255));
        LabelK3.setText("Lable 1");

        LabelK4.setBackground(new java.awt.Color(255, 255, 255));
        LabelK4.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        LabelK4.setForeground(new java.awt.Color(255, 255, 255));
        LabelK4.setText("Lable 1");

        LabelK5.setBackground(new java.awt.Color(255, 255, 255));
        LabelK5.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        LabelK5.setForeground(new java.awt.Color(255, 255, 255));
        LabelK5.setText("Lable 1");

        BtSimpanPenilaian.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        BtSimpanPenilaian.setText("SIMPAN");
        BtSimpanPenilaian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtSimpanPenilaianActionPerformed(evt);
            }
        });

        InputK1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InputK1ActionPerformed(evt);
            }
        });

        BT_Kedisiplinan.setText("..");
        BT_Kedisiplinan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_KedisiplinanActionPerformed(evt);
            }
        });

        BT_Kinerja.setText("..");
        BT_Kinerja.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_KinerjaActionPerformed(evt);
            }
        });

        BT_Kerjasamatim.setText("..");
        BT_Kerjasamatim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_KerjasamatimActionPerformed(evt);
            }
        });

        BT_Kemampuanteknis.setText("..");
        BT_Kemampuanteknis.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_KemampuanteknisActionPerformed(evt);
            }
        });

        BT_Pencapaianpenjualan.setText("..");
        BT_Pencapaianpenjualan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_PencapaianpenjualanActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(200, 200, 200)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jLabel6)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel5)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(InputBulanPenilaian, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel8)
                                    .addGap(18, 18, 18)
                                    .addComponent(InputTahunPenilaian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel13)
                                    .addGap(27, 27, 27)
                                    .addComponent(InputTglKaryawan))
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(jLabel10)
                                    .addGap(108, 108, 108)
                                    .addComponent(CmbNikKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel11)
                                    .addComponent(jLabel12)
                                    .addComponent(jLabel9))
                                .addGap(77, 77, 77)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(InputNamaKaryawan, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
                                    .addComponent(InputDivisiKaryawan)
                                    .addComponent(InputJabatanKaryawan))))
                        .addGap(151, 151, 151)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel14)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(LabelK5)
                                            .addComponent(LabelK4)
                                            .addComponent(LabelK3)
                                            .addComponent(LabelK1)
                                            .addComponent(LabelK2))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 150, Short.MAX_VALUE)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(BT_Kinerja, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(BT_Kedisiplinan, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(BT_Kerjasamatim, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(BT_Kemampuanteknis, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(BT_Pencapaianpenjualan, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(InputK4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                    .addComponent(InputK3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                    .addComponent(InputK2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)
                                    .addComponent(InputK1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(InputK5, javax.swing.GroupLayout.PREFERRED_SIZE, 1, Short.MAX_VALUE)))
                            .addComponent(BtSimpanPenilaian, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(184, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(InputBulanPenilaian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                    .addComponent(InputTahunPenilaian, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(19, 19, 19)
                                .addComponent(jLabel6)
                                .addGap(18, 18, 18)
                                .addComponent(CmbNikKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(jLabel10)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(InputNamaKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel11)
                            .addComponent(InputDivisiKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel12)
                            .addComponent(InputJabatanKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jLabel14)
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelK1)
                            .addComponent(InputK1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BT_Kedisiplinan))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelK2)
                            .addComponent(InputK2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BT_Kinerja))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelK3)
                            .addComponent(InputK3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BT_Kerjasamatim))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelK4)
                            .addComponent(InputK4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BT_Kemampuanteknis))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(LabelK5)
                            .addComponent(InputK5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(BT_Pencapaianpenjualan))))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(InputTglKaryawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(BtSimpanPenilaian))
                .addGap(67, 67, 67))
        );

        javax.swing.GroupLayout P_PenilaianLayout = new javax.swing.GroupLayout(P_Penilaian);
        P_Penilaian.setLayout(P_PenilaianLayout);
        P_PenilaianLayout.setHorizontalGroup(
            P_PenilaianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_PenilaianLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(P_PenilaianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(P_PenilaianLayout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, 1124, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel4))
                .addContainerGap(70, Short.MAX_VALUE))
        );
        P_PenilaianLayout.setVerticalGroup(
            P_PenilaianLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_PenilaianLayout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        P_Perhitungan.setBackground(new java.awt.Color(255, 255, 255));
        P_Perhitungan.setPreferredSize(new java.awt.Dimension(1250, 538));

        jLabel20.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel20.setText("PERIODE");

        jLabel21.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel21.setText("BULAN");

        jLabel22.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel22.setText("TAHUN");

        JT_Perhitungan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "NO", "NAMA", "NIK", "K1", "K2", "K3", "K4", "K5"
            }
        ));
        jScrollPane4.setViewportView(JT_Perhitungan);

        jLabel23.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel23.setText("PERHITUNGAN");

        jLabel24.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel24.setText("NORMALISASI");

        JT_Normalisasi.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "NO", "NAMA", "NIK", "K1", "K2", "K3", "K4", "K5", "HASIL"
            }
        ));
        jScrollPane6.setViewportView(JT_Normalisasi);

        NORMALISASI.setText("NORMALISASI");
        NORMALISASI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NORMALISASIActionPerformed(evt);
            }
        });

        RefereshPerhitungan.setText("REFRESH");
        RefereshPerhitungan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RefereshPerhitunganActionPerformed(evt);
            }
        });

        CariPeriodePerhitunganNormalisasi.setText("CARI");
        CariPeriodePerhitunganNormalisasi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CariPeriodePerhitunganNormalisasiActionPerformed(evt);
            }
        });

        BtHapus.setText("HAPUS");
        BtHapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtHapusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout P_PerhitunganLayout = new javax.swing.GroupLayout(P_Perhitungan);
        P_Perhitungan.setLayout(P_PerhitunganLayout);
        P_PerhitunganLayout.setHorizontalGroup(
            P_PerhitunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_PerhitunganLayout.createSequentialGroup()
                .addGap(50, 50, 50)
                .addGroup(P_PerhitunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(P_PerhitunganLayout.createSequentialGroup()
                        .addComponent(jLabel20)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, P_PerhitunganLayout.createSequentialGroup()
                        .addGroup(P_PerhitunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(P_PerhitunganLayout.createSequentialGroup()
                                .addGroup(P_PerhitunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel23)
                                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 534, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(P_PerhitunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(P_PerhitunganLayout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(jLabel24))
                                    .addComponent(jScrollPane6, javax.swing.GroupLayout.DEFAULT_SIZE, 598, Short.MAX_VALUE)))
                            .addGroup(P_PerhitunganLayout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addGap(18, 18, 18)
                                .addComponent(InputBulan1, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel22)
                                .addGap(18, 18, 18)
                                .addComponent(InputTahun1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(CariPeriodePerhitunganNormalisasi, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(BtHapus)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(RefereshPerhitungan)
                                .addGap(18, 18, 18)
                                .addComponent(NORMALISASI)))
                        .addGap(50, 50, 50))))
        );
        P_PerhitunganLayout.setVerticalGroup(
            P_PerhitunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, P_PerhitunganLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel20)
                .addGap(18, 18, 18)
                .addGroup(P_PerhitunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(P_PerhitunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel21)
                        .addComponent(jLabel22)
                        .addComponent(NORMALISASI)
                        .addComponent(RefereshPerhitungan)
                        .addComponent(CariPeriodePerhitunganNormalisasi)
                        .addComponent(BtHapus))
                    .addComponent(InputBulan1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(InputTahun1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addGroup(P_PerhitunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(jLabel24))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(P_PerhitunganLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(69, 69, 69))
        );

        P_Ranking.setBackground(new java.awt.Color(255, 255, 255));
        P_Ranking.setPreferredSize(new java.awt.Dimension(1250, 538));

        jLabel25.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel25.setText("PERIODE");

        jLabel26.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel26.setText("BULAN");

        jLabel27.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jLabel27.setText("TAHUN");

        CariRanking.setText("CARI");
        CariRanking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CariRankingActionPerformed(evt);
            }
        });

        JT_Ranking.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "NO", "NAMA", "NIK", "K1", "K2", "K3", "K4", "K5", "HASIL", "STATUS"
            }
        ));
        jScrollPane7.setViewportView(JT_Ranking);

        PrintRanking.setIcon(new javax.swing.ImageIcon(getClass().getResource("/pk_electroniccity/icon/print (1).png"))); // NOI18N
        PrintRanking.setBorder(null);
        PrintRanking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PrintRankingActionPerformed(evt);
            }
        });

        TarikData.setText("TARIK DATA");
        TarikData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TarikDataActionPerformed(evt);
            }
        });

        jButton11.setText("REFRESH");
        jButton11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton11ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout P_RankingLayout = new javax.swing.GroupLayout(P_Ranking);
        P_Ranking.setLayout(P_RankingLayout);
        P_RankingLayout.setHorizontalGroup(
            P_RankingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_RankingLayout.createSequentialGroup()
                .addGroup(P_RankingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(PrintRanking)
                    .addGroup(P_RankingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(P_RankingLayout.createSequentialGroup()
                            .addGap(50, 50, 50)
                            .addGroup(P_RankingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(P_RankingLayout.createSequentialGroup()
                                    .addComponent(jLabel26)
                                    .addGap(18, 18, 18)
                                    .addComponent(InputBulan2, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel27)
                                    .addGap(18, 18, 18)
                                    .addComponent(InputTahun2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(26, 26, 26)
                                    .addComponent(CariRanking, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(TarikData, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(18, 18, 18)
                                    .addComponent(jButton11, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jLabel25)))
                        .addGroup(P_RankingLayout.createSequentialGroup()
                            .addGap(60, 60, 60)
                            .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 1135, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(55, Short.MAX_VALUE))
        );
        P_RankingLayout.setVerticalGroup(
            P_RankingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_RankingLayout.createSequentialGroup()
                .addGap(42, 42, 42)
                .addComponent(jLabel25)
                .addGap(18, 18, 18)
                .addGroup(P_RankingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(P_RankingLayout.createSequentialGroup()
                        .addGroup(P_RankingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(P_RankingLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jLabel26, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jLabel27, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(CariRanking)
                                .addComponent(TarikData)
                                .addComponent(jButton11))
                            .addComponent(InputBulan2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(31, 31, 31)
                        .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 302, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(PrintRanking))
                    .addComponent(InputTahun2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(46, Short.MAX_VALUE))
        );

        P_Pengaturan.setBackground(new java.awt.Color(255, 255, 255));
        P_Pengaturan.setPreferredSize(new java.awt.Dimension(1250, 538));

        jLabel28.setFont(new java.awt.Font("Verdana", 1, 24)); // NOI18N
        jLabel28.setText("PENGATURAN AKUN");

        jLabel29.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel29.setText("USERNAME");

        jLabel30.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel30.setText("PASSWORD LAMA");

        BtSimpanAkun.setBackground(new java.awt.Color(0, 114, 198));
        BtSimpanAkun.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        BtSimpanAkun.setForeground(new java.awt.Color(255, 255, 255));
        BtSimpanAkun.setText("SIMPAN");
        BtSimpanAkun.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BtSimpanAkunActionPerformed(evt);
            }
        });

        jLabel31.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel31.setText("PASSWORD BARU");

        jLabel32.setFont(new java.awt.Font("Verdana", 1, 18)); // NOI18N
        jLabel32.setText("KONFIRMASI PASSWORD BARU");

        InputPwLama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                InputPwLamaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout P_PengaturanLayout = new javax.swing.GroupLayout(P_Pengaturan);
        P_Pengaturan.setLayout(P_PengaturanLayout);
        P_PengaturanLayout.setHorizontalGroup(
            P_PengaturanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_PengaturanLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(P_PengaturanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel31)
                    .addComponent(jLabel30)
                    .addComponent(jLabel28)
                    .addComponent(jLabel29)
                    .addComponent(InputUsername)
                    .addComponent(jLabel32)
                    .addComponent(BtSimpanAkun, javax.swing.GroupLayout.DEFAULT_SIZE, 417, Short.MAX_VALUE)
                    .addComponent(InputPwLama)
                    .addComponent(InputKPwBaru)
                    .addComponent(InputPwBaru))
                .addContainerGap(773, Short.MAX_VALUE))
        );
        P_PengaturanLayout.setVerticalGroup(
            P_PengaturanLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_PengaturanLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addComponent(jLabel28)
                .addGap(18, 18, 18)
                .addComponent(jLabel29)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(InputUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(InputPwLama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel31)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(InputPwBaru, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(jLabel32)
                .addGap(12, 12, 12)
                .addComponent(InputKPwBaru, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(BtSimpanAkun, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(111, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout P_IsiLayout = new javax.swing.GroupLayout(P_Isi);
        P_Isi.setLayout(P_IsiLayout);
        P_IsiLayout.setHorizontalGroup(
            P_IsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_IsiLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(P_IsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(P_Ranking, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(P_Pengaturan, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(P_IsiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(P_Awal, javax.swing.GroupLayout.PREFERRED_SIZE, 1241, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(P_IsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(P_IsiLayout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(P_IsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(P_Karyawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(P_Jabatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(P_Kriteria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(P_Penilaian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(P_Perhitungan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );
        P_IsiLayout.setVerticalGroup(
            P_IsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_IsiLayout.createSequentialGroup()
                .addComponent(P_Awal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(P_Ranking, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(P_Pengaturan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(1094, Short.MAX_VALUE))
            .addGroup(P_IsiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, P_IsiLayout.createSequentialGroup()
                    .addContainerGap(550, Short.MAX_VALUE)
                    .addComponent(P_Karyawan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(P_Jabatan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(P_Kriteria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(P_Penilaian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(P_Perhitungan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        javax.swing.GroupLayout P_HomeLayout = new javax.swing.GroupLayout(P_Home);
        P_Home.setLayout(P_HomeLayout);
        P_HomeLayout.setHorizontalGroup(
            P_HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(P_Isi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, P_HomeLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(P_Menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        P_HomeLayout.setVerticalGroup(
            P_HomeLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(P_HomeLayout.createSequentialGroup()
                .addComponent(P_Menu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(P_Isi, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        getContentPane().add(P_Home, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, -1, -1));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void B_JabatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_JabatanActionPerformed
        //Sembunyikan Panel
        P_Awal.setVisible(false);
        P_Kriteria.setVisible(false);
        P_Penilaian.setVisible(false);
        P_Perhitungan.setVisible(false);
        P_Ranking.setVisible(false);
        P_Pengaturan.setVisible(false);
        P_Karyawan.setVisible(false);
        
        //Tampilkan Panel
        P_Jabatan.setVisible(true);
    }//GEN-LAST:event_B_JabatanActionPerformed

    private void B_KaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_KaryawanActionPerformed
        //Sembunyikan Panel
        P_Awal.setVisible(false);
        P_Jabatan.setVisible(false);
        P_Kriteria.setVisible(false);
        P_Penilaian.setVisible(false);
        P_Perhitungan.setVisible(false);
        P_Ranking.setVisible(false);
        P_Pengaturan.setVisible(false);
        
        //Tampilkan Panel
        P_Karyawan.setVisible(true);
        
    }//GEN-LAST:event_B_KaryawanActionPerformed

    private void B_KriteriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_KriteriaActionPerformed
        //Sembunyikan Panel
        P_Awal.setVisible(false);
        P_Penilaian.setVisible(false);
        P_Perhitungan.setVisible(false);
        P_Ranking.setVisible(false);
        P_Pengaturan.setVisible(false);
        P_Karyawan.setVisible(false);
        P_Jabatan.setVisible(false);
        
        //Tampilkan Panel
        P_Kriteria.setVisible(true);
        
    }//GEN-LAST:event_B_KriteriaActionPerformed

    private void B_PenilaianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_PenilaianActionPerformed
        //Sembunyikan Panel
        P_Awal.setVisible(false);
        P_Perhitungan.setVisible(false);
        P_Ranking.setVisible(false);
        P_Pengaturan.setVisible(false);
        P_Kriteria.setVisible(false);
        P_Karyawan.setVisible(false);
        P_Jabatan.setVisible(false);
        
        //Tampilkan Panel
        P_Penilaian.setVisible(true);
    }//GEN-LAST:event_B_PenilaianActionPerformed

    private void B_PerhitunganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_PerhitunganActionPerformed
        //Sembunyikan Panel
        P_Awal.setVisible(false);
        P_Ranking.setVisible(false);
        P_Pengaturan.setVisible(false);
        P_Kriteria.setVisible(false);
        P_Karyawan.setVisible(false);
        P_Penilaian.setVisible(false);
        P_Jabatan.setVisible(false);
        
        //Tampilkan Panel
        P_Perhitungan.setVisible(true);
    }//GEN-LAST:event_B_PerhitunganActionPerformed

    private void B_KeluarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_KeluarActionPerformed
        // Menampikan response
        int response = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin keluar?", "Konfirmasi", JOptionPane.YES_NO_OPTION);
        
        // Jika Pilih yes
        if (response == JOptionPane.YES_OPTION){
            System.exit(0);} // Keluar
    }//GEN-LAST:event_B_KeluarActionPerformed

    private void B_RankingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_RankingActionPerformed
        //Sembunyikan Panel
        P_Awal.setVisible(false);
        P_Perhitungan.setVisible(false);
        P_Pengaturan.setVisible(false);
        P_Kriteria.setVisible(false);
        P_Karyawan.setVisible(false);
        P_Penilaian.setVisible(false);
        P_Jabatan.setVisible(false); 
        
        //Tampilkan Panel
        P_Ranking.setVisible(true);
    }//GEN-LAST:event_B_RankingActionPerformed

    private void B_PengaturanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_B_PengaturanActionPerformed
        //Sembunyikan Panel
        P_Awal.setVisible(false);
        P_Perhitungan.setVisible(false);
        P_Kriteria.setVisible(false);
        P_Karyawan.setVisible(false);
        P_Penilaian.setVisible(false);
        P_Jabatan.setVisible(false);
        P_Ranking.setVisible(false);
        
        //Tampilkan Panel
        P_Pengaturan.setVisible(true);
    }//GEN-LAST:event_B_PengaturanActionPerformed

    private void InputCariKaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InputCariKaryawanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InputCariKaryawanActionPerformed

    private void BT_CariKaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_CariKaryawanActionPerformed
    String keyword = InputCariKaryawan.getText().trim(); // Ambil input dari text field
    DefaultTableModel tb = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    // Tambahkan kolom tabel
    tb.addColumn("NIK");
    tb.addColumn("NAMA");
    tb.addColumn("DIVISI");
    tb.addColumn("JABATAN");
    tb.addColumn("TGL BERGABUNG");
    JT_Karayawan.setModel(tb);

    try {
        // Koneksi ke database
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_spksaw", "root", "");
        Statement stmt = con.createStatement();
        
        // Query untuk pencarian data
        String query = "SELECT * FROM karyawan WHERE nik_karyawan LIKE '%" + keyword + "%' " +
                       "OR nama_karyawan LIKE '%" + keyword + "%' " +
                       "OR divisi_karyawan LIKE '%" + keyword + "%' " +
                       "OR jabatan_karyawan LIKE '%" + keyword + "%' " +
                       "OR tglmasuk_karyawan LIKE '%" + keyword + "%'";
        ResultSet rs = stmt.executeQuery(query);

        // Masukkan hasil pencarian ke dalam tabel
        while (rs.next()) {
            tb.addRow(new Object[]{
                rs.getString("nik_karyawan"),
                rs.getString("nama_karyawan"),
                rs.getString("divisi_karyawan"),
                rs.getString("jabatan_karyawan"),
                rs.getString("tglmasuk_karyawan")
            });
        }

        // Tutup koneksi
        rs.close();
        stmt.close();
        con.close();
        
        if (tb.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Data tidak ditemukan!");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Pencarian gagal: " + e.getMessage());
    }
    }//GEN-LAST:event_BT_CariKaryawanActionPerformed

    private void BT_KaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_KaryawanActionPerformed
        // Membuat instance form T_Karyawan
        T_Karyawan tKaryawanForm = new T_Karyawan();
        
        // Menampilkan form T_Karyawan
        tKaryawanForm.setVisible(true);
    }//GEN-LAST:event_BT_KaryawanActionPerformed

    
    private void BU_KaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BU_KaryawanActionPerformed
    int selectedRow = JT_Karayawan.getSelectedRow(); // Ambil baris yang dipilih
    if (selectedRow != -1) {
        // Ambil data dari JTable berdasarkan kolom
        String nik = JT_Karayawan.getValueAt(selectedRow, 0).toString();
        String nama = JT_Karayawan.getValueAt(selectedRow, 1).toString();
        String divisi = JT_Karayawan.getValueAt(selectedRow, 2).toString();
        String jabatan = JT_Karayawan.getValueAt(selectedRow, 3).toString();
        String tgl = JT_Karayawan.getValueAt(selectedRow, 4).toString();

        // Membuka form edit dengan data dari JTable
        E_Karyawan editForm = new E_Karyawan();
        editForm.setFormData(nik, nama, divisi, jabatan, tgl);
        editForm.setVisible(true);
    } else {
        JOptionPane.showMessageDialog(null, "Pilih baris terlebih dahulu untuk mengedit!");
    }   
    }//GEN-LAST:event_BU_KaryawanActionPerformed
    

    private void Print_KaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Print_KaryawanActionPerformed
        try {
            // Koneksi ke database
            Connection conn = db_connect.connect();

            // Path file .jasper relatif dari folder src
            String filePath = "src/pk_electroniccity/CetakKaryawan.jasper";

            // Jika ada parameter untuk laporan, tambahkan di sini
            Map<String, Object> parameters = new HashMap<>();
            // Contoh: parameters.put("key_parameter", nilai_parameter);

            // Generate laporan
            JasperPrint jp = JasperFillManager.fillReport(filePath, parameters, conn);

            // Tampilkan laporan menggunakan JasperViewer
            JasperViewer.viewReport(jp, false);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Gagal mencetak laporan: " + e.getMessage());
            e.printStackTrace();
        }
    }//GEN-LAST:event_Print_KaryawanActionPerformed

    private void InputCariDivisiJabatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InputCariDivisiJabatanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InputCariDivisiJabatanActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
    String keyword = InputCariDivisiJabatan.getText().trim(); // Ambil input dari text field
    DefaultTableModel tb = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    // Tambahkan kolom tabel
    tb.addColumn("DIVISI");
    JT_Divisi.setModel(tb);

    try {
        // Koneksi ke database
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_spksaw", "root", "");
        Statement stmt = con.createStatement();
        
        // Query untuk pencarian data
        String query = "SELECT * FROM divisi WHERE nama_divisi LIKE '%" + keyword + "%' ";
        ResultSet rs = stmt.executeQuery(query);

        // Masukkan hasil pencarian ke dalam tabel
        while (rs.next()) {
            tb.addRow(new Object[]{
                rs.getString("nama_divisi"),
            });
        }

        // Tutup koneksi
        rs.close();
        stmt.close();
        con.close();
        
        if (tb.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Data tidak ditemukan!");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Pencarian gagal: " + e.getMessage());
    }    
    }//GEN-LAST:event_jButton2ActionPerformed

    private void BT_JabatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_JabatanActionPerformed
        // Membuat instance
        T_Jabatan T_Jabatan = new T_Jabatan();
        
        // Menampilkan form
        T_Jabatan.setVisible(true);
    }//GEN-LAST:event_BT_JabatanActionPerformed

    private void BU_JabatanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BU_JabatanActionPerformed
    int selectedRow = JT_Jabatan.getSelectedRow(); // Ambil baris yang dipilih
    if (selectedRow != -1) {
        // Ambil data dari JTable berdasarkan kolom
        String jabatan = JT_Jabatan.getValueAt(selectedRow, 0).toString();
        // Membuka form edit dengan data dari JTable
        E_Jabatan editForm = new E_Jabatan();
        editForm.setJabatan(jabatan);
        editForm.setVisible(true);
    } else {
        JOptionPane.showMessageDialog(null, "Pilih baris terlebih dahulu untuk mengedit!");
    } 
    }//GEN-LAST:event_BU_JabatanActionPerformed

    private void jTextField3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField3ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void BT_KriteriiaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_KriteriiaActionPerformed
        if (isMaxRowsReached()) {
        JOptionPane.showMessageDialog(null, "Maksimal 5 data telah tercapai. Tidak dapat menambahkan data baru.");
        return;
        }
        // Membuat Instance
        T_Kriteria T_Kriteria = new T_Kriteria();
        
        //Menampilkan form
        T_Kriteria.setVisible(true);   
    
    }//GEN-LAST:event_BT_KriteriiaActionPerformed

    private void BU_KriteriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BU_KriteriaActionPerformed
    int selectedRow = JT_Kriteria.getSelectedRow(); // Ambil baris yang dipilih
    if (selectedRow != -1) {
        // Ambil data dari JTable berdasarkan kolom
        String kode = JT_Kriteria.getValueAt(selectedRow, 0).toString();
        String kriteria = JT_Kriteria.getValueAt(selectedRow, 1).toString();
        String bobot = JT_Kriteria.getValueAt(selectedRow, 2).toString();
        // Membuka form edit dengan data dari JTable
        E_Kriteria editForm = new E_Kriteria();
        editForm.setKriteria(kode, kriteria, bobot);
        editForm.setVisible(true);
    } else {
        JOptionPane.showMessageDialog(null, "Pilih baris terlebih dahulu untuk mengedit!");
    } 
    }//GEN-LAST:event_BU_KriteriaActionPerformed

    private void InputNamaKaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InputNamaKaryawanActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InputNamaKaryawanActionPerformed

private String periodeCari; 
    
    private void CariRankingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CariRankingActionPerformed
    DefaultTableModel modelRanking = new DefaultTableModel();

    // Set kolom untuk tabel ranking
    modelRanking.addColumn("PERIODE");
    modelRanking.addColumn("NIK");
    modelRanking.addColumn("NAMA");
    modelRanking.addColumn("K1");
    modelRanking.addColumn("K2");
    modelRanking.addColumn("K3");
    modelRanking.addColumn("K4");
    modelRanking.addColumn("K5");
    modelRanking.addColumn("BOBOT");
    modelRanking.addColumn("STATUS");

    try (Connection conn = db_connect.connect()) {
        // Ambil bulan dan tahun dari GUI
        int bulanIndex = InputBulan2.getMonth() + 1; // JMonthChooser
        int tahun = InputTahun2.getYear();          // JYearChooser
        String[] namaBulan = {"Januari", "Februari", "Maret", "April", "Mei", "Juni", "Juli", "Agustus", "September", "Oktober", "November", "Desember"};
        periodeCari = namaBulan[bulanIndex - 1] + " " + tahun; // Simpan untuk cetak

        // Debugging: cetak periode yang dicari
        System.out.println("Periode yang dicari: " + periodeCari);

        // Query ke tabel ranking
        String sqlRanking = "SELECT * FROM ranking WHERE periode = ?";
        PreparedStatement stmtRanking = conn.prepareStatement(sqlRanking);
        stmtRanking.setString(1, periodeCari);
        ResultSet rsRanking = stmtRanking.executeQuery();

        // Proses hasil query
        boolean adaData = false;
        while (rsRanking.next()) {
            adaData = true;
            modelRanking.addRow(new Object[]{
                rsRanking.getString("periode"),
                rsRanking.getInt("nik_ranking"),
                rsRanking.getString("nama_ranking"),
                rsRanking.getDouble("k1_ranking"),
                rsRanking.getDouble("k2_ranking"),
                rsRanking.getDouble("k3_ranking"),
                rsRanking.getDouble("k4_ranking"),
                rsRanking.getDouble("k5_ranking"),
                rsRanking.getDouble("bobot_ranking"),
                rsRanking.getString("status_ranking")
            });
        }

        if (!adaData) {
            JOptionPane.showMessageDialog(null, "Tidak ada data untuk periode: " + periodeCari);
        } else {
            // Tampilkan di tabel
            JT_Ranking.setModel(modelRanking);
            JOptionPane.showMessageDialog(null, "Data ranking berhasil ditemukan!");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal mencari data ranking: " + e.getMessage());
    }
    }//GEN-LAST:event_CariRankingActionPerformed

    private void PrintRankingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PrintRankingActionPerformed
    if (periodeCari == null || periodeCari.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Silakan cari data terlebih dahulu sebelum mencetak laporan!");
        return;
    }

    try {
        // Path file Jasper
        String filePath = "src/pk_electroniccity/ranking.jasper"; 

        // Koneksi database
        Connection conn = db_connect.connect();

        // Parameter laporan
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("P_Periode", periodeCari); // Sesuaikan dengan nama parameter di JRXML

        // Generate laporan
        JasperPrint jp = JasperFillManager.fillReport(filePath, parameters, conn);

        // Debug jumlah halaman
        System.out.println("Jumlah halaman laporan: " + jp.getPages().size());

        // Tampilkan laporan jika ada halaman
        if (jp.getPages().size() > 0) {
            JasperViewer.viewReport(jp, false);
        } else {
            JOptionPane.showMessageDialog(null, "Laporan tidak memiliki data untuk ditampilkan.");
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Gagal mencetak laporan: " + e.getMessage());
        e.printStackTrace();
    }
    }//GEN-LAST:event_PrintRankingActionPerformed

    private void BT_RefreshActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_RefreshActionPerformed
        // TODO add your handling code here:
    ConnKaryawan();   
     JOptionPane.showMessageDialog(null, "Data berhasil diperbarui!", "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_BT_RefreshActionPerformed

    private void JT_KarayawanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_JT_KarayawanMouseClicked

    }//GEN-LAST:event_JT_KarayawanMouseClicked

    private void BT_DivisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_DivisiActionPerformed
        T_Divisi T_Divisi = new T_Divisi();        
                T_Divisi.setVisible(true);

    }//GEN-LAST:event_BT_DivisiActionPerformed

    private void BU_DivisiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BU_DivisiActionPerformed
    int selectedRow = JT_Divisi.getSelectedRow(); // Ambil baris yang dipilih
    if (selectedRow != -1) {
        // Ambil data dari JTable berdasarkan kolom
        String divisi = JT_Divisi.getValueAt(selectedRow, 0).toString();
        // Membuka form edit dengan data dari JTable
        E_Divisi editForm = new E_Divisi();
        editForm.setDivisi(divisi);
        editForm.setVisible(true);
    } else {
        JOptionPane.showMessageDialog(null, "Pilih baris terlebih dahulu untuk mengedit!");
    } 
    }//GEN-LAST:event_BU_DivisiActionPerformed

    private void BT_Refresh1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_Refresh1ActionPerformed
        ConnDivisi();
        ConnJabatan();
            JOptionPane.showMessageDialog(null,"Data Diperbarui", "Informasi", JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_BT_Refresh1ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
    String keyword = InputCariDivisiJabatan.getText().trim(); // Ambil input dari text field
    DefaultTableModel tb = new DefaultTableModel(){
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };
    
    // Tambahkan kolom tabel
    tb.addColumn("JABATAN");
    JT_Jabatan.setModel(tb);

    try {
        // Koneksi ke database
        Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/db_spksaw", "root", "");
        Statement stmt = con.createStatement();
        
        // Query untuk pencarian data
        String query = "SELECT * FROM jabatan WHERE nama_jabatan LIKE '%" + keyword + "%' ";
        ResultSet rs = stmt.executeQuery(query);

        // Masukkan hasil pencarian ke dalam tabel
        while (rs.next()) {
            tb.addRow(new Object[]{
                rs.getString("nama_jabatan"),
            });
        }

        // Tutup koneksi
        rs.close();
        stmt.close();
        con.close();
        
        if (tb.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Data tidak ditemukan!");
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Pencarian gagal: " + e.getMessage());
    }
    }//GEN-LAST:event_jButton9ActionPerformed

    private void BT_RefreshKriteriaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_RefreshKriteriaActionPerformed
        ConnKriteria();
        JOptionPane.showMessageDialog(null, "Data Berhasil Diperbarui", "informasi",JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_BT_RefreshKriteriaActionPerformed

    private void CmbNikKaryawanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CmbNikKaryawanActionPerformed
    String selectedNik = (String) CmbNikKaryawan.getSelectedItem();

    if (!"Pilih NIK".equals(selectedNik)) {
        try (Connection conn = db_connect.connect()) {
            // Query untuk mendapatkan data karyawan berdasarkan NIK
            String sql = "SELECT nama_karyawan, divisi_karyawan, jabatan_karyawan, tglmasuk_karyawan FROM karyawan WHERE nik_karyawan = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, selectedNik);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                // Isi field form dengan data dari database
                InputNamaKaryawan.setText(rs.getString("nama_karyawan"));
                InputDivisiKaryawan.setText(rs.getString("divisi_karyawan"));
                InputJabatanKaryawan.setText(rs.getString("jabatan_karyawan"));
                InputTglKaryawan.setText(rs.getString("tglmasuk_karyawan"));
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Gagal memuat data karyawan: " + e.getMessage());
        }
    } else {
        // Kosongkan semua field jika NIK "Pilih NIK" dipilih
        InputNamaKaryawan.setText("");
        InputDivisiKaryawan.setText("");
        InputJabatanKaryawan.setText("");
        InputTglKaryawan.setText("");
    }
   
    }//GEN-LAST:event_CmbNikKaryawanActionPerformed
    private void resetForm() {
        CmbNikKaryawan.setSelectedIndex(0);
        InputNamaKaryawan.setText("");
        InputK1.setText("");
        InputK2.setText("");
        InputK3.setText("");
        InputK4.setText("");
        InputK5.setText("");
        InputBulanPenilaian.setMonth(0);
        InputTahunPenilaian.setYear(2025);
        CmbNikKaryawan.requestFocus();
    }

    private void BtSimpanPenilaianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtSimpanPenilaianActionPerformed
    
   String NikKaryawan = (String) CmbNikKaryawan.getSelectedItem();
    String NamaKaryawan = InputNamaKaryawan.getText();
    String K1 = (String) InputK1.getText();
    String K2 = (String) InputK2.getText();
    String K3 = (String) InputK3.getText();
    String K4 = (String) InputK4.getText();
    String K5 = (String) InputK5.getText();

    int bulan = InputBulanPenilaian.getMonth() + 1;
    int tahun = InputTahunPenilaian.getYear();
    
    String[] namaBulan = {
        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    };

    String periode = namaBulan[bulan - 1] + " " + tahun;
    
    // Validasi input
    if (NikKaryawan.isEmpty() || NamaKaryawan.isEmpty() || periode.isEmpty() || K1.isEmpty()
        || K2.isEmpty() || K3.isEmpty() || K4.isEmpty() 
        || K5.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Harap Lengkapi Semua Data", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try (Connection conn = db_connect.connect()) {
        // Cek apakah NIK dan periode sudah ada
        String checkSql = "SELECT COUNT(*) AS count FROM perhitungan WHERE nik_perhitungan = ? AND periode = ?";
        PreparedStatement checkStmt = conn.prepareStatement(checkSql);
        checkStmt.setInt(1, Integer.parseInt(NikKaryawan));
        checkStmt.setString(2, periode);
        ResultSet rs = checkStmt.executeQuery();
        
        if (rs.next() && rs.getInt("count") > 0) {
            JOptionPane.showMessageDialog(null, "Data dengan NIK ini sudah ada untuk periode yang sama!", "Peringatan", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Insert data ke tabel perhitungan
        String sql = "INSERT INTO perhitungan (nik_perhitungan, nama_perhitungan, periode, k1_perhitungan, k2_perhitungan, k3_perhitungan, k4_perhitungan, k5_perhitungan) VALUES (?,?,?,?,?,?,?,?)";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, Integer.parseInt(NikKaryawan)); 
        stmt.setString(2, NamaKaryawan);              
        stmt.setString(3, periode);                                  
        stmt.setDouble(4, Double.parseDouble(K1)); 
        stmt.setDouble(5, Double.parseDouble(K2)); 
        stmt.setDouble(6, Double.parseDouble(K3)); 
        stmt.setDouble(7, Double.parseDouble(K4)); 
        stmt.setDouble(8, Double.parseDouble(K5));
        
        stmt.executeUpdate();
        JOptionPane.showMessageDialog(null, "Data berhasil disimpan!");
        resetForm();
           
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Gagal menyimpan data: " + e.getMessage());
    }
    }//GEN-LAST:event_BtSimpanPenilaianActionPerformed

    private void RefereshPerhitunganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RefereshPerhitunganActionPerformed
        ConnNormalisasi();
        ConnPerhitungan();
        JOptionPane.showMessageDialog(null, "Data Berhasil Diperbarui", "informasi",JOptionPane.INFORMATION_MESSAGE);
    }//GEN-LAST:event_RefereshPerhitunganActionPerformed

    private void NORMALISASIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NORMALISASIActionPerformed
 try (Connection conn = db_connect.connect()) {
        
        // Kosongkan tabel normalisasi sebelum memasukkan data baru
        String sqlTruncate = "TRUNCATE TABLE normalisasi";
        PreparedStatement stmtTruncate = conn.prepareStatement(sqlTruncate);
        stmtTruncate.executeUpdate();
        
        // Ambil nilai maksimum setiap kriteria dari tabel perhitungan
        String sqlMax = "SELECT MAX(k1_perhitungan) AS max_k1, MAX(k2_perhitungan) AS max_k2, "
                + "MAX(k3_perhitungan) AS max_k3, MAX(k4_perhitungan) AS max_k4, MAX(k5_perhitungan) AS max_k5 FROM perhitungan";
        PreparedStatement stmtMax = conn.prepareStatement(sqlMax);
        ResultSet rsMax = stmtMax.executeQuery();
        
        double maxK1 = 0, maxK2 = 0, maxK3 = 0, maxK4 = 0, maxK5 = 0;
        if (rsMax.next()) {
            maxK1 = rsMax.getDouble("max_k1");
            maxK2 = rsMax.getDouble("max_k2");
            maxK3 = rsMax.getDouble("max_k3");
            maxK4 = rsMax.getDouble("max_k4");
            maxK5 = rsMax.getDouble("max_k5");
        }

        // Validasi nilai maksimum
        if (maxK1 == 0 || maxK2 == 0 || maxK3 == 0 || maxK4 == 0 || maxK5 == 0) {
            JOptionPane.showMessageDialog(null, "Nilai maksimum tidak boleh 0. Periksa data perhitungan!");
            return;
        }

        // Ambil bobot dari tabel kriteria
        String sqlBobot = "SELECT kode_kriteria, bobot_kriteria FROM kriteria";
        PreparedStatement stmtBobot = conn.prepareStatement(sqlBobot);
        ResultSet rsBobot = stmtBobot.executeQuery();

        // Simpan bobot kriteria dalam variabel
        double bobotK1 = 0, bobotK2 = 0, bobotK3 = 0, bobotK4 = 0, bobotK5 = 0;
        while (rsBobot.next()) {
            switch (rsBobot.getString("kode_kriteria")) {
                case "K1": bobotK1 = rsBobot.getDouble("bobot_kriteria"); break;
                case "K2": bobotK2 = rsBobot.getDouble("bobot_kriteria"); break;
                case "K3": bobotK3 = rsBobot.getDouble("bobot_kriteria"); break;
                case "K4": bobotK4 = rsBobot.getDouble("bobot_kriteria"); break;
                case "K5": bobotK5 = rsBobot.getDouble("bobot_kriteria"); break;
            }
        }

        // Ambil data dari tabel perhitungan
        String sqlPerhitungan = "SELECT * FROM perhitungan";
        PreparedStatement stmtPerhitungan = conn.prepareStatement(sqlPerhitungan);
        ResultSet rsPerhitungan = stmtPerhitungan.executeQuery();
        
        // Query untuk memasukkan hasil normalisasi ke tabel
        String sqlInsertNormalisasi = "INSERT INTO normalisasi (nik_normalisasi, nama_normalisasi, "
                + "k1_normalisasi, k2_normalisasi, k3_normalisasi, k4_normalisasi, k5_normalisasi, "
                + "bobot_normalisasi, periode) VALUES (?,?,?,?,?,?,?,?,?)";
        PreparedStatement stmtInsert = conn.prepareStatement(sqlInsertNormalisasi);
        
        DecimalFormat df = new DecimalFormat("#.##"); // Format untuk 2 angka desimal
        
        while (rsPerhitungan.next()) {
            // Ambil nilai per kriteria
            int nik = rsPerhitungan.getInt("nik_perhitungan");
            String nama = rsPerhitungan.getString("nama_perhitungan");
            String periode = rsPerhitungan.getString("periode");
            double k1 = rsPerhitungan.getDouble("k1_perhitungan");
            double k2 = rsPerhitungan.getDouble("k2_perhitungan");
            double k3 = rsPerhitungan.getDouble("k3_perhitungan");
            double k4 = rsPerhitungan.getDouble("k4_perhitungan");
            double k5 = rsPerhitungan.getDouble("k5_perhitungan");
            
            // Normalisasi
            double r1 = k1 / maxK1;
            double r2 = k2 / maxK2;
            double r3 = k3 / maxK3;
            double r4 = k4 / maxK4;
            double r5 = k5 / maxK5;
            
            // Hitung bobot normalisasi berdasarkan bobot kriteria
            double bobotNormalisasi = (r1 * bobotK1) + (r2 * bobotK2) + (r3 * bobotK3) + (r4 * bobotK4) + (r5 * bobotK5);

            // Format nilai ke 2 desimal
            r1 = Double.parseDouble(df.format(r1));
            r2 = Double.parseDouble(df.format(r2));
            r3 = Double.parseDouble(df.format(r3));
            r4 = Double.parseDouble(df.format(r4));
            r5 = Double.parseDouble(df.format(r5));
            bobotNormalisasi = Double.parseDouble(df.format(bobotNormalisasi));
            
            // Masukkan ke tabel normalisasi
            stmtInsert.setInt(1, nik);
            stmtInsert.setString(2, nama);
            stmtInsert.setDouble(3, r1);
            stmtInsert.setDouble(4, r2);
            stmtInsert.setDouble(5, r3);
            stmtInsert.setDouble(6, r4);
            stmtInsert.setDouble(7, r5);
            stmtInsert.setDouble(8, bobotNormalisasi);
            stmtInsert.setString(9, periode);
            stmtInsert.executeUpdate();
        }

        JOptionPane.showMessageDialog(null, "Normalisasi berhasil dilakukan!");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal melakukan normalisasi: " + e.getMessage());
    }
    }//GEN-LAST:event_NORMALISASIActionPerformed

    private void TarikDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TarikDataActionPerformed
                                        

    try (Connection conn = db_connect.connect()) {

        // Kosongkan tabel ranking sebelum proses
        String sqlTruncate = "TRUNCATE TABLE ranking";
        PreparedStatement stmtTruncate = conn.prepareStatement(sqlTruncate);
        stmtTruncate.executeUpdate();
        System.out.println("Tabel ranking dikosongkan.");

        // Cek data di tabel normalisasi
        String sqlCountNormalisasi = "SELECT COUNT(*) FROM normalisasi";
        PreparedStatement stmtCountNorm = conn.prepareStatement(sqlCountNormalisasi);
        ResultSet rsCountNorm = stmtCountNorm.executeQuery();
        if (rsCountNorm.next()) {
            System.out.println("Jumlah data di normalisasi: " + rsCountNorm.getInt(1));
        }

        // Tarik data dari normalisasi ke ranking
        String sqlInsertRanking = "INSERT INTO ranking ("
                                + "nik_ranking, nama_ranking, periode, bobot_ranking, "
                                + "k1_ranking, k2_ranking, k3_ranking, k4_ranking, k5_ranking) "
                                + "SELECT nik_normalisasi, nama_normalisasi, periode, bobot_normalisasi, "
                                + "k1_normalisasi, k2_normalisasi, k3_normalisasi, k4_normalisasi, k5_normalisasi "
                                + "FROM normalisasi";
        PreparedStatement stmtInsert = conn.prepareStatement(sqlInsertRanking);
        stmtInsert.executeUpdate();
        System.out.println("Data berhasil ditarik dari normalisasi ke ranking.");

        // Debug data di tabel ranking
        String sqlCountRanking = "SELECT COUNT(*) FROM ranking";
        PreparedStatement stmtCount = conn.prepareStatement(sqlCountRanking);
        ResultSet rsCount = stmtCount.executeQuery();
        if (rsCount.next()) {
            System.out.println("Jumlah data di ranking: " + rsCount.getInt(1));
        }

        //Update ranking berdasarkan bobot secara manual
        String sqlUpdateRanking = "UPDATE ranking r "
                                + "JOIN ("
                                + "    SELECT r1.nik_ranking, r1.periode, "
                                + "           (SELECT COUNT(*) + 1"
                                + "            FROM ranking r2 "
                                + "            WHERE r2.periode = r1.periode "
                                + "            AND (r2.bobot_ranking > r1.bobot_ranking "
                                + "                 OR (r2.bobot_ranking = r1.bobot_ranking "
                                + "                     AND r2.nik_ranking < r1.nik_ranking))) AS ranking "
                                + "    FROM ranking r1"
                                + ") rd ON r.nik_ranking = rd.nik_ranking AND r.periode = rd.periode "
                                + "SET r.status_ranking = rd.ranking";


        PreparedStatement stmtUpdate = conn.prepareStatement(sqlUpdateRanking);
        stmtUpdate.executeUpdate();
        System.out.println("Ranking berhasil diperbarui.");

        JOptionPane.showMessageDialog(null, "Data berhasil ditarik dan ranking diperbarui!");
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal menarik data dan menentukan ranking: " + e.getMessage());
        e.printStackTrace(); // Tambahkan ini untuk debug error
    } 

    }//GEN-LAST:event_TarikDataActionPerformed

    private void jButton11ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton11ActionPerformed
            ConnRanking();
    }//GEN-LAST:event_jButton11ActionPerformed

    private void CariPeriodePerhitunganNormalisasiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CariPeriodePerhitunganNormalisasiActionPerformed
 DefaultTableModel modelPerhitungan = new DefaultTableModel();
    DefaultTableModel modelNormalisasi = new DefaultTableModel();

    // Set kolom untuk tabel perhitungan
    modelPerhitungan.addColumn("NIK");
    modelPerhitungan.addColumn("NAMA");
    modelPerhitungan.addColumn("K1");
    modelPerhitungan.addColumn("K2");
    modelPerhitungan.addColumn("K3");
    modelPerhitungan.addColumn("K4");
    modelPerhitungan.addColumn("K5");
    modelPerhitungan.addColumn("PERIODE");

    // Set kolom untuk tabel normalisasi
    modelNormalisasi.addColumn("NIK");
    modelNormalisasi.addColumn("NAMA");
    modelNormalisasi.addColumn("K1");
    modelNormalisasi.addColumn("K2");
    modelNormalisasi.addColumn("K3");
    modelNormalisasi.addColumn("K4");
    modelNormalisasi.addColumn("K5");
    modelNormalisasi.addColumn("HASIL");
    modelNormalisasi.addColumn("PERIODE");

   try (Connection conn = db_connect.connect()) {
    // Ambil nilai bulan dan tahun dari komponen GUI
    int bulanIndex = InputBulan1.getMonth() + 1; // Jika InputBulanPenilaian adalah JMonthChooser
    int tahun = InputTahun1.getYear();          // Jika InputTahunPenilaian adalah JYearChooser

    // Daftar nama bulan
    String[] namaBulan = {
        "Januari", "Februari", "Maret", "April", "Mei", "Juni",
        "Juli", "Agustus", "September", "Oktober", "November", "Desember"
    };
    String bulan = namaBulan[bulanIndex - 1];

    // Format periode
    String periode = bulan + " " + tahun;

    // Query untuk tabel perhitungan
    String sqlPerhitungan = "SELECT * FROM perhitungan WHERE periode = ?";
    PreparedStatement stmtPerhitungan = conn.prepareStatement(sqlPerhitungan);
    stmtPerhitungan.setString(1, periode);
    ResultSet rsPerhitungan = stmtPerhitungan.executeQuery();

    // Query untuk tabel normalisasi
    String sqlNormalisasi = "SELECT * FROM normalisasi WHERE periode = ?";
    PreparedStatement stmtNormalisasi = conn.prepareStatement(sqlNormalisasi);
    stmtNormalisasi.setString(1, periode);
    ResultSet rsNormalisasi = stmtNormalisasi.executeQuery();

    // Proses hasil query ke model tabel
    while (rsPerhitungan.next()) {
        modelPerhitungan.addRow(new Object[]{
            rsPerhitungan.getInt("nik_perhitungan"),
            rsPerhitungan.getString("nama_perhitungan"),
            rsPerhitungan.getDouble("k1_perhitungan"),
            rsPerhitungan.getDouble("k2_perhitungan"),
            rsPerhitungan.getDouble("k3_perhitungan"),
            rsPerhitungan.getDouble("k4_perhitungan"),
            rsPerhitungan.getDouble("k5_perhitungan"),
            rsPerhitungan.getString("periode")
        });
    }

    while (rsNormalisasi.next()) {
        modelNormalisasi.addRow(new Object[]{
            rsNormalisasi.getInt("nik_normalisasi"),
            rsNormalisasi.getString("nama_normalisasi"),
            rsNormalisasi.getDouble("k1_normalisasi"),
            rsNormalisasi.getDouble("k2_normalisasi"),
            rsNormalisasi.getDouble("k3_normalisasi"),
            rsNormalisasi.getDouble("k4_normalisasi"),
            rsNormalisasi.getDouble("k5_normalisasi"),
            rsNormalisasi.getDouble("bobot_normalisasi"),
            rsNormalisasi.getString("periode")
        });
    }

    // Set model ke tabel
    JT_Perhitungan.setModel(modelPerhitungan);
    JT_Normalisasi.setModel(modelNormalisasi);

    JOptionPane.showMessageDialog(null, "Data berhasil ditemukan!");

} catch (SQLException e) {
    JOptionPane.showMessageDialog(null, "Gagal mencari data: " + e.getMessage());
}
    }//GEN-LAST:event_CariPeriodePerhitunganNormalisasiActionPerformed

    private void BtSimpanAkunActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtSimpanAkunActionPerformed
 String username = InputUsername.getText(); // Ambil username dari input
    char[] oldPasswordChar = InputPwLama.getPassword(); // Password lama
    char[] newPasswordChar = InputPwBaru.getPassword(); // Password baru
    char[] confirmPasswordChar = InputKPwBaru.getPassword(); // Konfirmasi password

    // Konversi password char[] ke String
    String oldPassword = new String(oldPasswordChar).trim();
    String newPassword = new String(newPasswordChar).trim();
    String confirmPassword = new String(confirmPasswordChar).trim();

    // Validasi input
    if (oldPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
        JOptionPane.showMessageDialog(null, "Semua field harus diisi!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    if (!newPassword.equals(confirmPassword)) {
        JOptionPane.showMessageDialog(null, "Password baru dan konfirmasi password tidak sama!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    try (Connection conn = db_connect.connect()) {
        // Cek password lama
        String sql = "SELECT password FROM users WHERE username = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            String currentPassword = rs.getString("password").trim(); // Password saat ini dari database

            // Debugging untuk melihat password
            System.out.println("Password lama dari database: " + currentPassword);
            System.out.println("Password lama dari input: " + oldPassword);

            // Validasi password lama
            if (!currentPassword.equals(oldPassword)) {
                JOptionPane.showMessageDialog(null, "Password lama salah!", "Peringatan", JOptionPane.WARNING_MESSAGE);
                return;
            }

            // Update password baru
            String updateSql = "UPDATE users SET password = ? WHERE username = ?";
            PreparedStatement updateStmt = conn.prepareStatement(updateSql);
            updateStmt.setString(1, newPassword); // Pastikan password baru di-hash sebelum disimpan
            updateStmt.setString(2, username);
            updateStmt.executeUpdate();

            JOptionPane.showMessageDialog(null, "Password berhasil diubah!");
        } else {
            JOptionPane.showMessageDialog(null, "Username tidak ditemukan!", "Peringatan", JOptionPane.WARNING_MESSAGE);
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Gagal mengubah password: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_BtSimpanAkunActionPerformed

    private void InputPwLamaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InputPwLamaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_InputPwLamaActionPerformed

    private void BtHapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BtHapusActionPerformed
    int selectedRow = JT_Perhitungan.getSelectedRow();

    if (selectedRow == -1) {
        JOptionPane.showMessageDialog(this, "Pilih baris yang ingin dihapus", "Peringatan", JOptionPane.WARNING_MESSAGE);
        return;
    }

    // Ambil NIK dan Periode dari baris yang dipilih
    String nikPerhitungan = (String) JT_Perhitungan.getValueAt(selectedRow, 0);  // Ambil NIK sebagai String
    String periode = (String) JT_Perhitungan.getValueAt(selectedRow, 7);          // Ambil Periode sebagai String

    // Konfirmasi penghapusan
    int confirmation = JOptionPane.showConfirmDialog(this, "Yakin ingin menghapus data dengan NIK " + nikPerhitungan + " dan Periode " + periode + "?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);

    if (confirmation == JOptionPane.YES_OPTION) {
        try (Connection conn = db_connect.connect()) {
            String sql = "DELETE FROM perhitungan WHERE nik_perhitungan = ? AND periode = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, nikPerhitungan);  // Set NIK sebagai String
            stmt.setString(2, periode);         // Set Periode sebagai String
            stmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Data berhasil dihapus!");

            tampilkanData();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal menghapus data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}

private void tampilkanData() {
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("NIK");
    model.addColumn("NAMA");
    model.addColumn("K1");
    model.addColumn("K2");
    model.addColumn("K3");
    model.addColumn("K4");
    model.addColumn("K5");
    model.addColumn("PERIODE");

    try (Connection conn = db_connect.connect()) {
        String sql = "SELECT * FROM perhitungan";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            model.addRow(new Object[] {
                rs.getString("nik_perhitungan"),  // Ambil NIK sebagai String
                rs.getString("nama_perhitungan"),
                rs.getDouble("k1_perhitungan"),
                rs.getDouble("k2_perhitungan"),
                rs.getDouble("k3_perhitungan"),
                rs.getDouble("k4_perhitungan"),
                rs.getDouble("k5_perhitungan"),
                rs.getString("periode")
            });
        }

        JT_Perhitungan.setModel(model);

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Gagal memuat data: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_BtHapusActionPerformed

    private void BT_KedisiplinanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_KedisiplinanActionPerformed
        T_Kedisiplinan T_Kedisiplinan = new T_Kedisiplinan(this);
        T_Kedisiplinan.setVisible(true);
    }//GEN-LAST:event_BT_KedisiplinanActionPerformed

    private void BT_KinerjaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_KinerjaActionPerformed
        T_Kinerja T_Kinerja = new T_Kinerja(this);
        T_Kinerja.setVisible(true);
    }//GEN-LAST:event_BT_KinerjaActionPerformed

    private void BT_KerjasamatimActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_KerjasamatimActionPerformed
        T_KerjaSamaTim T_KerjaSamaTim = new T_KerjaSamaTim(this);
        T_KerjaSamaTim.setVisible(true);
    }//GEN-LAST:event_BT_KerjasamatimActionPerformed

    private void BT_KemampuanteknisActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_KemampuanteknisActionPerformed
        T_KemampuanTeknis T_KemampuanTeknis = new T_KemampuanTeknis(this);
        T_KemampuanTeknis.setVisible(true);
    }//GEN-LAST:event_BT_KemampuanteknisActionPerformed

    private void BT_PencapaianpenjualanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_PencapaianpenjualanActionPerformed
        T_PencapaianPenjualan T_PencapaianPenjualan = new T_PencapaianPenjualan(this);
        T_PencapaianPenjualan.setVisible(true);
    }//GEN-LAST:event_BT_PencapaianpenjualanActionPerformed

    private void InputK1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_InputK1ActionPerformed

    }//GEN-LAST:event_InputK1ActionPerformed


    
    
   
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Home.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
            Connection conn = db_connect.connect();
    if (conn != null) {
        System.out.println("Koneksi berhasil!");
    } else {
        System.out.println("Koneksi gagal!");
    }

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Home().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BT_CariKaryawan;
    private javax.swing.JButton BT_Divisi;
    private javax.swing.JButton BT_Jabatan;
    private javax.swing.JButton BT_Karyawan;
    private javax.swing.JButton BT_Kedisiplinan;
    private javax.swing.JButton BT_Kemampuanteknis;
    private javax.swing.JButton BT_Kerjasamatim;
    private javax.swing.JButton BT_Kinerja;
    private javax.swing.JButton BT_Kriteriia;
    private javax.swing.JButton BT_Pencapaianpenjualan;
    private javax.swing.JToggleButton BT_Refresh;
    private javax.swing.JToggleButton BT_Refresh1;
    private javax.swing.JButton BT_RefreshKriteria;
    private javax.swing.JButton BU_Divisi;
    private javax.swing.JButton BU_Jabatan;
    private javax.swing.JButton BU_Karyawan;
    private javax.swing.JButton BU_Kriteria;
    private javax.swing.JButton B_Jabatan;
    private javax.swing.JButton B_Karyawan;
    private javax.swing.JButton B_Keluar;
    private javax.swing.JButton B_Kriteria;
    private javax.swing.JButton B_Pengaturan;
    private javax.swing.JButton B_Penilaian;
    private javax.swing.JButton B_Perhitungan;
    private javax.swing.JButton B_Ranking;
    private javax.swing.JButton BtHapus;
    private javax.swing.JButton BtSimpanAkun;
    private javax.swing.JButton BtSimpanPenilaian;
    private javax.swing.JButton CariPeriodePerhitunganNormalisasi;
    private javax.swing.JButton CariRanking;
    private javax.swing.JComboBox<String> CmbNikKaryawan;
    private com.toedter.calendar.JMonthChooser InputBulan1;
    private com.toedter.calendar.JMonthChooser InputBulan2;
    private com.toedter.calendar.JMonthChooser InputBulanPenilaian;
    private javax.swing.JTextField InputCariDivisiJabatan;
    private javax.swing.JTextField InputCariKaryawan;
    private javax.swing.JTextField InputDivisiKaryawan;
    private javax.swing.JTextField InputJabatanKaryawan;
    private javax.swing.JTextField InputK1;
    private javax.swing.JTextField InputK2;
    private javax.swing.JTextField InputK3;
    private javax.swing.JTextField InputK4;
    private javax.swing.JTextField InputK5;
    private javax.swing.JPasswordField InputKPwBaru;
    private javax.swing.JTextField InputNamaKaryawan;
    private javax.swing.JPasswordField InputPwBaru;
    private javax.swing.JPasswordField InputPwLama;
    private com.toedter.calendar.JYearChooser InputTahun1;
    private com.toedter.calendar.JYearChooser InputTahun2;
    private com.toedter.calendar.JYearChooser InputTahunPenilaian;
    private javax.swing.JTextField InputTglKaryawan;
    private javax.swing.JTextField InputUsername;
    private javax.swing.JTable JT_Divisi;
    private javax.swing.JTable JT_Jabatan;
    private javax.swing.JTable JT_Karayawan;
    private javax.swing.JTable JT_Kriteria;
    private javax.swing.JTable JT_Normalisasi;
    private javax.swing.JTable JT_Perhitungan;
    private javax.swing.JTable JT_Ranking;
    private javax.swing.JLabel LabelK1;
    private javax.swing.JLabel LabelK2;
    private javax.swing.JLabel LabelK3;
    private javax.swing.JLabel LabelK4;
    private javax.swing.JLabel LabelK5;
    private javax.swing.JButton NORMALISASI;
    private javax.swing.JPanel P_Awal;
    private javax.swing.JPanel P_Home;
    private javax.swing.JPanel P_Isi;
    private javax.swing.JPanel P_Jabatan;
    private javax.swing.JPanel P_Karyawan;
    private javax.swing.JPanel P_Kriteria;
    private javax.swing.JPanel P_Menu;
    private javax.swing.JPanel P_Pengaturan;
    private javax.swing.JPanel P_Penilaian;
    private javax.swing.JPanel P_Perhitungan;
    private javax.swing.JPanel P_Ranking;
    private javax.swing.JButton PrintRanking;
    private javax.swing.JButton Print_Karyawan;
    private javax.swing.JButton RefereshPerhitungan;
    private javax.swing.JButton TarikData;
    private javax.swing.JButton jButton11;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTextField jTextField3;
    // End of variables declaration//GEN-END:variables

}
