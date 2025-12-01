package com.ra.View.menu;

import com.ra.Controller.PermissionController;
import com.ra.Controller.RoleController;
import com.ra.Model.Entity.Permission;
import com.ra.Model.Entity.Roles;

import javax.swing.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddMenu extends javax.swing.JFrame {

    private final PermissionController permissionController = new PermissionController();
    private final RoleController roleController = new RoleController();

    // permission đang sửa (null nếu là tạo mới)
    private Permission editingPermission = null;

    public AddMenu() {
        initComponents();
    }

    // Constructor EDIT Mode
    public AddMenu(Permission permission) {
        this.editingPermission = permission;
        initComponents();
        loadEditData();
        setTitle("メニュー編集");
        btnSave.setText("更新");
    }

    private void loadEditData() {
        if (editingPermission == null) return;

        txtMenucode.setText(editingPermission.getCode());
        txtMenuname.setText(editingPermission.getName());

        if (editingPermission.getRoles() != null) {
            for (Roles r : editingPermission.getRoles()) {
                switch (r.getName()) {
                    case "EMPLOYEE" -> chkEmployee.setSelected(true);
                    case "MANAGER" -> chkManager.setSelected(true);
                    case "ADMIN" -> chkAdmin.setSelected(true);
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        pnlAddMenu = new javax.swing.JPanel();
        lbMenucode = new javax.swing.JLabel();
        txtMenucode = new javax.swing.JTextField();
        lbMenuname = new javax.swing.JLabel();
        txtMenuname = new javax.swing.JTextField();
        lbRole = new javax.swing.JLabel();
        chkEmployee = new javax.swing.JCheckBox();
        chkManager = new javax.swing.JCheckBox();
        chkAdmin = new javax.swing.JCheckBox();
        btnCancel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("新メニュー作成");

        pnlAddMenu.setBackground(new java.awt.Color(255, 255, 255));

        lbMenucode.setText("メニューコード");
        lbMenuname.setText("メニュー名");
        lbRole.setText("ロール");

        chkEmployee.setText("EMPLOYEE");
        chkManager.setText("MANAGER");
        chkAdmin.setText("ADMIN");

        btnCancel.setText("キャンセル");
        btnCancel.addActionListener(e -> this.dispose());

        btnSave.setText("保存");
        btnSave.addActionListener(this::btnSaveActionPerformed);

        javax.swing.GroupLayout pnlLayout = new javax.swing.GroupLayout(pnlAddMenu);
        pnlAddMenu.setLayout(pnlLayout);
        pnlLayout.setHorizontalGroup(
                pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlLayout.createSequentialGroup()
                                .addGap(40)
                                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lbMenucode)
                                        .addComponent(lbMenuname)
                                        .addComponent(lbRole))
                                .addGap(20)
                                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlLayout.createSequentialGroup()
                                                .addComponent(chkEmployee)
                                                .addGap(10)
                                                .addComponent(chkManager)
                                                .addGap(10)
                                                .addComponent(chkAdmin))
                                        .addComponent(txtMenucode, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtMenuname, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(pnlLayout.createSequentialGroup()
                                                .addComponent(btnCancel)
                                                .addGap(20)
                                                .addComponent(btnSave)))
                                .addContainerGap(40, Short.MAX_VALUE))
        );

        pnlLayout.setVerticalGroup(
                pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlLayout.createSequentialGroup()
                                .addGap(40)
                                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbMenucode)
                                        .addComponent(txtMenucode, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(15)
                                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbMenuname)
                                        .addComponent(txtMenuname, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20)
                                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbRole)
                                        .addComponent(chkEmployee)
                                        .addComponent(chkManager)
                                        .addComponent(chkAdmin))
                                .addGap(40)
                                .addGroup(pnlLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCancel)
                                        .addComponent(btnSave))
                                .addGap(40))
        );

        getContentPane().add(pnlAddMenu);
        pack();
        setLocationRelativeTo(null);
    }


    // ======================================================
    // SAVE HANDLER — CREATE + UPDATE
    // ======================================================
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {

        String code = txtMenucode.getText().trim();
        String name = txtMenuname.getText().trim();

        if (code.isEmpty() || name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "コードと名前は必須です！");
            return;
        }

        // Lấy roles được chọn
        List<Roles> selectedRoles = new ArrayList<>();

        if (chkEmployee.isSelected()) {
            Optional<Roles> r = roleController.findByName("EMPLOYEE");
            r.ifPresent(selectedRoles::add);
        }
        if (chkManager.isSelected()) {
            Optional<Roles> r = roleController.findByName("MANAGER");
            r.ifPresent(selectedRoles::add);
        }
        if (chkAdmin.isSelected()) {
            Optional<Roles> r = roleController.findByName("ADMIN");
            r.ifPresent(selectedRoles::add);
        }


        // ========== CREATE ==========
        if (editingPermission == null) {

            Permission p = new Permission();
            p.setCode(code);
            p.setName(name);
            p.setRoles(selectedRoles);
            p.setCreatedAt(LocalDateTime.now());

            Permission saved = permissionController.create(p);

            if (saved != null) {
                JOptionPane.showMessageDialog(this, "メニューが作成されました！");
                this.dispose();
            } else {
                JOptionPane.showMessageDialog(this, "作成に失敗しました！", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        // ========== UPDATE ==========
        else {
            editingPermission.setCode(code);
            editingPermission.setName(name);
            editingPermission.setRoles(selectedRoles);
            editingPermission.setUpdatedAt(LocalDateTime.now());

            permissionController.update(editingPermission);

            JOptionPane.showMessageDialog(this, "メニューが更新されました！");
            this.dispose();
        }
    }


    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new AddMenu().setVisible(true));
    }

    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JCheckBox chkAdmin;
    private javax.swing.JCheckBox chkEmployee;
    private javax.swing.JCheckBox chkManager;
    private javax.swing.JLabel lbMenucode;
    private javax.swing.JLabel lbMenuname;
    private javax.swing.JLabel lbRole;
    private javax.swing.JPanel pnlAddMenu;
    private javax.swing.JTextField txtMenucode;
    private javax.swing.JTextField txtMenuname;
}
