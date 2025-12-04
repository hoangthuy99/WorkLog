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
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;


public class AddMenu extends JPanel {

    // 1. KHAI BÁO CONTROLLER VÀ ENTITY
    private final PermissionController permissionController = new PermissionController();
    private final RoleController roleController = new RoleController();

    // permission đang sửa (null nếu là tạo mới)
    private Permission editingPermission = null;

    // Khai báo Content Panel để chứa nội dung cũ
    private JPanel contentPanel;


    /**
     * Creates new form AddMenu1
     */
    public AddMenu() {
        initComponents();
        // GỌI PHƯƠNG THỨC CĂN GIỮA
        applyCenteredLayout();
        // Cài đặt sự kiện cho nút Cancel (đóng cửa sổ cha)
        btnCancel.addActionListener(e -> closeParentWindow());
    }

    // Constructor EDIT Mode (Dành cho việc tạo JPanel này trong chế độ chỉnh sửa)
    // Lưu ý: Cần gọi loadEditData() sau khi khởi tạo JPanel này.
    public AddMenu(Permission permission) {
        this.editingPermission = permission;
        initComponents();
        // GỌI PHƯƠNG THỨC CĂN GIỮA
        applyCenteredLayout();
        loadEditData();
        btnSave.setText("更新"); // Cập nhật chữ trên nút
        btnCancel.addActionListener(e -> closeParentWindow());
    }

    // ==========================================================
    // PHƯƠNG THỨC THAY ĐỔI LAYOUT ĐỂ CĂN GIỮA
    // ==========================================================
    private void applyCenteredLayout() {
        // 1. Tạo JPanel mới để giữ bố cục
        contentPanel = new JPanel();
        // Đặt màu nền trắng
        contentPanel.setBackground(new java.awt.Color(255, 255, 255));

        // 2. Tạo GroupLayout mới cho contentPanel
        GroupLayout contentLayout = new GroupLayout(contentPanel);
        contentPanel.setLayout(contentLayout);

        // Xóa tất cả components khỏi JPanel chính
        this.removeAll();

        // Thêm components vào contentPanel (Đây là bước cần thiết khi chuyển từ Null/Absolute Layout)
        contentPanel.add(btnSave);
        contentPanel.add(lbMenucode);
        contentPanel.add(txtMenucode);
        contentPanel.add(lbMenuname);
        contentPanel.add(txtMenuname);
        contentPanel.add(lbRole);
        contentPanel.add(chkEmployee);
        contentPanel.add(chkManager);
        contentPanel.add(chkAdmin);
        contentPanel.add(btnCancel);

        // --- CÁC HẰNG SỐ ĐÃ TỐI ƯU ---
        int LABEL_WIDTH = GroupLayout.PREFERRED_SIZE; // Giữ nguyên kích thước ưu tiên
        int FIELD_WIDTH = 280; // Tăng chiều rộng trường nhập liệu
        int CHECKBOX_WIDTH = 95; // Tăng chiều rộng CheckBox để chứa đủ text
        int BUTTON_WIDTH = 90; // Kích thước nút
        int BUTTON_GAP = 30; // Khoảng cách giữa nút CANCEL và SAVE


        // Bố cục Ngang (Horizontal Group)
        contentLayout.setHorizontalGroup(
                contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(contentLayout.createSequentialGroup()
                                .addContainerGap(40, Short.MAX_VALUE) // Padding trái
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)

                                        // NHÓM 1: LABELS (lbMenucode, lbMenuname, lbRole)
                                        .addComponent(lbRole, LABEL_WIDTH, LABEL_WIDTH, LABEL_WIDTH)
                                        .addComponent(lbMenuname, LABEL_WIDTH, LABEL_WIDTH, LABEL_WIDTH)
                                        .addComponent(lbMenucode, LABEL_WIDTH, LABEL_WIDTH, LABEL_WIDTH))
                                .addGap(20, 20, 20) // Khoảng cách giữa Label và Text/Checkbox
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)

                                        // Hàng 1: Menu Code
                                        .addComponent(txtMenucode, FIELD_WIDTH, FIELD_WIDTH, FIELD_WIDTH)

                                        // Hàng 2: Menu Name
                                        .addComponent(txtMenuname, FIELD_WIDTH, FIELD_WIDTH, FIELD_WIDTH)

                                        // Hàng 3: Roles (Checkboxes)
                                        .addGroup(contentLayout.createSequentialGroup()
                                                .addComponent(chkEmployee, CHECKBOX_WIDTH, CHECKBOX_WIDTH, CHECKBOX_WIDTH)
                                                .addGap(5, 5, 5) // Tăng khoảng cách giữa Checkboxes
                                                .addComponent(chkManager, CHECKBOX_WIDTH, CHECKBOX_WIDTH, CHECKBOX_WIDTH)
                                                .addGap(5, 5, 5) // Tăng khoảng cách giữa Checkboxes
                                                .addComponent(chkAdmin, CHECKBOX_WIDTH, CHECKBOX_WIDTH, CHECKBOX_WIDTH))

