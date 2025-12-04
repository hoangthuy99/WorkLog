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
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Window;
import java.util.logging.Logger;

public class AddDepartment extends JPanel {

    // Interface để thông báo cho màn hình cha khi thao tác hoàn tất
    public interface DepartmentListener {
        void onDepartmentOperationComplete();
    }

    // Khai báo Listener và ID
    private DepartmentListener listener;
    private Integer editingDepartmentId = null;
    private Department editingDepartment = null;
    private Window parentWindow;

    private static final Logger logger = Logger.getLogger(AddDepartment.class.getName());

    private final DepartmentController departmentController = new DepartmentController();
    private final ProjectController projectController = new ProjectController();
    private final TaskController taskController = new TaskController();

    private List<Project> projectList = new ArrayList<>();
    private List<Tasks> taskList = new ArrayList<>();

    private JPanel contentPanel;

    // 🌟 CONSTRUCTOR 1: Dành cho MainDashboard gọi (new AddDepartment()) - CHẾ ĐỘ NHÚNG
    public AddDepartment() {
        this.listener = null;
        this.parentWindow = null;
        this.editingDepartmentId = null;

        initializeForm(null);
        btnSave.setText("保存");
    }

    // 🌟 CONSTRUCTOR 2: Dùng cho Pop-up Add/Edit (Được gọi từ AllDepartment) - CHẾ ĐỘ POP-UP
    public AddDepartment(Window parentWindow, DepartmentListener listener, Integer id) {
        this.parentWindow = parentWindow;
        this.listener = listener;
        this.editingDepartmentId = id;

        initializeForm(id);

        if (id != null) {
            btnSave.setText("更新");
        } else {
            btnSave.setText("保存");
        }
    }

    // PHƯƠNG THỨC KHỞI TẠO FORM (Logic chung)
    private void initializeForm(Integer id) {
        initComponents();
        applyCenteredLayout();

        loadProjects();
        loadTasks();

        // Load data nếu đang ở chế độ chỉnh sửa
        if (id != null) {
            loadDepartmentData(id);
        }

        btnCancel.addActionListener(this::btnCancelActionPerformed);
        btnSave.addActionListener(this::btnSaveActionPerformed);
    }

    // Tải dữ liệu cho chế độ EDIT
    private void loadDepartmentData(int id) {
        editingDepartment = departmentController.findById(id);

        if (editingDepartment != null) {
            txtDepartmentname.setText(editingDepartment.getName());

            // Xử lý Project
            if (editingDepartment.getProjects() != null && !editingDepartment.getProjects().isEmpty()) {
                String projectName = editingDepartment.getProjects().get(0).getName();
                cbbProjectname.setSelectedItem(projectName);
            } else {
                cbbProjectname.setSelectedIndex(0);
            }

            // Xử lý Task
            if (editingDepartment.getTasks() != null && !editingDepartment.getTasks().isEmpty()) {
                String taskName = editingDepartment.getTasks().get(0).getName();
                cbbTaskname.setSelectedItem(taskName);
            } else {
                cbbTaskname.setSelectedIndex(0);
            }
        } else {
            JOptionPane.showMessageDialog(this, "部署データが見つかりません！", "Error", JOptionPane.ERROR_MESSAGE);
            closeAndNotify();
        }
    }

    private void loadProjects() {
        projectList = projectController.findAll();
        cbbProjectname.removeAllItems();
        cbbProjectname.addItem("--- 選択してください ---");
        for (Project p : projectList) {
            cbbProjectname.addItem(p.getName());
        }
    }

    private void loadTasks() {
        taskList = taskController.findAll();
        cbbTaskname.removeAllItems();
        cbbTaskname.addItem("--- 選択してください ---");
        for (Tasks t : taskList) {
            cbbTaskname.addItem(t.getName());
        }
    }

    // Phương thức đóng cửa sổ và thông báo cho Listener
    private void closeAndNotify() {
        if (listener != null) {
            listener.onDepartmentOperationComplete();
        }
        if (parentWindow != null) {
            parentWindow.dispose();
        }
    }


