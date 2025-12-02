package com.ra.View.department;

import com.ra.Controller.DepartmentController;
import com.ra.Controller.ProjectController;
import com.ra.Controller.TaskController;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.awt.event.ActionEvent;

public class AddDepartment extends javax.swing.JPanel {

    // Khai báo Controllers và Lists
    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(AddDepartment.class.getName());

    private final DepartmentController departmentController = new DepartmentController();
    private final ProjectController projectController = new ProjectController();
    private final TaskController taskController = new TaskController();

    private List<Project> projectList = new ArrayList<>();
    private List<Tasks> taskList = new ArrayList<>();

    public AddDepartment() {
        initComponents();
        // Gọi các hàm tải dữ liệu
        loadProjects();
        loadTasks();
        // Gắn sự kiện cho các Button (BỔ SUNG)
        btnCancel.addActionListener(this::btnCancelActionPerformed);
        btnSave.addActionListener(this::btnSaveActionPerformed);
    }

    // Phương thức tải danh sách Project (từ File 1)
    private void loadProjects() {
        projectList = projectController.findAll();
        cbbProjectname.removeAllItems();
        for (Project p : projectList) {
            cbbProjectname.addItem(p.getName());
        }
    }

    // Phương thức tải danh sách Task (từ File 1)
    private void loadTasks() {
        taskList = taskController.findAll();
        cbbTaskname.removeAllItems();
        for (Tasks t : taskList) {
            cbbTaskname.addItem(t.getName());
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbDepartmentname = new javax.swing.JLabel();
        txtDepartmentname = new javax.swing.JTextField();
        lbProjectname = new javax.swing.JLabel();
        lbTaskname = new javax.swing.JLabel();
        cbbTaskname = new javax.swing.JComboBox<>();
        cbbProjectname = new javax.swing.JComboBox<>();
        btnCancel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        lbDepartmentname.setText("部署名");

        // ĐÃ XÓA DÒNG GÂY LỖI Ở ĐÂY:
        // txtDepartmentname.addActionListener(this::txtDepartmentnameActionPerformed);

        lbProjectname.setText("プロジェクト名");

        lbTaskname.setText("タスク名");

        btnCancel.setBackground(new java.awt.Color(255, 204, 0));
        btnCancel.setText("キャンセル");

        btnSave.setBackground(new java.awt.Color(153, 255, 0));
        btnSave.setText("保存");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addGroup(layout.createSequentialGroup()
                                                                                .addContainerGap()
                                                                                .addComponent(lbTaskname, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                                                .addGap(87, 87, 87)
                                                                                .addComponent(lbProjectname, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                                .addGap(41, 41, 41))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addComponent(lbDepartmentname, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(26, 26, 26)))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(txtDepartmentname, javax.swing.GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                                                        .addComponent(cbbTaskname, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(cbbProjectname, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(153, 153, 153)
                                                .addComponent(btnCancel)
                                                .addGap(59, 59, 59)
                                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(130, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(76, 76, 76)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbDepartmentname)
                                        .addComponent(txtDepartmentname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbProjectname)
                                        .addComponent(cbbProjectname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbTaskname)
                                        .addComponent(cbbTaskname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCancel)
                                        .addComponent(btnSave))
                                .addContainerGap(93, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // Phương thức xử lý sự kiện nút LƯU (từ File 1)
    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {

        String depName = txtDepartmentname.getText().trim();

        if (depName.isEmpty()) {
            // Hiển thị thông báo trong ngữ cảnh JPanel
            JOptionPane.showMessageDialog(this, "部署名は空にできません！", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int projIndex = cbbProjectname.getSelectedIndex();
        List<Project> selectedProjects = new ArrayList<>();
        if (projIndex >= 0) selectedProjects.add(projectList.get(projIndex));

        int taskIndex = cbbTaskname.getSelectedIndex();
        List<Tasks> selectedTasks = new ArrayList<>();
        if (taskIndex >= 0) selectedTasks.add(taskList.get(taskIndex));

        Department d = new Department();
        d.setName(depName);
        d.setDepartmentCode(Department.generateDepartmentCode());
        d.setProjects(selectedProjects);
        d.setTasks(selectedTasks);

        departmentController.create(d);

        JOptionPane.showMessageDialog(this, "部署が正常に作成されました！");
    }

    // Phương thức xử lý sự kiện nút HỦY (từ File 1)
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        // Nếu JPanel này được đặt trong JDialog/JFrame, đóng cửa sổ đó.
        java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(this);
        if (parentWindow != null) {
            parentWindow.dispose();
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cbbProjectname;
    private javax.swing.JComboBox<String> cbbTaskname;
    private javax.swing.JLabel lbDepartmentname;
    private javax.swing.JLabel lbProjectname;
    private javax.swing.JLabel lbTaskname;
    private javax.swing.JTextField txtDepartmentname;
    // End of variables declaration//GEN-END:variables

    // PHƯƠNG THỨC MAIN ĐỂ CHẠY THỬ
    // ==========================================================
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            // 1. Tạo một cửa sổ JFrame mới để chứa JPanel
            JFrame frame = new JFrame("Add Department Test");
            // 2. Tạo đối tượng JPanel của bạn
            AddDepartment panel = new AddDepartment();
            // 3. Đặt JPanel vào JFrame
            frame.setContentPane(panel);
            // 4. Thiết lập hành vi đóng cửa sổ
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            // 5. Điều chỉnh kích thước cửa sổ vừa với nội dung bên trong
            frame.pack();
            // 6. Đặt cửa sổ ở giữa màn hình
            frame.setLocationRelativeTo(null);
            // 7. Hiển thị cửa sổ
            frame.setVisible(true);
        });
    }
}