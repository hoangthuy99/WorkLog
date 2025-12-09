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
    private JComboBox<Project> cbProjectname;
    private JComboBox<Department> cbDepartmentname;
    private JLabel lbProjectname;
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

        // ===== DEPARTMENT =====
        Department selectedDept = null;
        if (editingTask.getDepartments() != null && !editingTask.getDepartments().isEmpty()) {
            int editDeptId = editingTask.getDepartments().get(0).getId();

            // tìm trong departmentList cái Department có cùng id
            for (Department d : departmentList) {
                if (d.getId() == editDeptId) {
                    selectedDept = d;
                    break;
                }
            }
        }
        cbDepartmentname.setSelectedItem(selectedDept); // nếu không có thì sẽ là null → 未選択

        // ===== PROJECT =====
        Project selectedProj = null;
        if (editingTask.getProjects() != null && !editingTask.getProjects().isEmpty()) {
            int editProjId = editingTask.getProjects().get(0).getId();

            for (Project p : projectList) {
                if (p.getId() == editProjId) {
                    selectedProj = p;
                    break;
                }
            }
        }
        cbProjectname.setSelectedItem(selectedProj);

        btnSave.setText("更新");
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
        cbDepartmentname.addItem(null);

        for (Department d : departmentList) {
            cbDepartmentname.addItem(d);
        }

        cbDepartmentname.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("未選択");   // "chưa chọn"
                } else {
                    setText(((Department) value).getName());
                }
                return this;
            }
        });
    }


    private void loadProjects() {
        projectList = projectController.findAll();
        cbProjectname.removeAllItems();

        // ✅ Cho phép "không chọn"
        cbProjectname.addItem(null);

        for (Project p : projectList) {
            cbProjectname.addItem(p);
        }

        // ✅ Renderer hiển thị khi null
        cbProjectname.setRenderer(new DefaultListCellRenderer() {
            @Override
            public java.awt.Component getListCellRendererComponent(
                    JList<?> list, Object value, int index,
                    boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("未選択");
                } else {
                    setText(((Project) value).getName());
                }
                return this;
            }
        });
    }


    // ==========================================================
    // BUTTON HANDLERS
    // ==========================================================


    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {

        String taskName = txtTaskname.getText().trim();
        if (taskName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "タスク名を空にすることはできません！");
            return;
        }

        Department selectedDept = (Department) cbDepartmentname.getSelectedItem();
        Project selectedProj = (Project) cbProjectname.getSelectedItem();

        List<Department> deps = new ArrayList<>();
        if (selectedDept != null) {
            deps.add(selectedDept);
        }

        List<Project> projs = new ArrayList<>();
        if (selectedProj != null) {
            projs.add(selectedProj);
        }


        try {

            // === EDIT MODE ===
            if (editingTask != null) {
                editingTask.setName(taskName);
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

    // ==========================================================
    // AUTO GENERATED UI
    // ==========================================================
    @SuppressWarnings("unchecked")
    private void initComponents() {

        cbProjectname = new JComboBox<>();
        lbProjectname = new JLabel();
        btnSave = new JButton();
        txtTaskname = new JTextField();
        lbTaskname = new JLabel();
        lbDepartmentname = new JLabel();
        cbDepartmentname = new JComboBox<>();

        setBackground(new java.awt.Color(255, 255, 255));

        lbProjectname.setText("プロジェクト名");




        btnSave.setBackground(new java.awt.Color(102, 255, 102));
        btnSave.setText("保存");

        lbTaskname.setText("タスク名");
        lbDepartmentname.setText("部署名");


    }
}
