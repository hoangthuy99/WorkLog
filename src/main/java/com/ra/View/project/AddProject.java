package com.ra.View.project;

import com.ra.Controller.ProjectController;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;

import javax.swing.*;
import java.util.List;

public class AddProject extends javax.swing.JFrame {

    private final ProjectController projectController = new ProjectController();

    private Integer editingProjectId = null;
    private Project editingProject;

    /** ---------------- ADD MODE ---------------- */
    public AddProject() {
        initComponents();
        loadComboBoxes();
        setTitle("プロジェクト作成");
    }

    /** ---------------- EDIT MODE ---------------- */
    public AddProject(int projectId) {
        this.editingProjectId = projectId;
        initComponents();
        loadComboBoxes();
        loadProjectData();
        setTitle("プロジェクト編集");
    }


    /** Load department/task */
    private void loadComboBoxes() {
        cbDepartment.removeAllItems();
        cbTask.removeAllItems();

        for (Department d : projectController.getAllDepartments())
            cbDepartment.addItem(d);

        for (Tasks t : projectController.getAllTasks())
            cbTask.addItem(t);
    }

    /** Load data lên form */
    private void loadProjectData() {
        editingProject = projectController.findById(editingProjectId);
        projectController.loadRelations(editingProject);

        txtAddProject.setText(editingProject.getName());
        if (!editingProject.getDepartments().isEmpty())
            cbDepartment.setSelectedItem(editingProject.getDepartments().get(0));

        if (!editingProject.getTasks().isEmpty())
            cbTask.setSelectedItem(editingProject.getTasks().get(0));

        btnCreate.setText("更新");
    }


    @SuppressWarnings("unchecked")
    private void initComponents() {

        pnlAddProject = new javax.swing.JPanel();
        lbAddProjectName = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtAddProject = new javax.swing.JTextField();
        cbDepartment = new javax.swing.JComboBox<>();
        cbTask = new javax.swing.JComboBox<>();
        btnCancel = new javax.swing.JButton();
        btnCreate = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lbAddProjectName.setText("プロジェクト名");
        jLabel2.setText("部署名");
        jLabel3.setText("タスク名");

        btnCancel.setText("キャンセル");
        btnCancel.addActionListener(e -> dispose());

        btnCreate.setText("保存");
        btnCreate.addActionListener(e -> saveProject());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(40)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(jLabel2)
                                        .addComponent(lbAddProjectName)
                                        .addComponent(jLabel3))
                                .addGap(18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtAddProject, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(cbDepartment, 0, 300, Short.MAX_VALUE)
                                        .addComponent(cbTask, 0, 300, Short.MAX_VALUE))
                                .addGap(40))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap(100, Short.MAX_VALUE)
                                .addComponent(btnCancel)
                                .addGap(80)
                                .addComponent(btnCreate)
                                .addGap(100))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(40)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbAddProjectName)
                                        .addComponent(txtAddProject, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(cbDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel3)
                                        .addComponent(cbTask, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnCreate, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40))
        );

        pack();
        setLocationRelativeTo(null);
    }

    /** Save or update */
    private void saveProject() {

        String name = txtAddProject.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "プロジェクト名を入力してください");
            return;
        }

        Department dept = (Department) cbDepartment.getSelectedItem();
        Tasks task = (Tasks) cbTask.getSelectedItem();

        if (editingProjectId == null) {

            Project newProject = projectController.create(
                    name,
                    List.of(dept),
                    List.of(task)
            );

            JOptionPane.showMessageDialog(this, "作成完了");
            dispose();

        } else {

            editingProject.setName(name);
            editingProject.setDepartments(List.of(dept));
            editingProject.setTasks(List.of(task));

            projectController.update(editingProject);

            JOptionPane.showMessageDialog(this, "更新完了");
            dispose();
        }
    }

    /** MAIN để test riêng */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new AddProject().setVisible(true));
    }

    // Variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnCreate;
    private javax.swing.JComboBox<Department> cbDepartment;
    private javax.swing.JComboBox<Tasks> cbTask;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel lbAddProjectName;
    private javax.swing.JPanel pnlAddProject;
    private javax.swing.JTextField txtAddProject;
}
