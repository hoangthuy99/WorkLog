package com.ra.View.project;

import com.ra.Controller.ProjectController;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AddProject extends JPanel {

    // 1. CHUYỂN KHAI BÁO CONTROLLER VÀ THUỘC TÍNH TRẠNG THÁI
    private final ProjectController projectController = new ProjectController();

    private Integer editingProjectId = null;
    private Project editingProject;

    // Sử dụng JComboBox với kiểu đối tượng để phù hợp với logic loadComboBoxes
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
    }


    // 2. CHUYỂN PHƯƠNG THỨC LOAD DATA

    /** Load department/task */
    private void loadComboBoxes() {
        // LƯU Ý: Nếu không dùng kiểu generic đúng, cần ép kiểu khi gọi removeAllItems()
        ((JComboBox<Department>) cbDepartment).removeAllItems();
        ((JComboBox<Tasks>) cbTask).removeAllItems();

        // Sử dụng projectController.getAllDepartments() để lấy danh sách
        for (Department d : projectController.getAllDepartments())
            // Thêm đối tượng Department vào JComboBox
            // LƯU Ý: JComboBox hiển thị đối tượng bằng cách gọi phương thức toString() của đối tượng đó.
            ((JComboBox<Department>) cbDepartment).addItem(d);

        for (Tasks t : projectController.getAllTasks())
            // Thêm đối tượng Tasks vào JComboBox
            ((JComboBox<Tasks>) cbTask).addItem(t);
    }

    /** Load data lên form */
    private void loadProjectData() {
        editingProject = projectController.findById(editingProjectId);
        projectController.loadRelations(editingProject);

        txtAddProject.setText(editingProject.getName());

        // Chọn Department
        if (!editingProject.getDepartments().isEmpty())
            ((JComboBox<Department>) cbDepartment).setSelectedItem(editingProject.getDepartments().get(0));

        // Chọn Tasks
        if (!editingProject.getTasks().isEmpty())
            ((JComboBox<Tasks>) cbTask).setSelectedItem(editingProject.getTasks().get(0));
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlAddProject = new JPanel();
        lbAddProjectName = new JLabel();
        jLabel2 = new JLabel();
        jLabel3 = new JLabel();
        txtAddProject = new JTextField();
        // Giữ nguyên khai báo của IDE
        cbDepartment = new JComboBox<>();
        cbTask = new JComboBox<>();
        btnCancel = new JButton();
        btnCreate = new JButton();

        // ------------------------- ĐỔI MÀU NỀN CHO PANEL CHÍNH (this) -------------------------
        setBackground(new java.awt.Color(255, 255, 255));

        // ------------------------- ĐỔI MÀU NỀN CHO PANEL CON (pnlAddProject) -------------------------
        pnlAddProject.setBackground(new java.awt.Color(255, 255, 255));


        lbAddProjectName.setText("プロジェクト名");

        jLabel2.setText("部署名");

        jLabel3.setText("タスク名");

        txtAddProject.setText("プロジェクト名");

        btnCancel.setBackground(new java.awt.Color(255, 204, 204));
        btnCancel.setText("キャンセル");

        btnCreate.setBackground(new java.awt.Color(204, 204, 255));
        btnCreate.setText("保存");

        GroupLayout pnlAddProjectLayout = new GroupLayout(pnlAddProject);
        pnlAddProject.setLayout(pnlAddProjectLayout);

        // =========================================================================
        // CĂN CHỈNH BỐ CỤC HORIZONTAL GROUP
        // =========================================================================
        pnlAddProjectLayout.setHorizontalGroup(
                pnlAddProjectLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pnlAddProjectLayout.createSequentialGroup()
                                // Thêm khoảng trống co giãn ở đầu (trái) để căn giữa
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlAddProjectLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlAddProjectLayout.createSequentialGroup()
                                                // Nhóm Label (căn phải)
                                                .addGroup(pnlAddProjectLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(lbAddProjectName, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(18, 18, 18)
                                                // Nhóm Input (Text field/Combo box)
                                                .addGroup(pnlAddProjectLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(txtAddProject, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                                                        .addComponent(cbDepartment, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(cbTask, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(pnlAddProjectLayout.createSequentialGroup()
                                                // Nhóm nút Cancel/Create (căn giữa)
                                                .addGap(120, 120, 120) // Điều chỉnh khoảng cách để căn giữa nhóm nút
                                                .addComponent(btnCancel)
                                                .addGap(89, 89, 89)
                                                .addComponent(btnCreate)))
                                // Thêm khoảng trống co giãn ở cuối (phải) để căn giữa
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        // =========================================================================
        // CĂN CHỈNH BỐ CỤC VERTICAL GROUP
        // =========================================================================
        pnlAddProjectLayout.setVerticalGroup(
                pnlAddProjectLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pnlAddProjectLayout.createSequentialGroup()
                                // Thêm khoảng trống co giãn ở trên
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlAddProjectLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbAddProjectName)
                                        .addComponent(txtAddProject, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)
                                .addGroup(pnlAddProjectLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(cbDepartment, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(29, 29, 29)
                                .addGroup(pnlAddProjectLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(cbTask, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(pnlAddProjectLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCancel, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnCreate, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
                                // Thêm khoảng trống co giãn ở dưới
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        // Đảm bảo pnlAddProject chiếm toàn bộ không gian của JPanel cha
                        .addComponent(pnlAddProject, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        // Đảm bảo pnlAddProject chiếm toàn bộ không gian của JPanel cha
                        .addComponent(pnlAddProject, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Loại bỏ các phương thức ActionPerformed không cần thiết
    private void txtAddProjectActionPerformed(ActionEvent evt) {}
    private void cbDepartmentActionPerformed(ActionEvent evt) {}


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
    private JButton btnCancel;
    private JButton btnCreate;
    // Để giữ IDE Generated Code không bị lỗi, ta giữ kiểu generic không xác định hoặc String,
    // và ép kiểu khi sử dụng trong logic (như đã làm trong loadComboBoxes và saveProject).
    private JComboBox cbDepartment;
    private JComboBox cbTask;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel lbAddProjectName;
    private JPanel pnlAddProject;
    private JTextField txtAddProject;
    // End of variables declaration//GEN-END:variables
}