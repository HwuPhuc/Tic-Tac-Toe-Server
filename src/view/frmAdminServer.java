package view;

import caroserver.CaroServer;
import caroserver.ClientHandler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.WindowConstants;

public class frmAdminServer extends javax.swing.JFrame implements Runnable{
    
    DefaultListModel<String> serverActivity;
    DefaultListModel<String> playerOnline;
    
    public frmAdminServer() {
        initComponents();
        serverActivity = new DefaultListModel<>();
        playerOnline = new DefaultListModel<>();
        btnCloseServer.setEnabled(false);
        btnCloseServer.setVisible(false);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listServerActivity = new javax.swing.JList<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        listOnlinePlayer = new javax.swing.JList<>();
        jLabel2 = new javax.swing.JLabel();
        lblPlayerOnline = new javax.swing.JLabel();
        btnListOnlinePlayer = new javax.swing.JButton();
        btnCloseServer = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Server Cờ Caro");

        jLabel1.setText("Hoạt động của Server");

        listServerActivity.setFont(new java.awt.Font("Humanst521 Lt BT", 0, 18)); // NOI18N
        listServerActivity.setForeground(new java.awt.Color(51, 204, 0));
        jScrollPane1.setViewportView(listServerActivity);

        jScrollPane2.setViewportView(listOnlinePlayer);

        jLabel2.setText("Danh sách người chơi đang online");

        lblPlayerOnline.setText("Số người đang online: ");

        btnListOnlinePlayer.setText("Xem số người online");
        btnListOnlinePlayer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnListOnlinePlayerActionPerformed(evt);
            }
        });

        btnCloseServer.setText("Đóng Server");
        btnCloseServer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseServerActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jLabel1)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 639, Short.MAX_VALUE)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(121, 121, 121)
                        .addComponent(lblPlayerOnline)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(71, 71, 71)
                .addComponent(btnListOnlinePlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 231, Short.MAX_VALUE)
                .addComponent(btnCloseServer, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(82, 82, 82))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(41, 41, 41)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblPlayerOnline))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 176, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnListOnlinePlayer, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCloseServer, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(107, 107, 107))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnListOnlinePlayerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnListOnlinePlayerActionPerformed
        playerOnline.clear();
        for(ClientHandler ch : CaroServer.clients){
            playerOnline.addElement(ch.getUser().getUserName());
        }
        listOnlinePlayer.setModel(playerOnline);
    }//GEN-LAST:event_btnListOnlinePlayerActionPerformed

    private void btnCloseServerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseServerActionPerformed
        for(ClientHandler ch : CaroServer.clients){
            try {
                ch.write("server closed");
                CaroServer.clients.remove(ch);
            } catch (IOException ex) {
                Logger.getLogger(frmAdminServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        try {
            CaroServer.serverSocket.close();
        } catch (IOException ex) {
            Logger.getLogger(frmAdminServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.dispose();
    }//GEN-LAST:event_btnCloseServerActionPerformed

    public void playerOnline(int x){
        lblPlayerOnline.setText("Số người chơi online: " + x);
    }
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
            java.util.logging.Logger.getLogger(frmAdminServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmAdminServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmAdminServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmAdminServer.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmAdminServer().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCloseServer;
    private javax.swing.JButton btnListOnlinePlayer;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblPlayerOnline;
    private javax.swing.JList<String> listOnlinePlayer;
    private javax.swing.JList<String> listServerActivity;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {
        this.setVisible(true);
        String firstMessage = "Server is opened, welcome everyone!";
        serverActivity.addElement(firstMessage);
        listServerActivity.setModel(serverActivity);
    }
    public void aClientJoined(String userName){
        serverActivity.addElement("Người chơi có Username " + userName + " vừa vào server!");
        listServerActivity.setModel(serverActivity);
    }
    public void aClientLeft(String userName){
        serverActivity.addElement("Người chơi có Username " + userName + " vừa rời server!");
        listServerActivity.setModel(serverActivity);
    }
}