    // ==========================================================
    // PHƯƠNG THỨC LAYOUT VÀ initComponents (GIỮ NGUYÊN DESIGN)
    // ==========================================================
    private void applyCenteredLayout() {
        contentPanel = new JPanel();
        contentPanel.setBackground(new java.awt.Color(255, 255, 255));

        GroupLayout contentLayout = new GroupLayout(contentPanel);
        contentPanel.setLayout(contentLayout);

        this.removeAll();

        contentPanel.add(lbDepartmentname);
        contentPanel.add(txtDepartmentname);
        contentPanel.add(lbProjectname);
        contentPanel.add(lbTaskname);
        contentPanel.add(cbbTaskname);
        contentPanel.add(cbbProjectname);
        contentPanel.add(btnCancel);
        contentPanel.add(btnSave);

        contentLayout.setHorizontalGroup(
                contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(contentLayout.createSequentialGroup()
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(contentLayout.createSequentialGroup()
                                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                                        .addGroup(contentLayout.createSequentialGroup()
                                                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                                        .addGroup(contentLayout.createSequentialGroup()
                                                                                .addContainerGap()
                                                                                .addComponent(lbTaskname, GroupLayout.PREFERRED_SIZE, 58, GroupLayout.PREFERRED_SIZE))
                                                                        .addGroup(GroupLayout.Alignment.LEADING, contentLayout.createSequentialGroup()
                                                                                .addGap(87, 87, 87)
                                                                                .addComponent(lbProjectname, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)))
                                                                .addGap(41, 41, 41))
                                                        .addGroup(GroupLayout.Alignment.TRAILING, contentLayout.createSequentialGroup()
                                                                .addComponent(lbDepartmentname, GroupLayout.PREFERRED_SIZE, 60, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(26, 26, 26)))
                                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(txtDepartmentname, GroupLayout.DEFAULT_SIZE, 214, Short.MAX_VALUE)
                                                        .addComponent(cbbTaskname, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(cbbProjectname, 0, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(contentLayout.createSequentialGroup()
                                                .addGap(153, 153, 153)
                                                .addComponent(btnCancel)
                                                .addGap(59, 59, 59)
                                                .addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(130, Short.MAX_VALUE))
        );
        contentLayout.setVerticalGroup(
                contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(contentLayout.createSequentialGroup()
                                .addGap(76, 76, 76)
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbDepartmentname)
                                        .addComponent(txtDepartmentname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbProjectname)
                                        .addComponent(cbbProjectname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbTaskname)
                                        .addComponent(cbbTaskname, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(32, 32, 32)
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCancel)
                                        .addComponent(btnSave))
                                .addContainerGap(93, Short.MAX_VALUE))
        );

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        this.add(contentPanel, gbc);
        this.revalidate();
        this.repaint();
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        lbDepartmentname = new JLabel();
        txtDepartmentname = new JTextField();
        lbProjectname = new JLabel();
        lbTaskname = new JLabel();
        cbbTaskname = new JComboBox<>();
        cbbProjectname = new JComboBox<>();
        btnCancel = new JButton();
        btnSave = new JButton();

        setBackground(new java.awt.Color(255, 255, 255));
        lbDepartmentname.setText("部署名");
        lbProjectname.setText("プロジェクト名");
        lbTaskname.setText("タスク名");
        btnCancel.setBackground(new java.awt.Color(255, 204, 0));
        btnCancel.setText("キャンセル");
        btnSave.setBackground(new java.awt.Color(153, 255, 0));
        btnSave.setText("保存");

        GroupLayout layout = new GroupLayout(this);
    }// </editor-fold>//GEN-END:initComponents

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {
        String depName = txtDepartmentname.getText().trim();

        if (depName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "部署名は空にできません！", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Project selectedProject = null;
        if (cbbProjectname.getSelectedIndex() > 0) {
            String projName = (String) cbbProjectname.getSelectedItem();
            selectedProject = projectList.stream()
                    .filter(p -> p.getName().equals(projName))
                    .findFirst().orElse(null);
        }

        Tasks selectedTask = null;
        if (cbbTaskname.getSelectedIndex() > 0) {
            String taskName = (String) cbbTaskname.getSelectedItem();
            selectedTask = taskList.stream()
                    .filter(t -> t.getName().equals(taskName))
                    .findFirst().orElse(null);
        }

        List<Project> projects = selectedProject != null ? List.of(selectedProject) : new ArrayList<>();
        List<Tasks> tasks = selectedTask != null ? List.of(selectedTask) : new ArrayList<>();

        if (editingDepartmentId == null) {
            // Logic Thêm mới
            Department d = new Department();
            d.setName(depName);
            d.setDepartmentCode(Department.generateDepartmentCode());
            d.setProjects(projects);
            d.setTasks(tasks);

            departmentController.create(d);
            JOptionPane.showMessageDialog(this, "部署が正常に作成されました！");
        } else {
            // Logic Cập nhật
            if (editingDepartment == null) {
                JOptionPane.showMessageDialog(this, "更新する部署データが見つかりません。", "Error", JOptionPane.ERROR_MESSAGE);
                closeAndNotify();
                return;
            }
            editingDepartment.setName(depName);
            editingDepartment.setProjects(projects);
            editingDepartment.setTasks(tasks);

            departmentController.update(editingDepartment);
            JOptionPane.showMessageDialog(this, "部署が正常に更新されました！");
        }

        closeAndNotify();
    }

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {
        closeAndNotify();
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnCancel;
    private JButton btnSave;
    private JComboBox<String> cbbProjectname;
    private JComboBox<String> cbbTaskname;
    private JLabel lbDepartmentname;
    private JLabel lbProjectname;
    private JLabel lbTaskname;
    private JTextField txtDepartmentname;
    // End of variables declaration//GEN-END:variables
}