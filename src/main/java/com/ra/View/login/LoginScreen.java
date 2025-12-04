package com.ra.View.login;

import com.ra.Controller.UserController;
import com.ra.Model.Entity.Users;
import com.ra.Sercurity.PasswordHash;
import com.ra.Sercurity.PermissionUtil;
import com.ra.View.dashboard.MainDashboard;

import javax.swing.*;
import java.util.List;
import java.util.Optional;
import com.ra.Sercurity.SessionLocal;


public class LoginScreen extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(LoginScreen.class.getName());

    private final UserController userController = new UserController();

    public LoginScreen(JFrame jFrame, boolean par) {
        initComponents();
        setLocationRelativeTo(null); // căn giữa màn hình
    }

    public LoginScreen() {
        initComponents();
        setLocationRelativeTo(null);
    }




    @SuppressWarnings("unchecked")
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

        lbTitle.setFont(new java.awt.Font("Yu Mincho", 1, 24));
        lbTitle.setForeground(new java.awt.Color(0, 51, 102));
        lbTitle.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        lbTitle.setText("勤怠管理システム");

        lbPassword.setFont(new java.awt.Font("Yu Mincho", 1, 14));
        lbPassword.setText("パスワード");

        lbUsername.setFont(new java.awt.Font("Yu Mincho", 1, 14));
        lbUsername.setText("ユーザー名");

        btnLogin.setBackground(new java.awt.Color(222, 228, 232));
        btnLogin.setFont(new java.awt.Font("Yu Mincho", 1, 14));
        btnLogin.setText("ログイン");
        btnLogin.addActionListener(this::btnLoginActionPerformed);

        chkShowPassword.setText("パスワード表示");
        chkShowPassword.addActionListener(this::chkShowPasswordActionPerformed);

        javax.swing.GroupLayout pnlLoginLayout = new javax.swing.GroupLayout(pnlLogin);
        pnlLogin.setLayout(pnlLoginLayout);

        pnlLoginLayout.setHorizontalGroup(
                pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlLoginLayout.createSequentialGroup()
                                .addGap(70)
                                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlLoginLayout.createSequentialGroup()
                                                .addGap(118)
                                                .addComponent(lbTitle, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(pnlLoginLayout.createSequentialGroup()
                                                .addComponent(lbUsername)
                                                .addGap(18)
                                                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(pnlLoginLayout.createSequentialGroup()
                                                .addComponent(lbPassword)
                                                .addGap(18)
                                                .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 347, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLoginLayout.createSequentialGroup()
                                                .addComponent(chkShowPassword)
                                                .addGap(5))))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlLoginLayout.createSequentialGroup()
                                .addGap(0, 0, Short.MAX_VALUE)
                                .addComponent(btnLogin)
                                .addGap(253))
        );

        pnlLoginLayout.setVerticalGroup(
                pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlLoginLayout.createSequentialGroup()
                                .addGap(72)
                                .addComponent(lbTitle)
                                .addGap(27)
                                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbUsername)
                                        .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(26)
                                .addGroup(pnlLoginLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbPassword)
                                        .addComponent(txtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(chkShowPassword)
                                .addGap(18)
                                .addComponent(btnLogin)
                                .addContainerGap(97, Short.MAX_VALUE))
        );

        getContentPane().add(pnlLogin);
        pack();
    }

    private void chkShowPasswordActionPerformed(java.awt.event.ActionEvent evt) {
        if (chkShowPassword.isSelected()) {
            txtPassword.setEchoChar((char) 0); // Show
        } else {
            txtPassword.setEchoChar('*'); // Hide
        }
    }

    // ======================== LOGIN LOGIC ======================== //

    private void btnLoginActionPerformed(java.awt.event.ActionEvent evt) {

        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ユーザー名とパスワードを入力してください。");
            return;
        }

        Optional<Users> userOpt = userController.findByUsername(username);
        logger.info("login with username " + username);
        if (userOpt.isEmpty()) {
            logger.info("Không tìm thấy username : " + username);
            JOptionPane.showMessageDialog(this, "ユーザーが存在しません。");
            return;
        }

        Users user = userOpt.get();

        // Debug BCrypt
        System.out.println("Input password = " + password);
        System.out.println("DB hash        = " + user.getPassword());
        System.out.println("Match?         = " +
                PasswordHash.verifyPassword(password, user.getPassword()));
        // SO SÁNH DẠNG BCRYPT
        if (!PasswordHash.verifyPassword(password, user.getPassword())) {
            JOptionPane.showMessageDialog(this, "パスワードが間違っています。");
            return;
        }
        System.out.println("User found: " + user.getUserName());
        System.out.println("User role: " + user.getRole().getName());
        System.out.println("---- DEBUG USER ENTITY ----");
        System.out.println("ID: " + user.getId());
        System.out.println("Username: " + user.getUserName());
        System.out.println("Password: " + user.getPassword());
        System.out.println("RoleId: " + user.getRole().getId());

        System.out.println("Role object: " + user.getRole());
        if (user.getRole() != null)
            System.out.println("Role name: " + user.getRole().getName());

// Lưu role của user
        SessionLocal.set("USER_ROLE", user.getRole().getName());

// Lưu danh sách permission của role này
        if (user.getRole().getPermissions() != null) {
            List<String> permCodes = user.getRole()
                    .getPermissions()
                    .stream()
                    .map(p -> p.getCode())   // lấy code: USER_MANAGE, TASK_MANAGE ...
                    .toList();

            SessionLocal.set("USER_PERMISSIONS", permCodes);
        }
        PermissionUtil.setUser(user);

        new MainDashboard(user).setVisible(true);
        this.dispose();
        // Lưu role của user
        SessionLocal.set("USER_ROLE", user.getRole().getName());



    }



    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            LoginScreen Jframe = new LoginScreen(new javax.swing.JFrame(), true);
            Jframe.setVisible(true);
        });
    }

    private javax.swing.JButton btnLogin;
    private javax.swing.JCheckBox chkShowPassword;
    private javax.swing.JLabel lbPassword;
    private javax.swing.JLabel lbTitle;
    private javax.swing.JLabel lbUsername;
    private javax.swing.JPanel pnlLogin;
    private javax.swing.JPasswordField txtPassword;
    private javax.swing.JTextField txtUsername;
}