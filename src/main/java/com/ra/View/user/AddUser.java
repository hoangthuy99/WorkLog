/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.ra.View.user;

import com.ra.Controller.UserController;
import com.ra.DAO.Auth.AuthDAO;
import com.ra.DAO.Department.DepartmentDAO;
import com.ra.DAO.Task.TaskDAO;

import com.ra.DTO.request.UserRequest;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Roles;
import com.ra.Model.Entity.Tasks;
import com.ra.Model.Entity.Users;
import com.ra.Sercurity.PasswordHash;
import com.ra.View.menu.AllMenu;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Admin
 */
public class AddUser extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AddUser.class.getName());

    /**
     * Creates new form AddUser
     */
    private DefaultListModel<String> taskModel = new DefaultListModel<>();

    public AddUser() {
        initComponents();
        listTask.setModel(taskModel);
        listTask.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        // --- Thêm đoạn MouseListener để click thường chọn được nhiều ---
        listTask.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int index = listTask.locationToIndex(evt.getPoint());
                if (index >= 0) {
                    if (listTask.isSelectedIndex(index)) {
                        listTask.removeSelectionInterval(index, index);
                    } else {
                        listTask.addSelectionInterval(index, index);
                    }
                }
            }
        });
        loadData();
    }

    private void loadData() {
        loadDepartments();
        loadTasks();
        loadRoles();
    }
    private void loadDepartments() {
        try {
            DepartmentDAO departmentDAO = new DepartmentDAO();
            List<Department> departments = departmentDAO.findAll();

            cbDepartment.removeAllItems();
            for (Department d : departments) {
                cbDepartment.addItem(d.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadTasks() {
        try {
            TaskDAO taskDAO = new TaskDAO();
            List<Tasks> tasks = taskDAO.findAll();

            // lấy model hiện tại của listTask
            DefaultListModel<String> model = (DefaultListModel<String>) listTask.getModel();
            model.clear(); // xoá toàn bộ item cũ

            // add dữ liệu vào list
            for (Tasks t : tasks) {
                model.addElement(t.getName());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void loadRoles() {
        try{
            AuthDAO authDAO = new AuthDAO();
            List<Roles> roles = authDAO.findAllRoles();
            cbRole.removeAllItems();
            for (Roles r : roles) {
                cbRole.addItem(r.getName());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean validateForm() {
        String email = txtMail.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassWord.getPassword()).trim();
        String fullName = txtEmployeename.getText().trim();

        // Email 空チェック
        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "メールアドレスを入力してください。",
                    "警告",
                    JOptionPane.WARNING_MESSAGE
            );
            txtMail.requestFocus();
            return false;
        }

        // Email 形式チェック
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(
                    this,
                    "メールアドレスの形式が正しくありません。",
                    "警告",
                    JOptionPane.WARNING_MESSAGE
            );
            txtMail.requestFocus();
            return false;
        }

        // Username 空チェック
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "ユーザー名を入力してください。",
                    "警告",
                    JOptionPane.WARNING_MESSAGE
            );
            txtUsername.requestFocus();
            return false;
        }

        // Password 空チェック
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "パスワードを入力してください。",
                    "警告",
                    JOptionPane.WARNING_MESSAGE
            );
            txtPassWord.requestFocus();
            return false;
        }

        // Full name 空チェック
        if (fullName.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "氏名を入力してください。",
                    "警告",
                    JOptionPane.WARNING_MESSAGE
            );
            txtEmployeename.requestFocus();
            return false;
        }

        // 部署選択チェック
        if (cbDepartment.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "部署を選択してください。",
                    "警告",
                    JOptionPane.WARNING_MESSAGE
            );
            return false;
        }

        // 役割選択チェック
        if (cbRole.getSelectedIndex() == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "役割を選択してください。",
                    "警告",
                    JOptionPane.WARNING_MESSAGE
            );
            return false;
        }



        return true;
    }



    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem2 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem3 = new javax.swing.JCheckBoxMenuItem();
        pnlAdduser = new javax.swing.JPanel();
        lbMail = new javax.swing.JLabel();
        txtMail = new javax.swing.JTextField();
        lbUsername = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        lbPassword = new javax.swing.JLabel();
        lbEmployeename = new javax.swing.JLabel();
        txtEmployeename = new javax.swing.JTextField();
        lbDepartment = new javax.swing.JLabel();
        lbTask = new javax.swing.JLabel();
        lbRole = new javax.swing.JLabel();
        cbRole = new javax.swing.JComboBox<>();
        btnCancel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        cbDepartment = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        listTask = new javax.swing.JList<>();
        txtPassWord = new javax.swing.JPasswordField();



        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("新ユーザー作成");
        setPreferredSize(new java.awt.Dimension(600, 400));

        pnlAdduser.setBackground(new java.awt.Color(255, 255, 255));
        pnlAdduser.setPreferredSize(new java.awt.Dimension(600, 400));

        lbMail.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbMail.setText("メール");

        txtMail.addActionListener(this::txtMailActionPerformed);

        lbUsername.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbUsername.setText("ユーザー名");

        txtUsername.addActionListener(this::txtUsernameActionPerformed);

        lbPassword.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbPassword.setText("パスワード");

        lbEmployeename.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbEmployeename.setText("社員名");

        txtEmployeename.addActionListener(this::txtEmployeenameActionPerformed);

        lbDepartment.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbDepartment.setText("部署");

        lbTask.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbTask.setText("タスク");

        lbRole.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbRole.setText("ロール");

        cbRole.setModel(new javax.swing.DefaultComboBoxModel<>());


        btnCancel.setBackground(new java.awt.Color(255, 204, 153));
        btnCancel.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        btnCancel.setText("キャンセル");
        btnCancel.addActionListener(this::btnCancelActionPerformed);

        btnSave.setBackground(new java.awt.Color(189, 231, 189));
        btnSave.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        btnSave.setText("保存");
        btnSave.addActionListener(this::btnSaveActionPerformed);

        jScrollPane1.setViewportView(listTask);

        javax.swing.GroupLayout pnlAdduserLayout = new javax.swing.GroupLayout(pnlAdduser);
        pnlAdduser.setLayout(pnlAdduserLayout);
        pnlAdduserLayout.setHorizontalGroup(
            pnlAdduserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAdduserLayout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(pnlAdduserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbUsername)
                    .addComponent(lbPassword)
                    .addGroup(pnlAdduserLayout.createSequentialGroup()
                        .addComponent(lbMail)
                        .addGap(16, 16, 16))
                    .addComponent(lbRole, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlAdduserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtUsername, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMail, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbRole, 0, 140, Short.MAX_VALUE)
                    .addComponent(txtPassWord))
                .addGap(52, 52, 52)
                .addGroup(pnlAdduserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAdduserLayout.createSequentialGroup()
                        .addComponent(lbEmployeename)
                        .addGap(18, 18, 18)
                        .addComponent(txtEmployeename, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(pnlAdduserLayout.createSequentialGroup()
                        .addGroup(pnlAdduserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbTask)
                            .addComponent(lbDepartment))
                        .addGap(18, 18, 18)
                        .addGroup(pnlAdduserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                            .addComponent(cbDepartment, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(0, 32, Short.MAX_VALUE))
            .addGroup(pnlAdduserLayout.createSequentialGroup()
                .addGap(188, 188, 188)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(btnSave)
                .addGap(20, 20, 20))
        );
        pnlAdduserLayout.setVerticalGroup(
            pnlAdduserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlAdduserLayout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(pnlAdduserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbMail)
                    .addComponent(txtMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbEmployeename)
                    .addComponent(txtEmployeename, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(pnlAdduserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAdduserLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(pnlAdduserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbUsername)))
                    .addGroup(pnlAdduserLayout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(pnlAdduserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbDepartment))))
                .addGap(26, 26, 26)
                .addGroup(pnlAdduserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlAdduserLayout.createSequentialGroup()
                        .addGroup(pnlAdduserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbPassword)
                            .addComponent(lbTask)
                            .addComponent(txtPassWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 30, Short.MAX_VALUE)
                        .addGroup(pnlAdduserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbRole)
                            .addComponent(cbRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE))
                    .addGroup(pnlAdduserLayout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGroup(pnlAdduserLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnSave))
                .addGap(74, 74, 74))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlAdduser, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlAdduser, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        getAccessibleContext().setAccessibleDescription("Add User");

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private UserController userController = new UserController();


    private void txtMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMailActionPerformed

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtUsernameActionPerformed

    private void txtEmployeenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmployeenameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtEmployeenameActionPerformed

    private void cbRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRoleActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbRoleActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        this.dispose();
    }
