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

    private static final Logger logger = Logger.getLogger(AddTask.class.getName());
    private final TaskController taskController = new TaskController();
    private final DepartmentController departmentController = new DepartmentController();
    private final ProjectController projectController = new ProjectController();

    private List<Department> departmentList = new ArrayList<>();
    private List<Project> projectList = new ArrayList<>();

    private JPanel contentPanel;

    // === Task đang edit (nếu != null thì là EDIT MODE)
    private Tasks editingTask = null;

    // Generated UI components
    private JComboBox<String> cbProjectname;
    private JComboBox<String> cbDepartmentname;
    private JLabel lbProjectname;
    private JButton btnCancel;
    private JButton btnSave;
    private JTextField txtTaskname;
    private JLabel lbTaskname;
    private JLabel lbDepartmentname;

    // ==========================================================
    // CONSTRUCTOR — ADD MODE
    // ==========================================================
    public AddTask() {
        initComponents();
        applyCenteredLayout();
        loadDepartments();
        loadProjects();

        btnCancel.addActionListener(this::btnCancelActionPerformed);
        btnSave.addActionListener(this::btnSaveActionPerformed);
    }

    // ==========================================================
    // CONSTRUCTOR — EDIT MODE
    // ==========================================================
    public AddTask(Tasks taskToEdit) {
        this(); // gọi lại constructor mặc định để build UI cũ
        this.editingTask = taskToEdit;
        loadForEdit();
    }

    // ==========================================================
    // LOAD DATA INTO UI WHEN EDITING
    // ==========================================================
    private void loadForEdit() {
        txtTaskname.setText(editingTask.getName());

        if (editingTask.getDepartments() != null && !editingTask.getDepartments().isEmpty()) {
            cbDepartmentname.setSelectedItem(editingTask.getDepartments().get(0).getName());
        }

        if (editingTask.getProjects() != null && !editingTask.getProjects().isEmpty()) {
            cbProjectname.setSelectedItem(editingTask.getProjects().get(0).getName());
        }

        btnSave.setText("更新");  // đổi từ 保存 → 更新
    }

    // ==========================================================
    // LAYOUT — GIỮ NGUYÊN 100% CODE UI CŨ CỦA BẠN
    // ==========================================================
    private void applyCenteredLayout() {

        contentPanel = new JPanel();
        contentPanel.setBackground(new java.awt.Color(255, 255, 255));

        GroupLayout contentLayout = new GroupLayout(contentPanel);
        contentPanel.setLayout(contentLayout);

        this.removeAll();

        contentPanel.add(cbProjectname);
        contentPanel.add(lbProjectname);
        contentPanel.add(btnCancel);
        contentPanel.add(btnSave);
        contentPanel.add(txtTaskname);
        contentPanel.add(lbTaskname);
        contentPanel.add(lbDepartmentname);
        contentPanel.add(cbDepartmentname);

        // 👉 GIỮ NGUYÊN Y CHUỖI LAYOUT NHƯ BẠN ĐÃ GỬI (KHÔNG ĐỔI MỘT DÒNG)
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

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(contentPanel, gbc);

        this.revalidate();
        this.repaint();
    }

    // ==========================================================
    // LOAD DATA COMBOBOX
    // ==========================================================
    private void loadDepartments() {
        departmentList = departmentController.findAll();
        cbDepartmentname.removeAllItems();
        for (Department d : departmentList) cbDepartmentname.addItem(d.getName());
    }

    private void loadProjects() {
        projectList = projectController.findAll();
        cbProjectname.removeAllItems();
        for (Project p : projectList) cbProjectname.addItem(p.getName());
    }

    // ==========================================================
    // BUTTON HANDLERS
    // ==========================================================
    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        java.awt.Window parent = SwingUtilities.getWindowAncestor(this);
        if (parent != null) parent.dispose();
    }

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {

        String taskName = txtTaskname.getText().trim();
        if (taskName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Task name cannot be empty!");
            return;
        }

        Department selectedDept = departmentList.get(cbDepartmentname.getSelectedIndex());
        Project selectedProj = projectList.get(cbProjectname.getSelectedIndex());

        List<Department> deps = new ArrayList<>();
        deps.add(selectedDept);

        List<Project> projs = new ArrayList<>();
        projs.add(selectedProj);

        // === EDIT MODE ===
        if (editingTask != null) {
            editingTask.setName(taskName);
            editingTask.setDepartments(deps);
            editingTask.setProjects(projs);

            taskController.update(editingTask);

            JOptionPane.showMessageDialog(this, "Task updated successfully!");
        }
        // === ADD MODE ===
        else {
            Tasks newTask = new Tasks();
            newTask.setName(taskName);
            newTask.setTaskCode(Tasks.generateTaskCode());
            newTask.setDepartments(deps);
            newTask.setProjects(projs);

            taskController.create(newTask);

            JOptionPane.showMessageDialog(this, "Task created successfully!");
        }

    }

    // ==========================================================
    // AUTO GENERATED UI
    // ==========================================================
    @SuppressWarnings("unchecked")
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

        btnSave.setBackground(new java.awt.Color(102, 255, 102));
        btnSave.setText("保存");

        lbTaskname.setText("タスク名");
        lbDepartmentname.setText("部署名");

        // giữ layout cũ
        this.setLayout(null);
    }
}
