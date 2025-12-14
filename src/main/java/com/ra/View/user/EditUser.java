package com.ra.View.user;

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
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

/**
 *
 * @author Admin
 */
public class EditUser extends JFrame {

    private static final Logger logger = Logger.getLogger(EditUser.class.getName());

    // ✅ object model like AddUser/AddProject
    private DefaultListModel<Tasks> taskModel = new DefaultListModel<>();

    private UserController userController = new UserController();
    private Users currentUser;
    private AllUser parentPanel;

    public EditUser(Users users, AllUser parentPanel) {
        initComponents();

        this.parentPanel = parentPanel;
        this.currentUser = users;

        // ✅ giống AddUser/AddProject: chỉ set MULTIPLE, không toggle mouse
        listTask.setModel(taskModel);
        listTask.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        // ✅ renderer để hiển thị name
        listTask.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Tasks t) setText(t.getName());
                else setText("");
                return this;
            }
        });

        loadData();
        loadUserData();
    }

    public EditUser(Users users) {
        this(users, null);
    }

    private void loadData() {
        loadDepartments();
        loadTasks();
        loadRoles();
    }

    private void loadUserData() {
        if (currentUser == null) return;

        txtMail.setText(currentUser.getEmail());
        txtUsername.setText(currentUser.getUserName());
        txtPassword.setText(currentUser.getPassword());
        txtEmployeename.setText(currentUser.getFullName());

        // ✅ select Department object
        if (currentUser.getDepartment() != null) {
            cbDepartment.setSelectedItem(currentUser.getDepartment());
        }

        // ✅ select Role object
        if (currentUser.getRole() != null) {
            cbRole.setSelectedItem(currentUser.getRole());
        }

        // ✅ select Tasks by id
        List<Tasks> userTasks = currentUser.getTasks();
        if (userTasks == null || userTasks.isEmpty()) return;

        Set<Integer> ids = userTasks.stream()
                .map(Tasks::getId)
                .collect(Collectors.toSet());

        DefaultListModel<Tasks> model = (DefaultListModel<Tasks>) listTask.getModel();

        listTask.clearSelection();

        List<Integer> selectedIdx = new ArrayList<>();
        for (int i = 0; i < model.size(); i++) {
            Tasks t = model.get(i);
            if (t != null && ids.contains(t.getId())) selectedIdx.add(i);
        }
        listTask.setSelectedIndices(selectedIdx.stream().mapToInt(Integer::intValue).toArray());
    }

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

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "部署一覧の読み込みに失敗しました。",
                    "エラー",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void loadTasks() {
        try {
            TaskDAO taskDAO = new TaskDAO();
            List<Tasks> tasks = taskDAO.findAll();

            taskModel.clear();
            for (Tasks t : tasks) {
                taskModel.addElement(t);
            }

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "タスク一覧の読み込みに失敗しました。",
                    "エラー",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

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

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "ロール一覧の読み込みに失敗しました。",
                    "エラー",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        jFrame1 = new JFrame();
        jCheckBoxMenuItem4 = new JCheckBoxMenuItem();
        jCheckBoxMenuItem5 = new JCheckBoxMenuItem();
        jMenuItem1 = new JMenuItem();
        pnlAdduser = new JPanel();
        lbMail = new JLabel();
        txtMail = new JTextField();
        lbUsername = new JLabel();
        txtUsername = new JTextField();
        lbPassword = new JLabel();
        txtPassword = new JTextField();
        lbEmployeename = new JLabel();
        txtEmployeename = new JTextField();
        lbDepartment = new JLabel();
        lbTask = new JLabel();
        lbRole = new JLabel();

        cbRole = new JComboBox<>();
        btnSave = new JButton();
        cbDepartment = new JComboBox<>();

        jOptionPane1 = new JOptionPane();
        jSeparator1 = new JSeparator();
        jScrollPane1 = new JScrollPane();
        listTask = new JList<>();

        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("ユーザー更新");
        setPreferredSize(new java.awt.Dimension(600, 400));

        pnlAdduser.setBackground(new java.awt.Color(255, 255, 255));
        pnlAdduser.setPreferredSize(new java.awt.Dimension(600, 400));

        lbMail.setFont(new java.awt.Font("Yu Mincho", 1, 14));
        lbMail.setText("メール");

        lbUsername.setFont(new java.awt.Font("Yu Mincho", 1, 14));
        lbUsername.setText("ユーザー名");

        lbPassword.setFont(new java.awt.Font("Yu Mincho", 1, 14));
        lbPassword.setText("パスワード");

        lbEmployeename.setFont(new java.awt.Font("Yu Mincho", 1, 14));
        lbEmployeename.setText("社員名");

        lbDepartment.setFont(new java.awt.Font("Yu Mincho", 1, 14));
        lbDepartment.setText("部署");

        lbTask.setFont(new java.awt.Font("Yu Mincho", 1, 14));
        lbTask.setText("タスク");

        lbRole.setFont(new java.awt.Font("Yu Mincho", 1, 14));
        lbRole.setText("ロール");

        btnSave.setBackground(new java.awt.Color(189, 231, 189));
        btnSave.setFont(new java.awt.Font("Yu Mincho", 1, 14));
        btnSave.setText("保存");
        btnSave.addActionListener(this::btnSaveActionPerformed);

        jScrollPane1.setViewportView(listTask);

        GroupLayout pnlAdduserLayout = new GroupLayout(pnlAdduser);
        pnlAdduser.setLayout(pnlAdduserLayout);
        pnlAdduserLayout.setHorizontalGroup(
                pnlAdduserLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pnlAdduserLayout.createSequentialGroup()
                                .addGroup(pnlAdduserLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlAdduserLayout.createSequentialGroup()
                                                .addGap(47, 47, 47)
                                                .addGroup(pnlAdduserLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addGroup(pnlAdduserLayout.createSequentialGroup()
                                                                .addGroup(pnlAdduserLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                        .addGroup(pnlAdduserLayout.createSequentialGroup()
                                                                                .addComponent(lbMail)
                                                                                .addGap(22, 22, 22))
                                                                        .addGroup(pnlAdduserLayout.createSequentialGroup()
                                                                                .addComponent(lbPassword)
                                                                                .addGap(19, 19, 19))
                                                                        .addGroup(GroupLayout.Alignment.LEADING, pnlAdduserLayout.createSequentialGroup()
                                                                                .addComponent(lbUsername)
                                                                                .addGap(19, 19, 19)))
                                                                .addGroup(pnlAdduserLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                                                        .addComponent(txtPassword, GroupLayout.Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                                                                        .addComponent(txtUsername, GroupLayout.Alignment.LEADING)
                                                                        .addComponent(txtMail, GroupLayout.Alignment.LEADING))
                                                                .addGap(52, 52, 52))
                                                        .addGroup(pnlAdduserLayout.createSequentialGroup()
                                                                .addComponent(lbRole)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(cbRole, GroupLayout.PREFERRED_SIZE, 140, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(53, 53, 53)))
                                                .addGroup(pnlAdduserLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(pnlAdduserLayout.createSequentialGroup()
                                                                .addComponent(lbTask)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 179, GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(pnlAdduserLayout.createSequentialGroup()
                                                                .addGroup(pnlAdduserLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                                        .addComponent(lbEmployeename)
                                                                        .addComponent(lbDepartment))
                                                                .addGap(18, 18, 18)
                                                                .addGroup(pnlAdduserLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(txtEmployeename, GroupLayout.DEFAULT_SIZE, 175, Short.MAX_VALUE)
                                                                        .addComponent(cbDepartment, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                                        .addGroup(pnlAdduserLayout.createSequentialGroup()
                                                .addGap(224, 224, 224)
                                                .addComponent(btnSave)))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlAdduserLayout.setVerticalGroup(
                pnlAdduserLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pnlAdduserLayout.createSequentialGroup()
                                .addGap(59, 59, 59)
                                .addGroup(pnlAdduserLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbMail)
                                        .addComponent(txtMail, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbEmployeename)
                                        .addComponent(txtEmployeename, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGroup(pnlAdduserLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlAdduserLayout.createSequentialGroup()
                                                .addGap(32, 32, 32)
                                                .addGroup(pnlAdduserLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtUsername, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lbUsername)))
                                        .addGroup(pnlAdduserLayout.createSequentialGroup()
                                                .addGap(30, 30, 30)
                                                .addGroup(pnlAdduserLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(cbDepartment, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lbDepartment))))
                                .addGroup(pnlAdduserLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addGroup(pnlAdduserLayout.createSequentialGroup()
                                                .addGap(28, 28, 28)
                                                .addGroup(pnlAdduserLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(txtPassword, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lbPassword)
                                                        .addComponent(lbTask))
                                                .addGap(30, 30, 30)
                                                .addGroup(pnlAdduserLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                        .addComponent(cbRole, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(lbRole)))
                                        .addGroup(pnlAdduserLayout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 76, GroupLayout.PREFERRED_SIZE)
                                                .addGap(63, 63, 63)))
                                .addGroup(pnlAdduserLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnSave))
                                .addGap(74, 74, 74))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(pnlAdduser, GroupLayout.PREFERRED_SIZE, 599, GroupLayout.PREFERRED_SIZE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(pnlAdduser, GroupLayout.PREFERRED_SIZE, 401, GroupLayout.PREFERRED_SIZE)
        );

        pack();
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {
        // 🔒 chống double-click
        btnSave.setEnabled(false);

        try {
            if (currentUser == null) {
                JOptionPane.showMessageDialog(this, "ユーザーが存在しません。", "エラー", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String userName = txtUsername.getText().trim();
            String password = txtPassword.getText().trim();
            String email = txtMail.getText().trim();
            String fullName = txtEmployeename.getText().trim();

            Department dept = (Department) cbDepartment.getSelectedItem();
            Roles role = (Roles) cbRole.getSelectedItem();

            currentUser.setUserName(userName);
            currentUser.setPassword(password);
            currentUser.setEmail(email);
            currentUser.setFullName(fullName);

            if (dept != null) currentUser.setDepartment(dept);

            if (role == null) {
                JOptionPane.showMessageDialog(this, "ロールを選択してください。", "警告", JOptionPane.WARNING_MESSAGE);
                return;
            }
            currentUser.setRole(role);

            // ✅ chọn nhiều tasks bằng Ctrl/Shift
            List<Tasks> newTasks = new ArrayList<>(listTask.getSelectedValuesList());
            currentUser.setTasks(newTasks);

            userController.updateUser(currentUser);

            JOptionPane.showMessageDialog(this, "ユーザーが正常に更新されました！", "成功", JOptionPane.INFORMATION_MESSAGE);

            if (parentPanel != null) {
                parentPanel.loadUserTable();
            }

            this.dispose();

        } catch (IllegalArgumentException ex) {
            // nghiệp vụ -> tiếng Nhật rõ ràng
            JOptionPane.showMessageDialog(this, ex.getMessage(), "警告", JOptionPane.WARNING_MESSAGE);

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    this,
                    "システムエラーが発生しました。\n管理者に連絡してください。",
                    "エラー",
                    JOptionPane.ERROR_MESSAGE
            );
        } finally {
            btnSave.setEnabled(true);
        }
    }

    // Variables declaration
    private JButton btnSave;
    private JComboBox<Department> cbDepartment;
    private JComboBox<Roles> cbRole;
    private JCheckBoxMenuItem jCheckBoxMenuItem4;
    private JCheckBoxMenuItem jCheckBoxMenuItem5;
    private JFrame jFrame1;
    private JMenuItem jMenuItem1;
    private JOptionPane jOptionPane1;
    private JScrollPane jScrollPane1;
    private JSeparator jSeparator1;
    private JLabel lbDepartment;
    private JLabel lbEmployeename;
    private JLabel lbMail;
    private JLabel lbPassword;
    private JLabel lbRole;
    private JLabel lbTask;
    private JLabel lbUsername;
    private JList<Tasks> listTask;
    private JPanel pnlAdduser;
    private JTextField txtEmployeename;
    private JTextField txtMail;
    private JTextField txtPassword;
    private JTextField txtUsername;
}
