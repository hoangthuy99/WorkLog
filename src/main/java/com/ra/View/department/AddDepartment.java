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

public class AddDepartment extends javax.swing.JDialog {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(AddDepartment.class.getName());

    private final DepartmentController departmentController = new DepartmentController();
    private final ProjectController projectController = new ProjectController();
    private final TaskController taskController = new TaskController();

    private List<Project> projectList = new ArrayList<>();
    private List<Tasks> taskList = new ArrayList<>();

    public AddDepartment(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        loadProjects();
        loadTasks();
    }

    private void loadProjects() {
        projectList = projectController.findAll();
        cbbProjectname.removeAllItems();
        for (Project p : projectList) {
            cbbProjectname.addItem(p.getName());
        }
    }

    private void loadTasks() {
        taskList = taskController.findAll();
        cbbTaskname.removeAllItems();
        for (Tasks t : taskList) {
            cbbTaskname.addItem(t.getName());
        }
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        lbDepartmentname = new javax.swing.JLabel();
        txtDepartmentname = new javax.swing.JTextField();
        lbProjectname = new javax.swing.JLabel();
        lbTaskname = new javax.swing.JLabel();
        cbbTaskname = new javax.swing.JComboBox<>();
        cbbProjectname = new javax.swing.JComboBox<>();
        btnCancel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("部署作成");

        lbDepartmentname.setText("部署名");
        lbProjectname.setText("プロジェクト名");
        lbTaskname.setText("タスク名");

        btnCancel.setBackground(new java.awt.Color(255, 204, 0));
        btnCancel.setText("キャンセル");
        btnCancel.addActionListener(this::btnCancelActionPerformed);

        btnSave.setBackground(new java.awt.Color(153, 255, 0));
        btnSave.setText("保存");

        // ❗❗❗ QUAN TRỌNG – GẮN SỰ KIỆN
        btnSave.addActionListener(this::btnSaveActionPerformed);

        //------------------------------ Layout ------------------------------

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(60)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lbDepartmentname)
                                        .addComponent(lbProjectname)
                                        .addComponent(lbTaskname))
                                .addGap(20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtDepartmentname)
                                        .addComponent(cbbProjectname, 0, 200, Short.MAX_VALUE)
                                        .addComponent(cbbTaskname, 0, 200, Short.MAX_VALUE))
                                .addContainerGap(80, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(120)
                                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(40)
                                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(40)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbDepartmentname)
                                        .addComponent(txtDepartmentname, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbProjectname)
                                        .addComponent(cbbProjectname, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbTaskname)
                                        .addComponent(cbbTaskname, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(40)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCancel)
                                        .addComponent(btnSave))
                                .addContainerGap(40, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }

    //------------------------------ EVENTS ------------------------------

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {

        String depName = txtDepartmentname.getText().trim();

        if (depName.isEmpty()) {
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
        dispose();
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        dispose();
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() ->
                new AddDepartment(new javax.swing.JFrame(), true).setVisible(true)
        );
    }

    // Variables declaration
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<String> cbbProjectname;
    private javax.swing.JComboBox<String> cbbTaskname;
    private javax.swing.JLabel lbDepartmentname;
    private javax.swing.JLabel lbProjectname;
    private javax.swing.JLabel lbTaskname;
    private javax.swing.JTextField txtDepartmentname;
}
