package com.ra.View.login;

import javax.swing.JFrame;

public class LoginScreen extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(LoginScreen.class.getName());

    public LoginScreen(JFrame jFrame, boolean par) {
        initComponents();
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlLogin = new javax.swing.JPanel();
        lbTitle = new javax.swing.JLabel();
        lbPassword = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        lbUsername = new javax.swing.JLabel();
        txtPassword = new javax.swing.JPasswordField();
        btnLogin = new javax.swing.JButton();
        chkShowPassword = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("LOGINSCREEN");
        setPreferredSize(new java.awt.Dimension(600, 400));

        pnlLogin.setBackground(new java.awt.Color(255, 255, 255));
        pnlLogin.setPreferredSize(new java.awt.Dimension(600, 400));

        lbTitle.setFont(new java.awt.Font("Yu Mincho", 1, 24)); // NOI18N
        lbTitle.setForeground(new java.awt.Color(0, 51, 102));
        lbTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTitle.setText("勤怠管理システム");
        lbTitle.setMaximumSize(new java.awt.Dimension(200, 40));

        lbPassword.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbPassword.setText("パスワード");

        txtUsername.setText("jTextField1");
        txtUsername.addActionListener(this::txtUsernameActionPerformed);

        lbUsername.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbUsername.setText("ユーザー名");

        txtPassword.setText("jPasswordField1");

        btnLogin.setBackground(new java.awt.Color(222, 228, 232));
        btnLogin.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        btnLogin.setText("ログイン");
        btnLogin.addActionListener(this::btnLoginActionPerformed);

        chkShowPassword.setFont(new java.awt.Font("Yu Mincho", 0, 14)); // NOI18N
        chkShowPassword.setText("パスワード表示");
        chkShowPassword.addActionListener(this::chkShowPasswordActionPerformed);

        javax.swing.GroupLayout pnlLoginLayout = new javax.swing.GroupLayout(pnlLogin);
        pnlLogin.setLayout(pnlLoginLayout);
        pnlLoginLayout.setHorizontalGroup(
            pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoginLayout.createSequentialGroup()
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlLoginLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlLoginLayout.createSequentialGroup()
                                .addGap(118, 118, 118)
                                .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlLoginLayout.createSequentialGroup()
                                .addComponent(lbUsername)
                                .addGap(18, 18, 18)
                                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlLoginLayout.createSequentialGroup()
                                .addComponent(lbPassword)
                                .addGap(18, 18, 18)
                                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLoginLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(chkShowPassword)))
                .addContainerGap(91, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLoginLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(btnLogin)
                .addGap(253, 253, 253))
        );
        pnlLoginLayout.setVerticalGroup(
            pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlLoginLayout.createSequentialGroup()
                .addGap(72, 72, 72)
                .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbUsername)
                    .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbPassword)
                    .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(chkShowPassword)
                .addGap(18, 18, 18)
                .addComponent(btnLogin)
                .addContainerGap(97, Short.MAX_VALUE))
        );

        lbTitle.getAccessibleContext().setAccessibleName("Login");
        lbTitle.getAccessibleContext().setAccessibleDescription("Tiêu đề “勤怠管理システム”");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlLogin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pnlLogin.getAccessibleContext().setAccessibleName("");

        getAccessibleContext().setAccessibleName("LOGIN");
        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsernameActionPerformed

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLoginActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnLoginActionPerformed

    private void chkShowPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkShowPasswordActionPerformed
        // Lấy trạng thái hiện tại của checkbox
    if (chkShowPassword.isSelected()) {
        // Nếu đã chọn (Checked): Hiển thị mật khẩu rõ
        // Bằng cách đặt Echo Char thành 0 (null/không có)
        txtPassword.setEchoChar((char) 0); 
    } else {
        // Nếu không chọn (Unchecked): Ẩn mật khẩu (hiển thị dấu sao)
        // Bằng cách đặt Echo Char thành ký tự mặc định của JPasswordField (*)
        txtPassword.setEchoChar('*'); 
    }
    }//GEN-LAST:event_chkShowPasswordActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                LoginScreen Jframe = new LoginScreen(new javax.swing.JFrame(), true);
                Jframe.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                Jframe.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLogin;
    private javax.swing.JCheckBox chkShowPassword;
    private javax.swing.JLabel lbPassword;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lbUsername;
    private javax.swing.JPanel pnlLogin;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
