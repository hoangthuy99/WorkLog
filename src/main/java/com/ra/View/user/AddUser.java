package com.ra.View.user;

// Import cần thiết cho việc căn giữa
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
// ... các imports khác ...
import com.ra.Controller.UserController;
import com.ra.DAO.Auth.AuthDAO;
import com.ra.DAO.Department.DepartmentDAO;
import com.ra.DAO.Task.TaskDAO;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Roles;
import com.ra.Model.Entity.Tasks;
import com.ra.Model.Entity.Users;
import com.ra.View.dashboard.MainDashboard;

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
public class AddUser extends JPanel {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AddUser.class.getName());
    private final UserController userController = new UserController();
    private DefaultListModel<String> taskModel = new DefaultListModel<>();
    private AllUser parentPanel;
    private JPanel contentPanel;

    /**
     * Creates new form AddUser1
     */
    public AddUser(AllUser parentPanel) {
        initComponents();
        this.parentPanel = parentPanel;
        listTask.setModel(taskModel);
        listTask.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
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
        applyCenteredLayout();
        loadData();
    }
    public AddUser() {
        this(null);
    }
    private void applyCenteredLayout() {
        this.setLayout(new GridBagLayout());
        contentPanel = new JPanel();
        GroupLayout contentLayout = new GroupLayout(contentPanel);
        contentPanel.setLayout(contentLayout);
        contentPanel.setBackground(new java.awt.Color(255, 255, 255)); // Đảm bảo màu nền khớp
        setupContentPanelLayout(contentLayout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;

        this.add(contentPanel, gbc);
    }
    private void setupContentPanelLayout(GroupLayout layout) {
        this.removeAll();
        contentPanel.add(lbDepartment);
        contentPanel.add(lbTask);
        contentPanel.add(lbRole);
        contentPanel.add(cbRole);
        contentPanel.add(lbMail);
        contentPanel.add(txtMail);
        contentPanel.add(btnSave);
        contentPanel.add(lbUsername);
        contentPanel.add(cbDepartment);
        contentPanel.add(txtUsername);
        contentPanel.add(jScrollPane1);
        contentPanel.add(lbPassword);
        contentPanel.add(lbEmployeename);
        contentPanel.add(txtPassWord);
        contentPanel.add(txtEmployeename);

        final int FIELD_WIDTH = 200;
        final int HORIZONTAL_GAP = 70;
        final int LEFT_RIGHT_MARGIN = 80;


        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(LEFT_RIGHT_MARGIN, LEFT_RIGHT_MARGIN, LEFT_RIGHT_MARGIN) // Lề trái
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(lbUsername)
                                        .addComponent(lbMail)
                                        .addComponent(lbPassword)
                                        .addComponent(lbRole))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        // CỘT 1: Chiều rộng 200px
                                        .addComponent(txtUsername, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, FIELD_WIDTH, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtMail, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, FIELD_WIDTH, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtPassWord, GroupLayout.PREFERRED_SIZE, FIELD_WIDTH, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbRole, 0, FIELD_WIDTH, Short.MAX_VALUE))
                                .addGap(HORIZONTAL_GAP, HORIZONTAL_GAP, HORIZONTAL_GAP) // Khoảng cách giữa hai cột
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(lbEmployeename)
                                                .addGap(18, 18, 18)
                                                // CỘT 2: Chiều rộng 200px
                                                .addComponent(txtEmployeename, GroupLayout.PREFERRED_SIZE, FIELD_WIDTH, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(lbTask)
                                                        .addComponent(lbDepartment))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        // CỘT 2: Chiều rộng 200px
                                                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, FIELD_WIDTH, Short.MAX_VALUE)
                                                        .addComponent(cbDepartment, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(0, LEFT_RIGHT_MARGIN, Short.MAX_VALUE)) // Lề phải
                        .addGroup(layout.createSequentialGroup()
                                .addGap(250, 250, 250)
                                .addGap(40, 40, 40)
                                .addComponent(btnSave)
                                .addGap(20, 20, 20))
        );
        final int FIELD_HEIGHT = 30;
        final int VERTICAL_GAP = 35;
        final int SCROLL_PANE_HEIGHT = 85;
        final int ROLE_GAP = 8;

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(65, 65, 65) // Lề trên
                                // Hàng 1 (Mail/Employeename)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbMail)
                                        .addComponent(txtMail, GroupLayout.PREFERRED_SIZE, FIELD_HEIGHT, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbEmployeename)
                                        .addComponent(txtEmployeename, GroupLayout.PREFERRED_SIZE, FIELD_HEIGHT, GroupLayout.PREFERRED_SIZE))
                                .addGap(VERTICAL_GAP, VERTICAL_GAP, VERTICAL_GAP) // Khoảng cách H1 -> H2 (35px)
                                // Hàng 2 (Username/Department)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, FIELD_HEIGHT, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lbUsername))
                                                .addGap(VERTICAL_GAP, VERTICAL_GAP, VERTICAL_GAP) // Khoảng cách H2 -> H3 (35px)
                                                // Hàng 3 (Password/Task)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(lbPassword)
                                                                .addComponent(txtPassWord, GroupLayout.PREFERRED_SIZE, FIELD_HEIGHT, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(lbTask)
                                                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, SCROLL_PANE_HEIGHT, GroupLayout.PREFERRED_SIZE))
                                                ))
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(cbDepartment, GroupLayout.PREFERRED_SIZE, FIELD_HEIGHT, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lbDepartment)))

                                // Hàng 4 (Role) - SỬ DỤNG KHOẢNG CÁCH MỚI (8px)
                                .addGap(ROLE_GAP, ROLE_GAP, ROLE_GAP)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbRole)
                                        .addComponent(cbRole, GroupLayout.PREFERRED_SIZE, FIELD_HEIGHT, GroupLayout.PREFERRED_SIZE))

                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE) // Giữ khoảng cách nút
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnSave))
                                .addGap(74, 74, 74))
        );
        contentPanel.revalidate();
        contentPanel.repaint();
    }

    private void loadData() {
        loadDepartments();
        loadTasks();
        loadRoles();
    }

    // 🌟 Đã bao gồm dòng hướng dẫn và logic load dữ liệu
    private void loadDepartments() {
        try {
            DepartmentDAO departmentDAO = new DepartmentDAO();
            List<Department> departments = departmentDAO.findAll();
            cbDepartment.removeAllItems();
            cbDepartment.addItem("未選択");
            for (Department d : departments) {
                cbDepartment.addItem(d.getName());
            }
            cbDepartment.setSelectedIndex(0);

        } catch (Exception e) {
            e.printStackTrace();
            logger.log(java.util.logging.Level.SEVERE, "Lỗi tải Department", e);
        }
    }

    private void loadTasks() {
        try {
            TaskDAO taskDAO = new TaskDAO();
            List<Tasks> tasks = taskDAO.findAll();
            DefaultListModel<String> model = (DefaultListModel<String>) listTask.getModel();
            model.clear(); // xoá toàn bộ item cũ

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
            cbRole.addItem("未選択");
            for (Roles r : roles) {
                cbRole.addItem(r.getName());
            }
            cbRole.setSelectedIndex(0);

        }catch (Exception e){
            e.printStackTrace();
            logger.log(java.util.logging.Level.SEVERE, "ロールの読み込みエラー", e);
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
        // 🌟 Đã được sửa để kiểm tra dòng hướng dẫn
        if (cbDepartment.getSelectedIndex() <= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "部署を選択してください。",
                    "警告",
                    JOptionPane.WARNING_MESSAGE
            );
            return false;
        }

        // 役割選択チェック
        // 🌟 Đã được sửa để kiểm tra dòng hướng dẫn
        if (cbRole.getSelectedIndex() <= 0) {
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
     * * LƯU Ý: Phần này KHÔNG được sửa đổi (chỉ giữ lại việc khởi tạo components),
     * Logic Layout đã được chuyển sang applyCenteredLayout()
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbDepartment = new JLabel();
        lbTask = new JLabel();
        lbRole = new JLabel();
        cbRole = new JComboBox<>();
        lbMail = new JLabel();
        txtMail = new JTextField();
        btnSave = new JButton();
        lbUsername = new JLabel();
        cbDepartment = new JComboBox<>();
        txtUsername = new JTextField();
        jScrollPane1 = new JScrollPane();
        listTask = new JList<>();
        lbPassword = new JLabel();
        lbEmployeename = new JLabel();
        txtPassWord = new JPasswordField();
        txtEmployeename = new JTextField();

        setBackground(new java.awt.Color(255, 255, 255));

        lbDepartment.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbDepartment.setText("部署");

        lbTask.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbTask.setText("タスク");

        lbRole.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbRole.setText("ロール");

        // cbRole.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Employee", "Manager", "Admin" })); // Bị xóa vì logic đã chuyển sang loadRoles()
        cbRole.addActionListener(this::cbRoleActionPerformed);

        lbMail.setFont(new java.awt.Font("Yu Mincho", 1, 14)); // NOI18N
        lbMail.setText("メール");


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

        // Thiết lập layout tạm thời để các component không bị lỗi sau khi initComponents
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(null);
    }// </editor-fold>//GEN-END:initComponents


    private void cbRoleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbRoleActionPerformed
    }//GEN-LAST:event_cbRoleActionPerformed

    private void txtMailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMailActionPerformed
    }//GEN-LAST:event_txtMailActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
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
            user.setPassword(password);
            user.setFullName(fullName);
            user.setUserCode(Users.generateUserCode());
            user.setEmail(email);
            DepartmentDAO departmentDAO = new DepartmentDAO();
            Department dept = departmentDAO.findFindByName(departmentName).orElse(null);
            if (dept != null) {
                user.setDepartment(dept);
            }
            AuthDAO roleDAO = new AuthDAO();
            Optional<Roles> roleOpt = roleDAO.findByName(roleName);
            if (roleOpt.isPresent()) {
                user.setRole(roleOpt.get());
            } else {
                JOptionPane.showMessageDialog(this, "ロールが存在しません！");
                return;
            }

            TaskDAO taskDAO = new TaskDAO();
            List<String> selectedTasks = listTask.getSelectedValuesList();
            List<Tasks> newTasks = new ArrayList<>();

            for (String name : selectedTasks) {
                taskDAO.findByName(name).ifPresent(newTasks::add);
            }
            user.setTasks(newTasks);
            userController.createUser(user);

            JOptionPane.showMessageDialog(this, "ユーザーが正常に作成されました！");
            if (parentPanel != null) {
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "エラー： " + e.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtUsernameActionPerformed
    }//GEN-LAST:event_txtUsernameActionPerformed

    private void txtEmployeenameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtEmployeenameActionPerformed
        // Bỏ trống
    }//GEN-LAST:event_txtEmployeenameActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnSave;
    private JComboBox<String> cbDepartment;
    private JComboBox<String> cbRole;
    private JScrollPane jScrollPane1;
    private JLabel lbDepartment;
    private JLabel lbEmployeename;
    private JLabel lbMail;
    private JLabel lbPassword;
    private JLabel lbRole;
    private JLabel lbTask;
    private JLabel lbUsername;
    private JList<String> listTask;
    private JTextField txtEmployeename;
    private JTextField txtMail;
    private JPasswordField txtPassWord;
    private JTextField txtUsername;
    // End of variables declaration//GEN-END:variables
}