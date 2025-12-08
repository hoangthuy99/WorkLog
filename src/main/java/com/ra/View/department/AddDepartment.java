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
public class AddDepartment extends JPanel {

    public interface DepartmentListener {
        void onDepartmentOperationComplete();
    }
    private DepartmentListener listener;
    private Integer editingDepartmentId;
    private Department editingDepartment;
    private Window parentWindow;
    private DepartmentController departmentController = new DepartmentController();
    private ProjectController projectController = new ProjectController();
    private TaskController taskController = new TaskController();
    private List<Project> projectList = new ArrayList<>();
    private List<Tasks> taskList = new ArrayList<>();
    private JPanel contentPanel;

    // COMPONENTS
    private JLabel lbDepartmentname;
    private JLabel lbProjectname;
    private JLabel lbTaskname;
    private JTextField txtDepartmentname;
    private JComboBox<Project> cbbProjectname;
    private JComboBox<Tasks> cbbTaskname;
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

        if (editingDepartment.getProjects() != null && !editingDepartment.getProjects().isEmpty()) {
            cbbProjectname.setSelectedItem(editingDepartment.getProjects().get(0));
        } else {
            cbbProjectname.setSelectedItem(null);
        }

        if (editingDepartment.getTasks() != null && !editingDepartment.getTasks().isEmpty()) {
            cbbTaskname.setSelectedItem(editingDepartment.getTasks().get(0));
        } else {
            cbbTaskname.setSelectedItem(null);
        }
    }


    // -------------------- LOAD PROJECT COMBO --------------------
    private void loadProjects() {
        projectList = projectController.findAll();
        cbbProjectname.removeAllItems();

        cbbProjectname.addItem(null);

        for (Project p : projectList) {
            cbbProjectname.addItem(p);
        }

        cbbProjectname.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("未選択"); // hoặc "プロジェクト未選択"
                } else {
                    setText(((Project) value).getName());
                }
                return this;
            }
        });
    }


    // -------------------- LOAD TASK COMBO --------------------
    private void loadTasks() {
        taskList = taskController.findAll();
        cbbTaskname.removeAllItems();


        cbbTaskname.addItem(null);

        for (Tasks t : taskList) {
            cbbTaskname.addItem(t);
        }

        cbbTaskname.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                                                          int index, boolean isSelected,
                                                          boolean cellHasFocus) {
                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("未選択"); // hoặc "タスク未選択"
                } else {
                    setText(((Tasks) value).getName());
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

        Project selectedProject = (Project) cbbProjectname.getSelectedItem();
        Tasks selectedTask = (Tasks) cbbTaskname.getSelectedItem();

        List<Project> projects = selectedProject != null ? List.of(selectedProject) : new ArrayList<>();
        List<Tasks> tasks = selectedTask != null ? List.of(selectedTask) : new ArrayList<>();

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
                editingDepartment.setProjects(projects);
                editingDepartment.setTasks(tasks);

                departmentController.update(editingDepartment);
                JOptionPane.showMessageDialog(this, "部署が更新されました！");
            }

            closeAndNotify();

        } catch (Exception ex) {

            String msg = ex.getMessage();

            if (msg.contains("既に存在")) {
                // Duplicate name/code
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
        contentPanel.add(cbbProjectname);
        contentPanel.add(lbTaskname);
        contentPanel.add(cbbTaskname);
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
                                        .addComponent(cbbProjectname, 200, 200, 200)
                                        .addComponent(cbbTaskname, 200, 200, 200)))
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
                                .addComponent(cbbProjectname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                        .addGap(25)
                        .addGroup(layout.createParallelGroup()
                                .addComponent(lbTaskname)
                                .addComponent(cbbTaskname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
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

        cbbProjectname = new JComboBox<>();
        cbbTaskname = new JComboBox<>();

        btnSave = new JButton("保存");

        setBackground(Color.WHITE);
    }
}
