package com.ra.View.department;

import com.ra.Controller.DepartmentController;
import com.ra.Controller.ProjectController;
import com.ra.Controller.TaskController;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AddDepartment extends JPanel {

    private final DepartmentController departmentController = new DepartmentController();
    private final ProjectController projectController = new ProjectController();
    private final TaskController taskController = new TaskController();

    private Integer editingDepartmentId = null;
    private Department editingDepartment;

    private JDialog parentDialog;  // NEW

    /** ---------- MODE: ADD ---------- */
    public AddDepartment(Frame parent, boolean modal) {
        this.parentDialog = createDialog(parent, modal);
        initComponents();
        loadComboBoxes();
        parentDialog.setTitle("部署作成");
    }

    /** ---------- MODE: EDIT ---------- */
    public AddDepartment(Frame parent, boolean modal, int id) {
        this.editingDepartmentId = id;
        this.parentDialog = createDialog(parent, modal);
        initComponents();
        loadComboBoxes();
        loadDepartmentData();
        parentDialog.setTitle("部署編集");
    }

    /** Tạo JDialog chứa JPanel này */
    private JDialog createDialog(Frame parent, boolean modal) {
        JDialog d = new JDialog(parent, modal);
        d.setContentPane(this);

        // 💥 THÊM 2 DÒNG NÀY
        this.setPreferredSize(new Dimension(500, 350));  // Panel size
        d.setMinimumSize(new Dimension(520, 380));        // Dialog size

        d.pack();
        d.setLocationRelativeTo(null);
        return d;
    }

    /** SHOW FORM (thay thế setVisible của JDialog cũ) */
    public void showDialog() {
        parentDialog.setVisible(true);
    }

    private void loadComboBoxes() {
        cbProject.removeAllItems();
        cbTask.removeAllItems();

        List<Project> projects = projectController.findAll();
        for (Project p : projects) cbProject.addItem(p);

        List<Tasks> tasks = taskController.findAll();
        for (Tasks t : tasks) cbTask.addItem(t);
    }

    private void loadDepartmentData() {
        editingDepartment = departmentController.findAll()
                .stream().filter(d -> d.getId() == editingDepartmentId)
                .findFirst().orElse(null);

        if (editingDepartment == null) {
            JOptionPane.showMessageDialog(this, "部署データ取得失敗！");
            parentDialog.dispose();
            return;
        }

        txtDepartmentName.setText(editingDepartment.getName());

        if (!editingDepartment.getProjects().isEmpty())
            cbProject.setSelectedItem(editingDepartment.getProjects().get(0));

        if (!editingDepartment.getTasks().isEmpty())
            cbTask.setSelectedItem(editingDepartment.getTasks().get(0));

        btnSave.setText("更新");
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        pnlMain = new javax.swing.JPanel();
        lbDepartmentName = new javax.swing.JLabel();
        lbProject = new javax.swing.JLabel();
        lbTask = new javax.swing.JLabel();
        txtDepartmentName = new javax.swing.JTextField();
        cbProject = new javax.swing.JComboBox<>();
        cbTask = new javax.swing.JComboBox<>();
        btnCancel = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();

        lbDepartmentName.setText("部署名");
        lbProject.setText("プロジェクト名");
        lbTask.setText("タスク名");

        btnCancel.setBackground(new java.awt.Color(255, 204, 204));
        btnCancel.setText("キャンセル");
        btnCancel.addActionListener(evt -> parentDialog.dispose()); // FIX

        btnSave.setBackground(new java.awt.Color(204, 204, 255));
        btnSave.setText("保存");
        btnSave.addActionListener(evt -> saveDepartment());

        javax.swing.GroupLayout pnlMainLayout = new javax.swing.GroupLayout(pnlMain);
        pnlMain.setLayout(pnlMainLayout);
        pnlMainLayout.setHorizontalGroup(
                pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlMainLayout.createSequentialGroup()
                                .addGap(40)
                                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(lbDepartmentName)
                                        .addComponent(lbProject)
                                        .addComponent(lbTask))
                                .addGap(20)
                                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtDepartmentName)
                                        .addComponent(cbProject, 0, 300, Short.MAX_VALUE)
                                        .addComponent(cbTask, 0, 300, Short.MAX_VALUE))
                                .addContainerGap(40, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMainLayout.createSequentialGroup()
                                .addContainerGap(150, Short.MAX_VALUE)
                                .addComponent(btnCancel)
                                .addGap(40)
                                .addComponent(btnSave)
                                .addGap(120))
        );
        pnlMainLayout.setVerticalGroup(
                pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlMainLayout.createSequentialGroup()
                                .addGap(40)
                                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbDepartmentName)
                                        .addComponent(txtDepartmentName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20)
                                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbProject)
                                        .addComponent(cbProject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(20)
                                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbTask)
                                        .addComponent(cbTask, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(30)
                                .addGroup(pnlMainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(40, Short.MAX_VALUE))
        );

        setLayout(new BorderLayout());
        add(pnlMain, BorderLayout.CENTER);
    }

    private void saveDepartment() {
        String name = txtDepartmentName.getText().trim();
        if (name.isEmpty()) {
            JOptionPane.showMessageDialog(this, "部署名を入力してください");
            return;
        }

        Project project = (Project) cbProject.getSelectedItem();
        Tasks task = (Tasks) cbTask.getSelectedItem();

        if (editingDepartmentId == null) {

            Department newDept = new Department();
            newDept.setName(name);
            newDept.setDepartmentCode(Department.generateDepartmentCode());
            newDept.setProjects(List.of(project));
            newDept.setTasks(List.of(task));

            departmentController.create(newDept);
            JOptionPane.showMessageDialog(this, "作成完了");

        } else {
            editingDepartment.setName(name);
            editingDepartment.setProjects(List.of(project));
            editingDepartment.setTasks(List.of(task));

            departmentController.update(editingDepartment);
            JOptionPane.showMessageDialog(this, "更新完了");
        }

        parentDialog.dispose(); // FIX
    }

    /** MAIN giữ nguyên */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            AddDepartment dialog = new AddDepartment(null, true);
            dialog.showDialog();
        });
    }

    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnSave;
    private javax.swing.JComboBox<Project> cbProject;
    private javax.swing.JComboBox<Tasks> cbTask;
    private javax.swing.JLabel lbDepartmentName;
    private javax.swing.JLabel lbProject;
    private javax.swing.JLabel lbTask;
    private javax.swing.JPanel pnlMain;
    private javax.swing.JTextField txtDepartmentName;
}