                                        // Hàng 4: Buttons (Căn phải so với cột Text/Checkbox)
                                        .addGroup(GroupLayout.Alignment.TRAILING, contentLayout.createSequentialGroup()
                                                .addComponent(btnCancel, BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH)
                                                .addGap(BUTTON_GAP, BUTTON_GAP, BUTTON_GAP)
                                                .addComponent(btnSave, BUTTON_WIDTH, BUTTON_WIDTH, BUTTON_WIDTH)))
                                .addContainerGap(40, Short.MAX_VALUE)) // Padding phải
        );

        // Bố cục Dọc (Vertical Group)
        contentLayout.setVerticalGroup(
                contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(contentLayout.createSequentialGroup()
                                .addGap(50, 50, 50) // Padding trên

                                // Hàng 1: Menu Code
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbMenucode, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtMenucode, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)) // Tăng chiều cao Textfield

                                .addGap(15, 15, 15) // Tăng khoảng cách giữa các hàng

                                // Hàng 2: Menu Name
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbMenuname, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtMenuname, GroupLayout.PREFERRED_SIZE, 30, GroupLayout.PREFERRED_SIZE)) // Tăng chiều cao Textfield

                                .addGap(20, 20, 20) // Tăng khoảng cách giữa các hàng

                                // Hàng 3: Roles
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbRole, GroupLayout.PREFERRED_SIZE, 25, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(chkEmployee)
                                        .addComponent(chkManager)
                                        .addComponent(chkAdmin))

                                .addGap(40, 40, 40) // Khoảng cách giữa Roles và Buttons

                                // Hàng 4: Buttons
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCancel, 35, 35, 35) // Chiều cao nút cố định
                                        .addComponent(btnSave, 35, 35, 35)) // Chiều cao nút cố định

                                .addContainerGap(50, Short.MAX_VALUE)) // Padding dưới
        );

        // 3. Áp dụng GridBagLayout cho JPanel chính (this) và đặt contentPanel vào giữa
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        this.add(contentPanel, gbc);
        this.revalidate();
        this.repaint();
    }
    //... (phần còn lại của code giữ nguyên)
//... (phần còn lại của code giữ nguyên)

    // Phương thức hỗ trợ đóng cửa sổ cha (JFrame/JDialog)
    private void closeParentWindow() {
        java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(this);
        if (parentWindow != null) {
            parentWindow.dispose();
        }
    }

    // Phương thức tải dữ liệu chỉnh sửa
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


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSave = new JButton();
        lbMenucode = new JLabel();
        txtMenucode = new JTextField();
        lbMenuname = new JLabel();
        txtMenuname = new JTextField();
        lbRole = new JLabel();
        chkEmployee = new JCheckBox();
        chkManager = new JCheckBox();
        chkAdmin = new JCheckBox();
        btnCancel = new JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        btnSave.setBackground(new java.awt.Color(186, 225, 186));
        btnSave.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        btnSave.setText("保存");
        btnSave.addActionListener(this::btnSaveActionPerformed);

        lbMenucode.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbMenucode.setText("メニューコード");

        txtMenucode.setText("jTextField1");

        lbMenuname.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbMenuname.setText("メニュー名");

        txtMenuname.setText("jTextField1");
        txtMenuname.addActionListener(this::txtMenunameActionPerformed);

        lbRole.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbRole.setText("ロール");

        chkEmployee.setText("EMPLOYEE");
        chkEmployee.addActionListener(this::chkEmployeeActionPerformed);

        chkManager.setText("MANAGER");
        chkManager.addActionListener(this::chkManagerActionPerformed);

        chkAdmin.setText("ADMIN");
        chkAdmin.addActionListener(this::chkAdminActionPerformed);

        btnCancel.setBackground(new java.awt.Color(255, 204, 153));
        btnCancel.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        btnCancel.setText("キャンセル");

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        this.setLayout(null); // Thay thế bằng null layout tạm thời
    }// </editor-fold>//GEN-END:initComponents

    // ======================================================
    // SAVE HANDLER — CREATE + UPDATE
    // ======================================================
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
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
                closeParentWindow();
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
            closeParentWindow();
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void txtMenunameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMenunameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtMenunameActionPerformed

    private void chkEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkEmployeeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkEmployeeActionPerformed

    private void chkManagerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkManagerActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkManagerActionPerformed

    private void chkAdminActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkAdminActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_chkAdminActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnCancel;
    private JButton btnSave;
    private JCheckBox chkAdmin;
    private JCheckBox chkEmployee;
    private JCheckBox chkManager;
    private JLabel lbMenucode;
    private JLabel lbMenuname;
    private JLabel lbRole;
    private JTextField txtMenucode;
    private JTextField txtMenuname;
    // End of variables declaration//GEN-END:variables


}