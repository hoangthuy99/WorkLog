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
import java.util.Set;
import java.util.logging.Logger;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.stream.Collectors;

public class AddTask extends JPanel {

    private static final Logger logger = Logger.getLogger(AddTask.class.getName());
    private final TaskController taskController = new TaskController();
    private final DepartmentController departmentController = new DepartmentController();
    private final ProjectController projectController = new ProjectController();

    private List<Department> departmentList = new ArrayList<>();
    private List<Project> projectList = new ArrayList<>();

    private JPanel contentPanel;


    private Tasks editingTask = null;


    private JList<Project> listProject;
    private JList<Department> listDepartment;
    private JScrollPane scrProject;
    private JScrollPane scrDepartment;

    private JLabel lbProjectname;
    private JButton btnSave;
    private JTextField txtTaskname;
    private JLabel lbTaskname;
    private JLabel lbDepartmentname;


    public AddTask() {
        initComponents();
        applyCenteredLayout();
        loadDepartments();
        loadProjects();

        btnSave.addActionListener(this::btnSaveActionPerformed);
    }


    public AddTask(Tasks taskToEdit) {
        this(); // build UI
        this.editingTask = taskToEdit;
        loadForEdit();
    }


    private void loadForEdit() {
        if (editingTask == null) return;

        txtTaskname.setText(editingTask.getName());

        if (editingTask.getDepartments() != null && !editingTask.getDepartments().isEmpty()) {
            Set<Integer> deptIds = editingTask.getDepartments().stream()
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

        if (editingTask.getProjects() != null && !editingTask.getProjects().isEmpty()) {
            Set<Integer> projIds = editingTask.getProjects().stream()
                    .map(Project::getId)
                    .collect(Collectors.toSet());

            DefaultListModel<Project> m = (DefaultListModel<Project>) listProject.getModel();
            List<Integer> idx = new ArrayList<>();
            for (int i = 0; i < m.size(); i++) {
                Project p = m.get(i);
                if (p != null && projIds.contains(p.getId())) idx.add(i);
            }
            listProject.setSelectedIndices(idx.stream().mapToInt(Integer::intValue).toArray());
        }

        btnSave.setText("更新");
    }


    private void applyCenteredLayout() {

        contentPanel = new JPanel();
        contentPanel.setBackground(new java.awt.Color(255, 255, 255));

        GroupLayout contentLayout = new GroupLayout(contentPanel);
        contentPanel.setLayout(contentLayout);

        this.removeAll();

        // add components
        contentPanel.add(scrProject);
        contentPanel.add(lbProjectname);
        contentPanel.add(btnSave);
        contentPanel.add(txtTaskname);
        contentPanel.add(lbTaskname);
        contentPanel.add(lbDepartmentname);
        contentPanel.add(scrDepartment);

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
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                                .addGap(108, 108, 108))
                                        .addGroup(GroupLayout.Alignment.TRAILING, contentLayout.createSequentialGroup()
                                                .addGap(118, 118, 118)
                                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                        .addComponent(txtTaskname, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)
                                                        // đổi cbDepartmentname -> scrDepartment (list)
                                                        .addComponent(scrDepartment, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE)
                                                        // đổi cbProjectname -> scrProject (list)
                                                        .addComponent(scrProject, GroupLayout.PREFERRED_SIZE, 284, GroupLayout.PREFERRED_SIZE))))
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
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        // đổi cbDepartmentname -> scrDepartment
                                        .addComponent(scrDepartment, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbDepartmentname))
                                .addGap(18, 18, 18)
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        // đổi cbProjectname -> scrProject
                                        .addComponent(scrProject, GroupLayout.PREFERRED_SIZE, 90, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(lbProjectname))
                                .addGap(27, 27, 27)
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
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


    private void loadDepartments() {
        departmentList = departmentController.findAll();

        DefaultListModel<Department> model = new DefaultListModel<>();
        for (Department d : departmentList) model.addElement(d);
        listDepartment.setModel(model);

        listDepartment.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Department d) {
                    setText(d.getName());
                } else {
                    setText("");
                }
                return this;
            }
        });
    }

    private void loadProjects() {
        projectList = projectController.findAll();

        DefaultListModel<Project> model = new DefaultListModel<>();
        for (Project p : projectList) model.addElement(p);
        listProject.setModel(model);

        listProject.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Project p) {
                    setText(p.getName());
                } else {
                    setText("");
                }
                return this;
            }
        });
    }


    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {

        String taskName = txtTaskname.getText().trim();
        if (taskName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "タスク名を空にすることはできません！");
            return;
        }

        // multi select
        List<Department> deps = new ArrayList<>(listDepartment.getSelectedValuesList());
        List<Project> projs = new ArrayList<>(listProject.getSelectedValuesList());

        try {
            // === EDIT MODE ===
            if (editingTask != null) {
                editingTask.setName(taskName);

                // ghi đè list để tránh cộng dồn/trùng
                editingTask.setDepartments(deps);
                editingTask.setProjects(projs);

                taskController.update(editingTask);
                JOptionPane.showMessageDialog(this, "タスクの更新が完了しました。");
            }
            // === ADD MODE ===
            else {
                Tasks newTask = new Tasks();
                newTask.setName(taskName);
                newTask.setTaskCode(Tasks.generateTaskCode());
                newTask.setDepartments(deps);
                newTask.setProjects(projs);

                taskController.create(newTask);
                JOptionPane.showMessageDialog(this, "タスクの作成が完了しました。");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(
                    this,
                    ex.getMessage(),
                    "エラー",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }


    @SuppressWarnings("unchecked")
    private void initComponents() {

        // lists
        listProject = new JList<>();
        listDepartment = new JList<>();

        listProject.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listDepartment.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        scrProject = new JScrollPane(listProject);
        scrDepartment = new JScrollPane(listDepartment);

        lbProjectname = new JLabel();
        btnSave = new JButton();
        txtTaskname = new JTextField();
        lbTaskname = new JLabel();
        lbDepartmentname = new JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        lbProjectname.setText("プロジェクト名");
        btnSave.setBackground(new java.awt.Color(102, 255, 102));
        btnSave.setText("保存");

        lbTaskname.setText("タスク名");
        lbDepartmentname.setText("部署名");
    }
}
