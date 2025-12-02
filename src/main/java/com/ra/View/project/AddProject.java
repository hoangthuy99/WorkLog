package com.ra.View.project;

import com.ra.Controller.ProjectController;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AddProject extends javax.swing.JPanel {

    // 1. CHUYỂN KHAI BÁO CONTROLLER VÀ THUỘC TÍNH TRẠNG THÁI
    private final ProjectController projectController = new ProjectController();

    private Integer editingProjectId = null;
    private Project editingProject;

    // Sử dụng JComboBox với kiểu đối tượng để phù hợp với logic loadComboBoxes
    // Nếu bạn dùng công cụ thiết kế GUI, bạn cần đảm bảo biến cbDepartment và cbTask
    // được khai báo với kiểu dữ liệu chính xác (Department và Tasks)
    // Trong trường hợp này, tôi sẽ thay đổi kiểu trong phần Variables declaration
    // để phù hợp với logic Controller.

    /** ---------------- ADD MODE (Constructor mặc định) ---------------- */
    public AddProject() {
        initComponents();
        loadComboBoxes();
        // setTitle không áp dụng cho JPanel
        setupEventHandlers();
    }

    /** ---------------- EDIT MODE (Constructor cho Edit) ---------------- */
    public AddProject(int projectId) {
        this.editingProjectId = projectId;
        initComponents();
        loadComboBoxes();
        loadProjectData();
        btnCreate.setText("更新"); // Cập nhật text cho nút Create thành Update
        setupEventHandlers();
    }

    // Phương thức gán sự kiện thủ công (vì initComponents() của JPanel thường không gán sẵn)
    private void setupEventHandlers() {
        // Sự kiện cho nút Hủy
        btnCancel.addActionListener(this::btnCancelActionPerformed);
        // Sự kiện cho nút Lưu/Cập nhật
        btnCreate.addActionListener(this::btnCreateActionPerformed);

        // Loại bỏ các sự kiện không cần thiết do IDE tự tạo (nếu có)
        // Lưu ý: Đã sửa lại việc xóa/chú thích trong phần initComponents/ActionPerform
    }


    // 2. CHUYỂN PHƯƠNG THỨC LOAD DATA

    /** Load department/task */
    private void loadComboBoxes() {
        cbDepartment.removeAllItems();
        cbTask.removeAllItems();

        // Sử dụng projectController.getAllDepartments() để lấy danh sách
        for (Department d : projectController.getAllDepartments())
            // Thêm đối tượng Department vào JComboBox
            // LƯU Ý: JComboBox hiển thị đối tượng bằng cách gọi phương thức toString() của đối tượng đó.
            cbDepartment.addItem(d);

        for (Tasks t : projectController.getAllTasks())
            // Thêm đối tượng Tasks vào JComboBox
            cbTask.addItem(t);
    }

    /** Load data lên form */
    private void loadProjectData() {
        editingProject = projectController.findById(editingProjectId);
        projectController.loadRelations(editingProject);

        txtAddProject.setText(editingProject.getName());

        // Chọn Department
        if (!editingProject.getDepartments().isEmpty())
            cbDepartment.setSelectedItem(editingProject.getDepartments().get(0));

        // Chọn Tasks
        if (!editingProject.getTasks().isEmpty())
            cbTask.setSelectedItem(editingProject.getTasks().get(0));
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlAddProject = new javax.swing.JPanel();
        lbAddProjectName = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtAddProject = new javax.swing.JTextField();
        // Giữ nguyên khai báo của IDE
        cbDepartment = new javax.swing.JComboBox<>();
        cbTask = new javax.swing.JComboBox<>();
        btnCancel = new javax.swing.JButton();
        btnCreate = new javax.swing.JButton();

        lbAddProjectName.setText("プロジェクト名");

        jLabel2.setText("部署名");

        jLabel3.setText("タスク名");

        txtAddProject.setText("プロジェクト名");
        // Bỏ các sự kiện không cần thiết do IDE tự tạo
        // txtAddProject.addActionListener(this::txtAddProjectActionPerformed);

        // Bỏ các sự kiện không cần thiết do IDE tự tạo
        // cbDepartment.addActionListener(this::cbDepartmentActionPerformed);

        btnCancel.setBackground(new java.awt.Color(255, 204, 204));
        btnCancel.setText("キャンセル");

        btnCreate.setBackground(new java.awt.Color(204, 204, 255));
        btnCreate.setText("保存");

        javax.swing.GroupLayout pnlAddProjectLayout = new javax.swing.GroupLayout(pnlAddProject);
        pnlAddProject.setLayout(pnlAddProjectLayout);

        // =========================================================================
        // CĂN CHỈNH BỐ CỤC HORIZONTAL GROUP
        // =========================================================================
        pnlAddProjectLayout.setHorizontalGroup(
                pnlAddProjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlAddProjectLayout.createSequentialGroup()
                                // Thêm khoảng trống co giãn ở đầu (trái) để căn giữa
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlAddProjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlAddProjectLayout.createSequentialGroup()
                                                // Nhóm Label (căn phải)
                                                .addGroup(pnlAddProjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(lbAddProjectName, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(18, 18, 18)
                                                // Nhóm Input (Text field/Combo box)
                                                .addGroup(pnlAddProjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(txtAddProject, javax.swing.GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                                                        .addComponent(cbDepartment, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(cbTask, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(pnlAddProjectLayout.createSequentialGroup()
                                                // Nhóm nút Cancel/Create (căn giữa)
                                                .addGap(120, 120, 120) // Điều chỉnh khoảng cách để căn giữa nhóm nút
                                                .addComponent(btnCancel)
                                                .addGap(89, 89, 89)
                                                .addComponent(btnCreate)))
                                // Thêm khoảng trống co giãn ở cuối (phải) để căn giữa
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        // =========================================================================
        // CĂN CHỈNH BỐ CỤC VERTICAL GROUP
        // =========================================================================
        pnlAddProjectLayout.setVerticalGroup(
                pnlAddProjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlAddProjectLayout.createSequentialGroup()
                                // Thêm khoảng trống co giãn ở trên
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlAddProjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbAddProjectName)
                                        .addComponent(txtAddProject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)
                                .addGroup(pnlAddProjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(cbDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(29, 29, 29)
                                .addGroup(pnlAddProjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(cbTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(pnlAddProjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                                // Thêm khoảng trống co giãn ở dưới
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        // Đảm bảo pnlAddProject chiếm toàn bộ không gian của JPanel cha
                        .addComponent(pnlAddProject, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        // Đảm bảo pnlAddProject chiếm toàn bộ không gian của JPanel cha
                        .addComponent(pnlAddProject, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Loại bỏ các phương thức ActionPerformed không cần thiết
    private void txtAddProjectActionPerformed(java.awt.event.ActionEvent evt) {}
    private void cbDepartmentActionPerformed(java.awt.event.ActionEvent evt) {}


    // 3. CHUYỂN PHƯƠNG THỨC XỬ LÝ SỰ KIỆN

    /** Xử lý sự kiện cho nút Tạo/Cập nhật */
    private void btnCreateActionPerformed(ActionEvent e) {
        saveProject();
    }

    /** Xử lý sự kiện cho nút Hủy */
    private void btnCancelActionPerformed(ActionEvent e) {
        // Trong File 1 (JFrame), logic là dispose().
        // Trong File 2 (JPanel), cần đóng cửa sổ cha chứa nó.
        java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(this);
        if (parentWindow != null) {
            parentWindow.dispose();
        }
    }


    /** Save or update (TỪ FILE GỐC) */
    private void saveProject() {

        String name = txtAddProject.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "プロジェクト名を入力してください");
            return;
        }

        // Ép kiểu các item từ JComboBox về kiểu dữ liệu gốc (vì JComboBox mặc định dùng String)
        Department dept = (Department) cbDepartment.getSelectedItem();
        Tasks task = (Tasks) cbTask.getSelectedItem();

        if (editingProjectId == null) {
            // Logic CREATE
            Project newProject = projectController.create(
                    name,
                    List.of(dept),
                    List.of(task)
            );

            JOptionPane.showMessageDialog(this, "作成完了");

        } else {
            // Logic UPDATE
            editingProject.setName(name);
            editingProject.setDepartments(List.of(dept));
            editingProject.setTasks(List.of(task));

            projectController.update(editingProject);

            JOptionPane.showMessageDialog(this, "更新完了");
        }

        // Đóng cửa sổ cha sau khi Lưu/Cập nhật thành công
        java.awt.Window parentWindow = SwingUtilities.getWindowAncestor(this);
        if (parentWindow != null) {
            parentWindow.dispose();
        }
    }


    // 4. KHAI BÁO BIẾN (Chỉnh sửa để dùng kiểu đối tượng, mặc dù IDE có thể khai báo là <String>)
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCreate;
    // Đã thay đổi kiểu generic để phù hợp với loadComboBoxes()
    private javax.swing.JComboBox<Department> cbDepartment;
    private javax.swing.JComboBox<Tasks> cbTask;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lbAddProjectName;
    private javax.swing.JPanel pnlAddProject;
    private javax.swing.JTextField txtAddProject;
    // End of variables declaration//GEN-END:variables
}