//GEN-LAST:event_btnCancelActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {

        if (!validateForm()) {
            return;
        }

        String userName = txtUsername.getText();
        String password = new String(txtPassWord.getPassword());
        String roleName = cbRole.getSelectedItem().toString();
        String email = txtMail.getText();
        String fullName = txtEmployeename.getText();
        String departmentName = cbDepartment.getSelectedItem().toString();

        try {

            Users user = new Users();
            user.setUserName(userName);

            // Hash password
            user.setPassword(password);
            user.setFullName(fullName);
            user.setEmail(email);
            user.setUserCode(Users.generateUserCode());


            // ----- Department -----
            DepartmentDAO departmentDAO = new DepartmentDAO();
            Department dept = departmentDAO.findFindByName(departmentName).orElse(null);
            if (dept != null) {
                user.setDepartment(dept);
            }

            // ----- Role -----
            AuthDAO roleDAO = new AuthDAO();
            Optional<Roles> roleOpt = roleDAO.findByName(roleName);
            if (roleOpt.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Role không tồn tại!");
                return;
            }
            user.setRole(roleOpt.get());

            // ----- Tasks -----
            TaskDAO taskDAO = new TaskDAO();
            List<String> selected = listTask.getSelectedValuesList();
            List<Tasks> newTasks = new ArrayList<>();

            for (String t : selected) {
                taskDAO.findByName(t).ifPresent(newTasks::add);
            }

            user.setTasks(newTasks);   // <-- FIXED

            // ----- Save -----
            userController.createUser(user);

            JOptionPane.showMessageDialog(this, "ユーザーが正常に作成されました！");

            this.dispose();

            AllUser all = new AllUser();
            all.setVisible(true);
            all.setLocationRelativeTo(null);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }
//GEN-LAST:event_btnSaveActionPerformed
   

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
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new AddUser().setVisible(true));
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cbDepartment;
    private javax.swing.JComboBox<String> cbRole;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem2;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbDepartment;
    private javax.swing.JLabel lbEmployeename;
    private javax.swing.JLabel lbMail;
    private javax.swing.JLabel lbPassword;
    private javax.swing.JLabel lbRole;
    private javax.swing.JLabel lbTask;
    private javax.swing.JLabel lbUsername;
    private javax.swing.JList<String> listTask;
    private javax.swing.JPanel pnlAdduser;
    private javax.swing.JTextField txtEmployeename;
    private javax.swing.JTextField txtMail;
    private javax.swing.JPasswordField txtPassWord;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}
