package com.ra.View.user;

import com.ra.Controller.UserController;
import com.ra.DAO.Auth.AuthDAO;
import com.ra.DAO.Department.DepartmentDAO;
import com.ra.DAO.Task.TaskDAO;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Roles;
import com.ra.Model.Entity.Tasks;
import com.ra.Model.Entity.Users;
import com.ra.Sercurity.PasswordHash;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Admin
 */
public class AddUser extends javax.swing.JPanel {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AddUser.class.getName());

    // KHAI BÁO BIẾN CHO LOGIC XỬ LÝ
    private final UserController userController = new UserController();
    private DefaultListModel<String> taskModel = new DefaultListModel<>();

    // THAY ĐỔI 1: Thêm thuộc tính lưu trữ tham chiếu đến AllUser
    private AllUser parentPanel;

    /**
     * Creates new form AddUser1
     * CẬP NHẬT CONSTRUCTOR để nhận tham chiếu AllUser
     */
    public AddUser(AllUser parentPanel) {
        initComponents();

        // THAY ĐỔI 2: Gán tham chiếu AllUser
        this.parentPanel = parentPanel;

        // Cài đặt List Task và Load Data khi khởi tạo JPanel
        listTask.setModel(taskModel);
        listTask.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // --- Thêm đoạn MouseListener để click thường chọn được nhiều ---
        listTask.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
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

        // Cài đặt sự kiện cho nút Cancel (đóng cửa sổ cha)
        btnCancel.addActionListener(this::btnCancelActionPerformed);

        loadData();
    }

    // CONSTRUCTOR MẶC ĐỊNH (cần thiết nếu sử dụng IDE Form Designer)
    public AddUser() {
        this(null); // Gọi constructor chính với tham chiếu null
    }


    // Phương thức hỗ trợ đóng cửa sổ cha (JFrame/JDialog)
    private void closeParentWindow() {
        java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(this);
        if (parentWindow != null) {
            parentWindow.dispose();
        }
    }

    // ========================================================
    // LOGIC TẢI DỮ LIỆU
    // ========================================================
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
            logger.log(java.util.logging.Level.SEVERE, "Lỗi tải Department", e);
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
            logger.log(java.util.logging.Level.SEVERE, "Lỗi tải Tasks", e);
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
            logger.log(java.util.logging.Level.SEVERE, "Lỗi tải Roles", e);
        }
    }

    // ========================================================
    // LOGIC XÁC THỰC FORM
    // ========================================================

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

        lbDepartment = new javax.swing.JLabel();
        lbTask = new javax.swing.JLabel();
        lbRole = new javax.swing.JLabel();
        cbRole = new javax.swing.JComboBox<>();
        lbMail = new javax.swing.JLabel();
        btnCancel = new javax.swing.JButton();
        txtMail = new javax.swing.JTextField();
        btnSave = new javax.swing.JButton();
        lbUsername = new javax.swing.JLabel();
        cbDepartment = new javax.swing.JComboBox<>();
        txtUsername = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        listTask = new javax.swing.JList<>();
        lbPassword = new javax.swing.JLabel();
        lbEmployeename = new javax.swing.JLabel();
        txtPassWord = new javax.swing.JPasswordField();
        txtEmployeename = new javax.swing.JTextField();

        setBackground(new java.awt.Color(255, 255, 255));

        lbDepartment.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbDepartment.setText("部署");

        lbTask.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbTask.setText("タスク");

        lbRole.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbRole.setText("ロール");

        cbRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Employee", "Manager", "Admin" }));
        cbRole.addActionListener(this::cbRoleActionPerformed);

        lbMail.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbMail.setText("メール");

        btnCancel.setBackground(new java.awt.Color(255, 204, 153));
        btnCancel.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        btnCancel.setText("キャンセル");
        btnCancel.addActionListener(this::btnCancelActionPerformed);

        txtMail.addActionListener(this::txtMailActionPerformed);

        btnSave.setBackground(new java.awt.Color(189, 231, 189));
        btnSave.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        btnSave.setText("保存");
        btnSave.addActionListener(this::btnSaveActionPerformed);

        lbUsername.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbUsername.setText("ユーザー名");

        txtUsername.addActionListener(this::txtUsernameActionPerformed);

        jScrollPane1.setViewportView(listTask);

        lbPassword.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbPassword.setText("パスワード");

        lbEmployeename.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbEmployeename.setText("社員名");

        txtEmployeename.addActionListener(this::txtEmployeenameActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(60, 60, 60)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lbUsername)
                    .addComponent(lbPassword)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbMail)
                        .addGap(16, 16, 16))
                    .addComponent(lbRole, javax.swing.GroupLayout.Alignment.LEADING))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtUsername, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtMail, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cbRole, 0, 140, Short.MAX_VALUE)
                    .addComponent(txtPassWord))
                .addGap(52, 52, 52)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lbEmployeename)
                        .addGap(18, 18, 18)
                        .addComponent(txtEmployeename, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbTask)
                            .addComponent(lbDepartment))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 179, Short.MAX_VALUE)
                            .addComponent(cbDepartment, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addGap(0, 101, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(188, 188, 188)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(btnSave)
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(59, 59, 59)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbMail)
                    .addComponent(txtMail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lbEmployeename)
                    .addComponent(txtEmployeename, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbUsername)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lbDepartment))))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbPassword)
                            .addComponent(lbTask)
                            .addComponent(txtPassWord, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 32, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbRole)
                            .addComponent(cbRole, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnCancel)
                    .addComponent(btnSave))
                .addGap(74, 74, 74))
        );
    }// </editor-fold>//GEN-END:initComponents

    // ========================================================
    // EVENT HANDLERS
    // ========================================================

    private void cbRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRoleActionPerformed
        // Bỏ trống
    }//GEN-LAST:event_cbRoleActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        closeParentWindow(); // Đóng cửa sổ cha chứa JPanel này
    }//GEN-LAST:event_btnCancelActionPerformed

    private void txtMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMailActionPerformed
        // Bỏ trống
    }//GEN-LAST:event_txtMailActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        if (!validateForm()) {
            return; // dừng lại, không tạo User
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

            // Mã hóa mật khẩu
            PasswordHash passwordHash = new PasswordHash();
            String hashedPassword = passwordHash.hashPassword(password);
            user.setPassword(hashedPassword);

            user.setFullName(fullName);
            user.setUserCode(Users.generateUserCode());
            user.setEmail(email);
            // user.getCreatedAt(); (Tạo trong logic DAO hoặc Model)

            // Lấy Department theo tên
            DepartmentDAO departmentDAO = new DepartmentDAO();
            Department dept = departmentDAO.findFindByName(departmentName).orElse(null);
            if (dept != null) {
                user.setDepartment(dept);
            }

            // Role
            AuthDAO roleDAO = new AuthDAO();
            Optional<Roles> roleOpt = roleDAO.findByName(roleName); // tìm Role theo tên
            if (roleOpt.isPresent()) {
                user.setRole(roleOpt.get()); // gán Role vào User
            } else {
                JOptionPane.showMessageDialog(this, "Role không tồn tại!");
                return; // dừng nếu role không có trong DB
            }

            // Tasks
            TaskDAO taskDAO = new TaskDAO();
            List<String> selectedTasks = listTask.getSelectedValuesList();
            List<Tasks> newTasks = new ArrayList<>();

            for (String name : selectedTasks) {
                taskDAO.findByName(name).ifPresent(newTasks::add);
            }
            user.setTasks(newTasks);

            // Gọi Controller để lưu vào DB
            userController.createUser(user);

            JOptionPane.showMessageDialog(this, "ユーザーが正常に作成されました！");

            // THAY ĐỔI 3: Gọi loadUserTable() từ parentPanel và đóng cửa sổ hiện tại
            if (parentPanel != null) {
                parentPanel.loadUserTable(); // Refresh bảng của AllUser
            }
            closeParentWindow(); // Đóng JDialog chứa AddUser

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnSaveActionPerformed

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsernameActionPerformed
        // Bỏ trống
    }//GEN-LAST:event_txtUsernameActionPerformed

    private void txtEmployeenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmployeenameActionPerformed
        // Bỏ trống
    }//GEN-LAST:event_txtEmployeenameActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cbDepartment;
    private javax.swing.JComboBox<String> cbRole;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lbDepartment;
    private javax.swing.JLabel lbEmployeename;
    private javax.swing.JLabel lbMail;
    private javax.swing.JLabel lbPassword;
    private javax.swing.JLabel lbRole;
    private javax.swing.JLabel lbTask;
    private javax.swing.JLabel lbUsername;
    private javax.swing.JList<String> listTask;
    private javax.swing.JTextField txtEmployeename;
    private javax.swing.JTextField txtMail;
    private javax.swing.JPasswordField txtPassWord;
    private javax.swing.JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}