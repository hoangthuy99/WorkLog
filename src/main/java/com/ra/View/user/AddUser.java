package com.ra.View.user;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import com.ra.Controller.UserController;
import com.ra.DAO.Auth.AuthDAO;
import com.ra.DAO.Department.DepartmentDAO;
import com.ra.DAO.Task.TaskDAO;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Roles;
import com.ra.Model.Entity.Tasks;
import com.ra.Model.Entity.Users;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AddUser extends JPanel {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(AddUser.class.getName());

    private final UserController userController = new UserController();

    // ✅ đổi từ DefaultListModel<String> -> DefaultListModel<Tasks>
    private DefaultListModel<Tasks> taskModel = new DefaultListModel<>();

    private AllUser parentPanel;
    private JPanel contentPanel;
    private char defaultEchoChar;

    public AddUser(AllUser parentPanel) {
        initComponents();
        this.parentPanel = parentPanel;

        defaultEchoChar = txtPassWord.getEchoChar();
        txtPassWord.setEchoChar(defaultEchoChar);

        // ✅ Multi select chuẩn (Ctrl/Shift)
        listTask.setModel(taskModel);
        listTask.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // ✅ hiển thị tên task thay vì object
        listTask.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Tasks t) {
                    setText(t.getName());
                } else {
                    setText("");
                }
                return this;
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
        contentPanel.setBackground(new java.awt.Color(255, 255, 255));

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
        contentPanel.add(cbShowPassword);
        contentPanel.add(txtEmployeename);

        final int FIELD_WIDTH = 200;
        final int HORIZONTAL_GAP = 70;
        final int LEFT_RIGHT_MARGIN = 80;

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(LEFT_RIGHT_MARGIN, LEFT_RIGHT_MARGIN, LEFT_RIGHT_MARGIN)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(lbUsername)
                                        .addComponent(lbMail)
                                        .addComponent(lbPassword)
                                        .addComponent(lbRole))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addComponent(txtUsername, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, FIELD_WIDTH, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtMail, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, FIELD_WIDTH, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(txtPassWord, GroupLayout.PREFERRED_SIZE, FIELD_WIDTH, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbShowPassword, GroupLayout.PREFERRED_SIZE, FIELD_WIDTH, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbRole, 0, FIELD_WIDTH, Short.MAX_VALUE))
                                .addGap(HORIZONTAL_GAP, HORIZONTAL_GAP, HORIZONTAL_GAP)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(lbEmployeename)
                                                .addGap(18, 18, 18)
                                                .addComponent(txtEmployeename, GroupLayout.PREFERRED_SIZE, FIELD_WIDTH, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(lbTask)
                                                        .addComponent(lbDepartment))
                                                .addGap(18, 18, 18)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jScrollPane1, GroupLayout.DEFAULT_SIZE, FIELD_WIDTH, Short.MAX_VALUE)
                                                        .addComponent(cbDepartment, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                                .addGap(0, LEFT_RIGHT_MARGIN, Short.MAX_VALUE))
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
                                .addGap(65, 65, 65)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbMail)
                                        .addComponent(txtMail, GroupLayout.PREFERRED_SIZE, FIELD_HEIGHT, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbEmployeename)
                                        .addComponent(txtEmployeename, GroupLayout.PREFERRED_SIZE, FIELD_HEIGHT, GroupLayout.PREFERRED_SIZE))
                                .addGap(VERTICAL_GAP, VERTICAL_GAP, VERTICAL_GAP)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, FIELD_HEIGHT, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lbUsername))
                                                .addGap(VERTICAL_GAP, VERTICAL_GAP, VERTICAL_GAP)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(lbPassword)
                                                                .addComponent(txtPassWord, GroupLayout.PREFERRED_SIZE, FIELD_HEIGHT, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                                .addComponent(lbTask)
                                                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, SCROLL_PANE_HEIGHT, GroupLayout.PREFERRED_SIZE)))
                                                .addGap(6, 6, 6)
                                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(cbShowPassword))
                                        )
                                        .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(cbDepartment, GroupLayout.PREFERRED_SIZE, FIELD_HEIGHT, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(lbDepartment)))
                                .addGap(ROLE_GAP, ROLE_GAP, ROLE_GAP)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbRole)
                                        .addComponent(cbRole, GroupLayout.PREFERRED_SIZE, FIELD_HEIGHT, GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 60, Short.MAX_VALUE)
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

    // ✅ đổi cbDepartment từ String -> Department
    private void loadDepartments() {
        try {
            DepartmentDAO departmentDAO = new DepartmentDAO();
            List<Department> departments = departmentDAO.findAll();

            cbDepartment.removeAllItems();
            cbDepartment.addItem(null); // 未選択

            for (Department d : departments) {
                cbDepartment.addItem(d);
            }

            cbDepartment.setRenderer(new DefaultListCellRenderer() {
                @Override
                public java.awt.Component getListCellRendererComponent(
                        JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {

                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value == null) setText("未選択");
                    else setText(((Department) value).getName());
                    return this;
                }
            });

            cbDepartment.setSelectedIndex(0);

        } catch (Exception e) {
            e.printStackTrace();
            logger.log(java.util.logging.Level.SEVERE, "Lỗi tải Department", e);
        }
    }

    // ✅ đổi listTask từ String -> Tasks (khỏi findByName)
    private void loadTasks() {
        try {
            TaskDAO taskDAO = new TaskDAO();
            List<Tasks> tasks = taskDAO.findAll();

            DefaultListModel<Tasks> model = (DefaultListModel<Tasks>) listTask.getModel();
            model.clear();

            for (Tasks t : tasks) {
                model.addElement(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
            logger.log(java.util.logging.Level.SEVERE, "Lỗi tải Tasks", e);
        }
    }

    // ✅ đổi cbRole từ String -> Roles
    private void loadRoles() {
        try {
            AuthDAO authDAO = new AuthDAO();
            List<Roles> roles = authDAO.findAllRoles();

            cbRole.removeAllItems();
            cbRole.addItem(null); // 未選択

            for (Roles r : roles) {
                cbRole.addItem(r);
            }

            cbRole.setRenderer(new DefaultListCellRenderer() {
                @Override
                public java.awt.Component getListCellRendererComponent(
                        JList<?> list, Object value, int index,
                        boolean isSelected, boolean cellHasFocus) {

                    super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                    if (value == null) setText("未選択");
                    else setText(((Roles) value).getName());
                    return this;
                }
            });

            cbRole.setSelectedIndex(0);

        } catch (Exception e) {
            e.printStackTrace();
            logger.log(java.util.logging.Level.SEVERE, "ロールの読み込みエラー", e);
        }
    }

    private boolean validateForm() {
        String email = txtMail.getText().trim();
        String username = txtUsername.getText().trim();
        String password = new String(txtPassWord.getPassword()).trim();
        String fullName = txtEmployeename.getText().trim();

        if (email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "メールアドレスを入力してください。", "警告", JOptionPane.WARNING_MESSAGE);
            txtMail.requestFocus();
            return false;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(this, "メールアドレスの形式が正しくありません。", "警告", JOptionPane.WARNING_MESSAGE);
            txtMail.requestFocus();
            return false;
        }

        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "ユーザー名を入力してください。", "警告", JOptionPane.WARNING_MESSAGE);
            txtUsername.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "パスワードを入力してください。", "警告", JOptionPane.WARNING_MESSAGE);
            txtPassWord.requestFocus();
            return false;
        }

        if (fullName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "氏名を入力してください。", "警告", JOptionPane.WARNING_MESSAGE);
            txtEmployeename.requestFocus();
            return false;
        }

        // ✅ Department must be selected
        if (cbDepartment.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "部署を選択してください。", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        // ✅ Role must be selected
        if (cbRole.getSelectedItem() == null) {
            JOptionPane.showMessageDialog(this, "役割を選択してください。", "警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        lbDepartment = new JLabel();
        lbTask = new JLabel();
        lbRole = new JLabel();

        // ✅ generic types
        cbRole = new JComboBox<>();
        lbMail = new JLabel();
        txtMail = new JTextField();
        btnSave = new JButton();
        lbUsername = new JLabel();
        cbDepartment = new JComboBox<>();
        txtUsername = new JTextField();
        jScrollPane1 = new JScrollPane();

        // ✅ listTask now holds Tasks
        listTask = new JList<>();

        lbPassword = new JLabel();
        lbEmployeename = new JLabel();
        txtPassWord = new JPasswordField();
        txtEmployeename = new JTextField();

        cbShowPassword = new JCheckBox("パスワード表示");
        cbShowPassword.setBackground(new java.awt.Color(255, 255, 255));
        cbShowPassword.setFont(new java.awt.Font("Yu Mincho", 0, 12));
        cbShowPassword.addActionListener(e -> togglePassword());

        setBackground(new java.awt.Color(255, 255, 255));

        lbDepartment.setFont(new java.awt.Font("Yu Mincho", 1, 14));
        lbDepartment.setText("部署");

        lbTask.setFont(new java.awt.Font("Yu Mincho", 1, 14));
        lbTask.setText("タスク");

        lbRole.setFont(new java.awt.Font("Yu Mincho", 1, 14));
        lbRole.setText("ロール");

        cbRole.addActionListener(this::cbRoleActionPerformed);

        lbMail.setFont(new java.awt.Font("Yu Mincho", 1, 14));
        lbMail.setText("メール");
        txtMail.addActionListener(this::txtMailActionPerformed);

        btnSave.setBackground(new java.awt.Color(189, 231, 189));
        btnSave.setFont(new java.awt.Font("Yu Mincho", 1, 14));
        btnSave.setText("保存");
        btnSave.addActionListener(this::btnSaveActionPerformed);

        lbUsername.setFont(new java.awt.Font("Yu Mincho", 1, 14));
        lbUsername.setText("ユーザー名");
        txtUsername.addActionListener(this::txtUsernameActionPerformed);

        jScrollPane1.setViewportView(listTask);

        lbPassword.setFont(new java.awt.Font("Yu Mincho", 1, 14));
        lbPassword.setText("パスワード");

        lbEmployeename.setFont(new java.awt.Font("Yu Mincho", 1, 14));
        lbEmployeename.setText("社員名");
        txtEmployeename.addActionListener(this::txtEmployeenameActionPerformed);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(null);
    }

    private void togglePassword() {
        if (cbShowPassword.isSelected()) txtPassWord.setEchoChar((char) 0);
        else txtPassWord.setEchoChar(defaultEchoChar);
    }

    private void cbRoleActionPerformed(java.awt.event.ActionEvent evt) {}
    private void txtMailActionPerformed(java.awt.event.ActionEvent evt) {}

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {

        if (!validateForm()) return;

        String userName = txtUsername.getText().trim();
        String password = new String(txtPassWord.getPassword());
        String email = txtMail.getText();
        String fullName = txtEmployeename.getText();

        Department dept = (Department) cbDepartment.getSelectedItem();
        Roles role = (Roles) cbRole.getSelectedItem();

        try {
            Users user = new Users();
            user.setUserName(userName);
            user.setPassword(password);
            user.setFullName(fullName);
            user.setUserCode(Users.generateUserCode());
            user.setEmail(email);

            if (dept != null) user.setDepartment(dept);

            if (role == null) {
                JOptionPane.showMessageDialog(this, "ロールが存在しません！");
                return;
            }
            user.setRole(role);

            // ✅ lấy thẳng tasks object đã chọn
            List<Tasks> selectedTasks = new ArrayList<>(listTask.getSelectedValuesList());
            user.setTasks(selectedTasks);

            btnSave.setEnabled(false);
            userController.createUser(user);

            JOptionPane.showMessageDialog(this, "ユーザーが正常に作成されました！");

            // nếu muốn refresh list user:
            // if (parentPanel != null) parentPanel.reload();

        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "警告", JOptionPane.WARNING_MESSAGE);
            txtUsername.requestFocus();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "システムエラーが発生しました。\n管理者に連絡してください。",
                    "エラー",
                    JOptionPane.ERROR_MESSAGE
            );
        }
        finally {
            btnSave.setEnabled(true);
        }
    }

    private void txtUsernameActionPerformed(java.awt.event.ActionEvent evt) {}
    private void txtEmployeenameActionPerformed(java.awt.event.ActionEvent evt) {}

    // Variables declaration
    private JButton btnSave;
    private JComboBox<Department> cbDepartment;
    private JComboBox<Roles> cbRole;
    private JScrollPane jScrollPane1;
    private JLabel lbDepartment;
    private JLabel lbEmployeename;
    private JLabel lbMail;
    private JLabel lbPassword;
    private JLabel lbRole;
    private JLabel lbTask;
    private JLabel lbUsername;

    private JList<Tasks> listTask;

    private JTextField txtEmployeename;
    private JTextField txtMail;
    private JPasswordField txtPassWord;
    private JTextField txtUsername;
    private JCheckBox cbShowPassword;
}
