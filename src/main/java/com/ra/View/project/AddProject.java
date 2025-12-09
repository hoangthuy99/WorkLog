package com.ra.View.project;

import com.ra.Controller.ProjectController;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.List;

public class AddProject extends JPanel {

    private final ProjectController projectController = new ProjectController();
    private Integer editingProjectId = null;
    private Project editingProject;

    public AddProject() {
        initComponents();
        loadComboBoxes();
        setupEventHandlers();
    }
    public AddProject(int projectId) {
        this.editingProjectId = projectId;
        initComponents();
        loadComboBoxes();
        loadProjectData();
        btnCreate.setText("更新");
        setupEventHandlers();
    }

    private void setupEventHandlers() {

        btnCreate.addActionListener(this::btnCreateActionPerformed);

    }


    /** Load department/task */
    private void loadComboBoxes() {
        ((JComboBox<Department>) cbDepartment).removeAllItems();
        ((JComboBox<Tasks>) cbTask).removeAllItems();

        ((JComboBox<Department>) cbDepartment).addItem(null);
        ((JComboBox<Tasks>) cbTask).addItem(null);

        for (Department d : projectController.getAllDepartments()) {
            ((JComboBox<Department>) cbDepartment).addItem(d);
        }

        for (Tasks t : projectController.getAllTasks()) {
            ((JComboBox<Tasks>) cbTask).addItem(t);
        }

        ((JComboBox<Department>) cbDepartment).setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("未選択"); // chưa chọn
                } else {
                    setText(((Department) value).getName());
                }
                return this;
            }
        });
        ((JComboBox<Tasks>) cbTask).setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("未選択");
                } else {
                    setText(((Tasks) value).getName());
                }
                return this;
            }
        });
    }


    /** Load data lên form */
    private void loadProjectData() {
        editingProject = projectController.findById(editingProjectId);
        projectController.loadRelations(editingProject);

        txtAddProject.setText(editingProject.getName());

        // Department
        if (editingProject.getDepartments() != null && !editingProject.getDepartments().isEmpty()) {
            ((JComboBox<Department>) cbDepartment).setSelectedItem(editingProject.getDepartments().get(0));
        } else {
            ((JComboBox<Department>) cbDepartment).setSelectedItem(null);
        }

        // Tasks
        if (editingProject.getTasks() != null && !editingProject.getTasks().isEmpty()) {
            ((JComboBox<Tasks>) cbTask).setSelectedItem(editingProject.getTasks().get(0));
        } else {
            ((JComboBox<Tasks>) cbTask).setSelectedItem(null);
        }
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
        btnCreate = new JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        pnlAddProject.setBackground(new java.awt.Color(255, 255, 255));


        lbAddProjectName.setText("プロジェクト名");

        jLabel2.setText("部署名");

        jLabel3.setText("タスク名");

        txtAddProject.setText("プロジェクト名");


        btnCreate.setBackground(new java.awt.Color(204, 204, 255));
        btnCreate.setText("保存");

        GroupLayout pnlAddProjectLayout = new GroupLayout(pnlAddProject);
        pnlAddProject.setLayout(pnlAddProjectLayout);

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
                                                .addGap(120, 120, 120) // Điều chỉnh khoảng cách để căn giữa nhóm nút
                                                .addGap(89, 89, 89)
                                                .addComponent(btnCreate)))
                                // Thêm khoảng trống co giãn ở cuối (phải) để căn giữa
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

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

    private void txtAddProjectActionPerformed(ActionEvent evt) {}
    private void cbDepartmentActionPerformed(ActionEvent evt) {}



    /** Xử lý sự kiện cho nút Tạo/Cập nhật */
    private void btnCreateActionPerformed(ActionEvent e) {
        saveProject();
    }

    /** Xử lý sự kiện cho nút Hủy */



    /** Save or update (TỪ FILE GỐC) */
    private void saveProject() {

        String name = txtAddProject.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "プロジェクト名を入力してください。",
                    "エラー",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        Department dept = (Department) cbDepartment.getSelectedItem();
        Tasks task = (Tasks) cbTask.getSelectedItem();
        List<Department> departments = (dept != null) ? List.of(dept) : List.of();
        List<Tasks> tasks = (task != null) ? List.of(task) : List.of();
        try {

            if (editingProjectId == null) {

                projectController.create(
                        name,
                        departments,
                        tasks
                );

                JOptionPane.showMessageDialog(this,
                        "プロジェクトの作成が完了しました",
                        "成功",
                        JOptionPane.INFORMATION_MESSAGE);

            } else {

                editingProject.setName(name);
                editingProject.setDepartments(departments);
                editingProject.setTasks(tasks);

                projectController.update(editingProject);

                JOptionPane.showMessageDialog(this,
                        "プロジェクトを更新しました。",
                        "成功",
                        JOptionPane.INFORMATION_MESSAGE);
            }

        } catch (Exception ex) {

            JOptionPane.showMessageDialog(this,
                    ex.getMessage(),
                    "エラー",
                    JOptionPane.ERROR_MESSAGE);
        }

    }
    private JButton btnCreate;
    private JComboBox cbDepartment;
    private JComboBox cbTask;
    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel lbAddProjectName;
    private JPanel pnlAddProject;
    private JTextField txtAddProject;
}