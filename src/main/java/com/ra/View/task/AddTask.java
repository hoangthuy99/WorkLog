package com.ra.View.task;

import com.ra.Controller.DepartmentController;
import com.ra.Controller.ProjectController;
import com.ra.Controller.TaskController;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class AddTask extends JPanel {

    // 1. CHUYỂN KHAI BÁO CONTROLLER VÀ DANH SÁCH DỮ LIỆU
    private static final Logger logger = Logger.getLogger(AddTask.class.getName());
    private final TaskController taskController = new TaskController();
    private final DepartmentController departmentController = new DepartmentController();
    private final ProjectController projectController = new ProjectController();

    // Danh sách dữ liệu nạp vào combobox
    private List<Department> departmentList = new ArrayList<>();
    private List<Project> projectList = new ArrayList<>();

    // Khai báo Content Panel để chứa nội dung cũ
    private JPanel contentPanel;


    public AddTask() {
        initComponents();

        // 2. GỌI PHƯƠNG THỨC CĂN GIỮA
        applyCenteredLayout();

        // 3. GỌI HÀM TẢI DỮ LIỆU VÀ GẮN LẠI SỰ KIỆN
        loadDepartments();
        loadProjects();
        // Gắn lại sự kiện cho các nút
        btnCancel.addActionListener(this::btnCancelActionPerformed);
        btnSave.addActionListener(this::btnSaveActionPerformed);
    }

    // ==========================================================
    // PHƯƠNG THỨC THAY ĐỔI LAYOUT ĐỂ CĂN GIỮA
    // ==========================================================
    private void applyCenteredLayout() {
        // 1. Tạo JPanel mới để giữ bố cục cũ
        contentPanel = new JPanel();
        contentPanel.setBackground(new java.awt.Color(255, 255, 255)); // Giữ màu nền

        // 2. Tạo GroupLayout mới cho contentPanel
        GroupLayout contentLayout = new GroupLayout(contentPanel);
        contentPanel.setLayout(contentLayout);

        // Xóa tất cả components khỏi JPanel chính
        this.removeAll();

        // Thêm components vào contentPanel
        contentPanel.add(cbProjectname);
        contentPanel.add(lbProjectname);
        contentPanel.add(btnCancel);
        contentPanel.add(btnSave);
        contentPanel.add(txtTaskname);
        contentPanel.add(lbTaskname);
        contentPanel.add(lbDepartmentname);
        contentPanel.add(cbDepartmentname);

        // TÁI TẠO LẠI BỐ CỤC CŨ TRÊN contentLayout
        contentLayout.setHorizontalGroup(
                contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(contentLayout.createSequentialGroup()
                                .addGap(96, 96, 96)
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(contentLayout.createSequentialGroup()
                                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addComponent(lbProjectname, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(contentLayout.createSequentialGroup()
                                                                .addGap(36, 36, 36)
                                                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                        .addComponent(lbTaskname)
                                                                        .addComponent(lbDepartmentname))))
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 312, GroupLayout.PREFERRED_SIZE))
                                        .addGroup(GroupLayout.Alignment.TRAILING, contentLayout.createSequentialGroup()
                                                .addGap(52, 52, 52)
                                                .addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 101, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                                .addGap(108, 108, 108))
                                        .addGroup(GroupLayout.Alignment.TRAILING, contentLayout.createSequentialGroup()
                                                .addGap(118, 118, 118)
                                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addComponent(txtTaskname, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(cbDepartmentname, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(cbProjectname, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE))))
                                .addGap(92, 92, 92))
        );
        contentLayout.setVerticalGroup(
                contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(contentLayout.createSequentialGroup()
                                .addGap(118, 118, 118)
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(txtTaskname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbTaskname))
                                .addGap(18, 18, 18)
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(cbDepartmentname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbDepartmentname))
                                .addGap(18, 18, 18)
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(cbProjectname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbProjectname))
                                .addGap(27, 27, 27)
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCancel)
                                        .addComponent(btnSave))
                                .addContainerGap(140, Short.MAX_VALUE))
        );

        // 3. Áp dụng GridBagLayout cho JPanel chính (this) và đặt contentPanel vào giữa
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Cho phép không gian trống co giãn theo chiều ngang
        gbc.weighty = 1.0; // Cho phép không gian trống co giãn theo chiều dọc
        gbc.anchor = GridBagConstraints.CENTER; // Căn giữa contentPanel
        gbc.fill = GridBagConstraints.NONE; // Không kéo giãn contentPanel

        this.add(contentPanel, gbc);
        this.revalidate();
        this.repaint();
    }


    // 2. CHUYỂN CÁC PHƯƠNG THỨC TẢI DỮ LIỆU (Load Data)

    private void loadDepartments() {
        departmentList = departmentController.findAll();
        cbDepartmentname.removeAllItems();

        for (Department d : departmentList) {
            cbDepartmentname.addItem(d.getName());
        }
    }

    private void loadProjects() {
        projectList = projectController.findAll();
        cbProjectname.removeAllItems();

        for (Project p : projectList) {
            cbProjectname.addItem(p.getName());
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

        cbProjectname = new JComboBox<>();
        lbProjectname = new JLabel();
        btnCancel = new JButton();
        btnSave = new JButton();
        txtTaskname = new JTextField();
        lbTaskname = new JLabel();
        lbDepartmentname = new JLabel();
        cbDepartmentname = new JComboBox<>();

        setBackground(new java.awt.Color(255, 255, 255));

        lbProjectname.setText("プロジェクト名");

        btnCancel.setBackground(new java.awt.Color(255, 153, 0));
        btnCancel.setText("キャンセル");
        // Loại bỏ sự kiện trùng lặp
        // btnCancel.addActionListener(this::btnCancelActionPerformed);

        btnSave.setBackground(new java.awt.Color(102, 255, 102));
        btnSave.setText("保存");
        // Loại bỏ sự kiện trùng lặp
        // btnSave.addActionListener(this::btnSaveActionPerformed);

        lbTaskname.setText("タスク名");

        lbDepartmentname.setText("部署名");

        // Giữ layout tự động tạo (GroupLayout) nhưng nó sẽ bị ghi đè bởi applyCenteredLayout().
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        this.setLayout(null); // Thay thế bằng null layout tạm thời
    }// </editor-fold>//GEN-END:initComponents

    // 4. CHUYỂN CÁC PHƯƠNG THỨC XỬ LÝ SỰ KIỆN

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        // Trong JPanel, cần đóng cửa sổ cha (thường là JFrame hoặc JDialog) chứa nó.
        java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(this);
        if (parentWindow != null) {
            parentWindow.dispose();
        }
    }


    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {

        String taskName = txtTaskname.getText().trim();
        if (taskName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Task name cannot be empty!");
            return;
        }

        int depIndex = cbDepartmentname.getSelectedIndex();
        int projIndex = cbProjectname.getSelectedIndex();

        // Lấy đối tượng từ danh sách đã load (dùng index của ComboBox)
        Department selectedDept = depIndex >= 0 ? departmentList.get(depIndex) : null;
        Project selectedProject = projIndex >= 0 ? projectList.get(projIndex) : null;

        List<Department> deps = new ArrayList<>();
        if (selectedDept != null) deps.add(selectedDept);

        List<Project> projs = new ArrayList<>();
        if (selectedProject != null) projs.add(selectedProject);

        // TẠO TASK
        Tasks task = new Tasks();
        task.setName(taskName);
        task.setTaskCode(Tasks.generateTaskCode());
        task.setDepartments(deps);
        task.setProjects(projs);

        // Cập nhật quan hệ ngược lại
        if (selectedDept != null) {
            // Đảm bảo list được khởi tạo
            if (selectedDept.getTasks() == null) selectedDept.setTasks(new ArrayList<>());
            selectedDept.getTasks().add(task);
        }
        if (selectedProject != null) {
            // Đảm bảo list được khởi tạo
            if (selectedProject.getTasks() == null) selectedProject.setTasks(new ArrayList<>());
            selectedProject.getTasks().add(task);
        }

        // SAVE
        taskController.create(task);

        JOptionPane.showMessageDialog(this, "Task created successfully!");

        // Đóng cửa sổ cha sau khi lưu thành công
        java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(this);
        if (parentWindow != null) {
            parentWindow.dispose();
        }
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnCancel;
    private JButton btnSave;
    private JComboBox<String> cbDepartmentname;
    private JComboBox<String> cbProjectname;
    private JLabel lbDepartmentname;
    private JLabel lbProjectname;
    private JLabel lbTaskname;
    private JTextField txtTaskname;
    // End of variables declaration//GEN-END:variables

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.getLogger(AddTask.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            // Tạo một JFrame tạm thời để chứa JPanel AddTask1
            JFrame frame = new JFrame("プロジェクトとタスク追加 (テスト)");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Thêm JPanel vào JFrame
            frame.add(new AddTask());

            // Tự động điều chỉnh kích thước và hiển thị
            frame.pack();
            frame.setLocationRelativeTo(null); // Đặt ở giữa màn hình
            frame.setVisible(true);
        });
    }
}