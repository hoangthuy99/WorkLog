package com.ra.View.project;

import com.ra.Controller.ProjectController;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AddProject extends JPanel {

    private final ProjectController projectController = new ProjectController();
    private Integer editingProjectId = null;
    private Project editingProject;
    private List<Department> departmentList;
    private List<Tasks> taskList;

    /** ---------------- ADD MODE (Constructor mặc định) ---------------- */
    public AddProject() {
        initComponents();
        loadLists();
        setupEventHandlers();
    }

    /** ---------------- EDIT MODE (Constructor cho Edit) ---------------- */
    public AddProject(int projectId) {
        this.editingProjectId = projectId;
        initComponents();
        loadLists();
        loadProjectData();
        btnCreate.setText("更新");
        setupEventHandlers();
    }

    private void setupEventHandlers() {
        btnCreate.addActionListener(this::btnCreateActionPerformed);
    }

    /** Load department/task */
    private void loadLists() {
        departmentList = projectController.getAllDepartments();
        taskList = projectController.getAllTasks();

        // ===== Department list =====
        DefaultListModel<Department> deptModel = new DefaultListModel<>();
        for (Department d : departmentList) deptModel.addElement(d);
        listDepartment.setModel(deptModel);

        listDepartment.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Department d) setText(d.getName());
                else setText("");
                return this;
            }
        });

        // ===== Task list =====
        DefaultListModel<Tasks> taskModel = new DefaultListModel<>();
        for (Tasks t : taskList) taskModel.addElement(t);
        listTask.setModel(taskModel);

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
    }

    /** Load data lên form */
    private void loadProjectData() {
        editingProject = projectController.findById(editingProjectId);
        projectController.loadRelations(editingProject);

        txtAddProject.setText(editingProject.getName());

        // ===== DEPARTMENT multi-select =====
        if (editingProject.getDepartments() != null && !editingProject.getDepartments().isEmpty()) {
            Set<Integer> deptIds = editingProject.getDepartments().stream()
                    .map(Department::getId)
                    .collect(Collectors.toSet());

            DefaultListModel<Department> m = (DefaultListModel<Department>) listDepartment.getModel();
            List<Integer> idx = new ArrayList<>();
            for (int i = 0; i < m.size(); i++) {
                Department d = m.get(i);
                if (d != null && deptIds.contains(d.getId())) idx.add(i);
            }
            listDepartment.setSelectedIndices(idx.stream().mapToInt(Integer::intValue).toArray());
        }

        // ===== TASK multi-select =====
        if (editingProject.getTasks() != null && !editingProject.getTasks().isEmpty()) {
            Set<Integer> taskIds = editingProject.getTasks().stream()
                    .map(Tasks::getId)
                    .collect(Collectors.toSet());

            DefaultListModel<Tasks> m = (DefaultListModel<Tasks>) listTask.getModel();
            List<Integer> idx = new ArrayList<>();
            for (int i = 0; i < m.size(); i++) {
                Tasks t = m.get(i);
                if (t != null && taskIds.contains(t.getId())) idx.add(i);
            }
            listTask.setSelectedIndices(idx.stream().mapToInt(Integer::intValue).toArray());
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

        // ===== đổi JComboBox -> JList + JScrollPane =====
        listDepartment = new JList<>();
        listTask = new JList<>();
        listDepartment.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listTask.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        scrDepartment = new JScrollPane(listDepartment);
        scrTask = new JScrollPane(listTask);

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
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlAddProjectLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(pnlAddProjectLayout.createSequentialGroup()
                                                .addGroup(pnlAddProjectLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(lbAddProjectName, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel2, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel3, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(18, 18, 18)
                                                .addGroup(pnlAddProjectLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(txtAddProject, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                                                        // đổi cbDepartment -> scrDepartment
                                                        .addComponent(scrDepartment, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
                                                        // đổi cbTask -> scrTask
                                                        .addComponent(scrTask, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)))
                                        .addGroup(pnlAddProjectLayout.createSequentialGroup()
                                                .addGap(120, 120, 120)
                                                .addGap(89, 89, 89)
                                                .addComponent(btnCreate)))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlAddProjectLayout.setVerticalGroup(
                pnlAddProjectLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(pnlAddProjectLayout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(pnlAddProjectLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbAddProjectName)
                                        .addComponent(txtAddProject, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(22, 22, 22)
                                .addGroup(pnlAddProjectLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel2)
                                        // list cao hơn combobox để chọn nhiều
                                        .addComponent(scrDepartment, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
                                .addGap(29, 29, 29)
                                .addGroup(pnlAddProjectLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel3)
                                        .addComponent(scrTask, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(pnlAddProjectLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCreate, GroupLayout.PREFERRED_SIZE, 34, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(pnlAddProject, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(pnlAddProject, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void txtAddProjectActionPerformed(ActionEvent evt) {}
    private void cbDepartmentActionPerformed(ActionEvent evt) {}

    /** Xử lý sự kiện cho nút Tạo/Cập nhật */
    private void btnCreateActionPerformed(ActionEvent e) {
        saveProject();
    }

    /** Save or update (đã sửa để lấy multi-select) */
    private void saveProject() {

        String name = txtAddProject.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "プロジェクト名を入力してください。",
                    "エラー",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        // multi select
        List<Department> departments = new ArrayList<>(listDepartment.getSelectedValuesList());
        List<Tasks> tasks = new ArrayList<>(listTask.getSelectedValuesList());

        try {
            if (editingProjectId == null) {

                projectController.create(
                        name,
                        departments,
                        tasks
                );

                JOptionPane.showMessageDialog(this,
                        "プロジェクトを作成しました。",
                        "成功",
                        JOptionPane.INFORMATION_MESSAGE);

            } else {

                editingProject.setName(name);

                // ghi đè list để tránh cộng dồn/trùng
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

    // ===== bỏ cbDepartment/cbTask, thay bằng list + scroll =====
    private JList<Department> listDepartment;
    private JList<Tasks> listTask;
    private JScrollPane scrDepartment;
    private JScrollPane scrTask;

    private JLabel jLabel2;
    private JLabel jLabel3;
    private JLabel lbAddProjectName;
    private JPanel pnlAddProject;
    private JTextField txtAddProject;
}
