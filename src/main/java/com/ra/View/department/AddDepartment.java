package com.ra.View.department;

import com.ra.Controller.DepartmentController;
import com.ra.Controller.ProjectController;
import com.ra.Controller.TaskController;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AddDepartment extends JPanel {

    public interface DepartmentListener {
        void onDepartmentOperationComplete();
    }

    private DepartmentListener listener;
    private Integer editingDepartmentId;
    private Department editingDepartment;
    private Window parentWindow;

    private final DepartmentController departmentController = new DepartmentController();
    private final ProjectController projectController = new ProjectController();
    private final TaskController taskController = new TaskController();

    private List<Project> projectList = new ArrayList<>();
    private List<Tasks> taskList = new ArrayList<>();
    private JPanel contentPanel;

    // COMPONENTS
    private JLabel lbDepartmentname;
    private JLabel lbProjectname;
    private JLabel lbTaskname;
    private JTextField txtDepartmentname;

    // ====== MULTI SELECT LISTS ======
    private JList<Project> listProject;
    private JList<Tasks> listTask;
    private JScrollPane scrProject;
    private JScrollPane scrTask;

    private JButton btnSave;

    // -------------------- CONSTRUCTOR - ADD MODE --------------------
    public AddDepartment() {
        this.listener = null;
        this.parentWindow = null;
        this.editingDepartmentId = null;
        initializeForm(null);
    }

    // -------------------- CONSTRUCTOR - EDIT MODE --------------------
    public AddDepartment(Window parentWindow, DepartmentListener listener, Integer id) {
        this.parentWindow = parentWindow;
        this.listener = listener;
        this.editingDepartmentId = id;
        initializeForm(id);
    }

    // -------------------- INIT FORM --------------------
    private void initializeForm(Integer id) {
        initComponents();
        applyCenteredLayout();

        loadProjects();
        loadTasks();

        if (id != null) {
            loadDepartmentData(id);
            btnSave.setText("更新");
        } else {
            btnSave.setText("保存");
        }

        btnSave.addActionListener(e -> saveDepartment());
    }

    // -------------------- LOAD DATA EDIT MODE --------------------
    private void loadDepartmentData(int id) {
        editingDepartment = departmentController.findById(id);
        departmentController.loadRelations(editingDepartment);

        txtDepartmentname.setText(editingDepartment.getName());

        // ===== PROJECT multi select =====
        if (editingDepartment.getProjects() != null && !editingDepartment.getProjects().isEmpty()) {
            Set<Integer> editProjectIds = editingDepartment.getProjects().stream()
                    .map(Project::getId)
                    .collect(Collectors.toSet());

            DefaultListModel<Project> m = (DefaultListModel<Project>) listProject.getModel();
            List<Integer> idx = new ArrayList<>();
            for (int i = 0; i < m.size(); i++) {
                Project p = m.get(i);
                if (p != null && editProjectIds.contains(p.getId())) idx.add(i);
            }
            listProject.setSelectedIndices(idx.stream().mapToInt(Integer::intValue).toArray());
        }

        // ===== TASK multi select =====
        if (editingDepartment.getTasks() != null && !editingDepartment.getTasks().isEmpty()) {
            Set<Integer> editTaskIds = editingDepartment.getTasks().stream()
                    .map(Tasks::getId)
                    .collect(Collectors.toSet());

            DefaultListModel<Tasks> m = (DefaultListModel<Tasks>) listTask.getModel();
            List<Integer> idx = new ArrayList<>();
            for (int i = 0; i < m.size(); i++) {
                Tasks t = m.get(i);
                if (t != null && editTaskIds.contains(t.getId())) idx.add(i);
            }
            listTask.setSelectedIndices(idx.stream().mapToInt(Integer::intValue).toArray());
        }
    }

    // -------------------- LOAD PROJECT LIST --------------------
    private void loadProjects() {
        projectList = projectController.findAll();

        DefaultListModel<Project> model = new DefaultListModel<>();
        for (Project p : projectList) model.addElement(p);
        listProject.setModel(model);

        listProject.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected,
                                                          boolean cellHasFocus) {
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

    // -------------------- LOAD TASK LIST --------------------
    private void loadTasks() {
        taskList = taskController.findAll();

        DefaultListModel<Tasks> model = new DefaultListModel<>();
        for (Tasks t : taskList) model.addElement(t);
        listTask.setModel(model);

        listTask.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Tasks t) {
                    setText(t.getName());
                } else {
                    setText("");
                }
                return this;
            }
        });
    }

    // -------------------- SAVE ACTION --------------------
    private void saveDepartment() {
        String depName = txtDepartmentname.getText().trim();

        if (depName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "部署名は空にできません！", "エラー", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // multi select values
        List<Project> projects = new ArrayList<>(listProject.getSelectedValuesList());
        List<Tasks> tasks = new ArrayList<>(listTask.getSelectedValuesList());

        try {
            if (editingDepartmentId == null) {
                // CREATE
                Department d = new Department();
                d.setName(depName);
                d.setDepartmentCode(Department.generateDepartmentCode());
                d.setProjects(projects);
                d.setTasks(tasks);

                departmentController.create(d);
                JOptionPane.showMessageDialog(this, "部署が作成されました！");
            } else {
                // UPDATE
                editingDepartment.setName(depName);

                // tránh cộng dồn/trùng: ghi đè danh sách lựa chọn
                editingDepartment.setProjects(projects);
                editingDepartment.setTasks(tasks);

                departmentController.update(editingDepartment);
                JOptionPane.showMessageDialog(this, "部署が更新されました！");
            }

            closeAndNotify();

        } catch (Exception ex) {
            String msg = ex.getMessage();

            if (msg != null && msg.contains("既に存在")) {
                JOptionPane.showMessageDialog(this,
                        msg,
                        "重複エラー",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this,
                        "エラーが発生しました: " + msg,
                        "エラー",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void closeAndNotify() {
        if (listener != null) {
            listener.onDepartmentOperationComplete();
        }
        if (parentWindow != null) {
            parentWindow.dispose();
        }
    }

    private void applyCenteredLayout() {
        contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);

        GroupLayout layout = new GroupLayout(contentPanel);
        contentPanel.setLayout(layout);

        contentPanel.add(lbDepartmentname);
        contentPanel.add(txtDepartmentname);
        contentPanel.add(lbProjectname);
        contentPanel.add(scrProject);
        contentPanel.add(lbTaskname);
        contentPanel.add(scrTask);
        contentPanel.add(btnSave);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(70)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        .addComponent(lbDepartmentname)
                                        .addComponent(lbProjectname)
                                        .addComponent(lbTaskname))
                                .addGap(30)
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(txtDepartmentname, 200, 200, 200)
                                        .addComponent(scrProject, 200, 200, 200)
                                        .addComponent(scrTask, 200, 200, 200)))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(150)
                                .addGap(40)
                                .addComponent(btnSave))
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGap(40)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(lbDepartmentname)
                                .addComponent(txtDepartmentname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(25)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(lbProjectname)
                                .addComponent(scrProject, 90, 90, 140))
                        .addGap(25)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(lbTaskname)
                                .addComponent(scrTask, 90, 90, 140))
                        .addGap(40)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(btnSave))
        );

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;

        this.add(contentPanel, gbc);
    }

    private void initComponents() {
        lbDepartmentname = new JLabel("部署名");
        lbProjectname = new JLabel("プロジェクト名");
        lbTaskname = new JLabel("タスク名");

        txtDepartmentname = new JTextField();

        // multi select lists
        listProject = new JList<>();
        listTask = new JList<>();
        listProject.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        listTask.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        scrProject = new JScrollPane(listProject);
        scrTask = new JScrollPane(listTask);

        btnSave = new JButton("保存");

        setBackground(Color.WHITE);
    }
}
