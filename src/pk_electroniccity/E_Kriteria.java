/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package pk_electroniccity;

import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import pk_electroniccity.db_connect;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import javax.swing.JOptionPane;


/**
 *
 * @author lenovo
 */
public class E_Kriteria extends javax.swing.JFrame {

    /**
     * Creates new form E_Kriteria
     */
    public E_Kriteria() {
        initComponents();
                //set beahvior saat form ditutup
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    private String kriteria; // Variabel untuk menyimpan nama_kriteria lama

    // Method Setter untuk menerima nama_kriteria lama dari form lain
    public void setKriteria(String kode, String kriteria, String bobot) {
    InputKode.setText(kode);
    InputKriteria.setText(kriteria);
    InputBobot.setText(bobot);
}
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        InputKriteria = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        InputBobot = new javax.swing.JTextField();
        JLabel = new javax.swing.JLabel();
        InputKode = new javax.swing.JTextField();
        BT_Update = new javax.swing.JButton();
        BT_Hapus = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createMatteBorder(2, 2, 2, 2, new java.awt.Color(0, 114, 198)));

        jLabel3.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel3.setText("KRITERIA");

        jLabel4.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jLabel4.setText("BOBOT");

        JLabel.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        JLabel.setText("KODE");

        BT_Update.setText("UPDATE");
        BT_Update.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_UpdateActionPerformed(evt);
            }
        });

        BT_Hapus.setText("HAPUS");
        BT_Hapus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BT_HapusActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(BT_Hapus, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(JLabel)
                            .addComponent(jLabel4))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(InputKode, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(InputKriteria, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(26, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(BT_Update, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(InputBobot, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 0, Short.MAX_VALUE))))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(47, 47, 47)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(JLabel)
                    .addComponent(InputKode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(InputKriteria, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(InputBobot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(51, 51, 51)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(BT_Hapus)
                    .addComponent(BT_Update))
                .addContainerGap(72, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void BT_UpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_UpdateActionPerformed
    try (Connection conn = db_connect.connect()) {
        // Query untuk update data
        String sql = "UPDATE kriteria SET nama_kriteria = ?, bobot_kriteria = ? WHERE kode_kriteria = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);

        // Mengisi parameter query
        stmt.setString(3, InputKode.getText());
        stmt.setString(1,InputKriteria.getText());
        stmt.setString(2,InputBobot.getText());
      

        // Eksekusi query
        int rowsUpdated = stmt.executeUpdate();
        if (rowsUpdated > 0) {
            JOptionPane.showMessageDialog(null, "Data berhasil diperbarui!");
        } else {
            JOptionPane.showMessageDialog(null, "Gagal memperbarui data.");
        }

        // Tutup form setelah update
        this.dispose();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
    }
    }//GEN-LAST:event_BT_UpdateActionPerformed

    private void BT_HapusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BT_HapusActionPerformed
    int confirm = JOptionPane.showConfirmDialog(null, "Apakah Anda yakin ingin menghapus data ini?", "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
        try (Connection conn = db_connect.connect()) {
            // Query untuk hapus data
            String sql = "DELETE FROM kriteria WHERE kode_kriteria = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            // Mengisi parameter query
            stmt.setString(1, InputKode.getText());

            // Eksekusi query
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                JOptionPane.showMessageDialog(null, "Data berhasil dihapus!");
            } else {
                JOptionPane.showMessageDialog(null, "Gagal menghapus data.");
            }

            // Tutup form setelah hapus
            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Terjadi kesalahan: " + e.getMessage());
        }
    }

    }//GEN-LAST:event_BT_HapusActionPerformed

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
            java.util.logging.Logger.getLogger(E_Kriteria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(E_Kriteria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(E_Kriteria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(E_Kriteria.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new E_Kriteria().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton BT_Hapus;
    private javax.swing.JButton BT_Update;
    private javax.swing.JTextField InputBobot;
    private javax.swing.JTextField InputKode;
    private javax.swing.JTextField InputKriteria;
    private javax.swing.JLabel JLabel;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
