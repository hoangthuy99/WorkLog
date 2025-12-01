package com.ra.View.task;

import com.ra.Controller.TaskController;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class AllTask extends javax.swing.JDialog {

    private final TaskController taskController = new TaskController();

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(AllTask.class.getName());

    public AllTask(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        loadTable(taskController.findAll());   // tải tất cả task
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        scrAddtask = new javax.swing.JScrollPane();
        tblAddtask = new javax.swing.JTable();
        btnSearch = new javax.swing.JButton();
        btnAddTask = new javax.swing.JButton();
        lbKeywork = new javax.swing.JLabel();
        txtSearch = new javax.swing.JTextField();
        btnAll = new javax.swing.JButton();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        tblAddtask.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                        "タスク名", "プロジェクト名", "部署名"
                }
        ));
        scrAddtask.setViewportView(tblAddtask);

        btnSearch.setText("検索");
        btnSearch.addActionListener(this::btnSearchActionPerformed);

        btnAddTask.setText("タスク作成");
        btnAddTask.addActionListener(this::btnAddTaskActionPerformed);

        lbKeywork.setText("キーワード");

        btnAll.setText("全て");
        btnAll.addActionListener(this::btnAllActionPerformed);

        btnEdit.setText("編集");
        btnDelete.setText("削除");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(25)
                                .addComponent(lbKeywork)
                                .addGap(18)
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18)
                                .addComponent(btnSearch)
                                .addGap(18)
                                .addComponent(btnAll)
                                .addGap(18)
                                .addComponent(btnAddTask, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(40, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(25)
                                .addComponent(scrAddtask, javax.swing.GroupLayout.PREFERRED_SIZE, 520, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(25)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(25))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(26)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnAddTask)
                                        .addComponent(lbKeywork)
                                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSearch)
                                        .addComponent(btnAll))
                                .addGap(26)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(scrAddtask, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnEdit)
                                                .addGap(20)
                                                .addComponent(btnDelete)))
                                .addGap(26))
        );

        pack();
    }

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {
        String kw = txtSearch.getText().trim();
        loadTable(taskController.search(kw));
    }

    private void btnAddTaskActionPerformed(java.awt.event.ActionEvent evt) {
        AddTask dialog = new AddTask(null, true);
        dialog.setVisible(true);

        loadTable(taskController.findAll()); // reload
    }

    private void btnAllActionPerformed(java.awt.event.ActionEvent evt) {
        loadTable(taskController.findAll());
    }

    private void loadTable(List<Tasks> list) {

        String[] cols = {"タスク名", "プロジェクト名", "部署名"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (Tasks t : list) {

            // Nhiều projects
            String projectNames = "";
            if (t.getProjects() != null) {
                projectNames = t.getProjects()
                        .stream()
                        .map(Project::getName)
                        .reduce("", (a, b) -> a + (a.isEmpty() ? "" : ", ") + b);
            }

            // Nhiều departments
            String departmentNames = "";
            if (t.getDepartments() != null) {
                departmentNames = t.getDepartments()
                        .stream()
                        .map(Department::getName)
                        .reduce("", (a, b) -> a + (a.isEmpty() ? "" : ", ") + b);
            }

            model.addRow(new Object[]{
                    t.getName(),
                    projectNames,
                    departmentNames,
                    "編集",
                    "削除"
            });
        }

        tblAddtask.setModel(model);
    }

    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            AllTask dialog = new AllTask(new javax.swing.JFrame(), true);
            dialog.setVisible(true);
        });
    }

    private javax.swing.JButton btnAddTask;
    private javax.swing.JButton btnAll;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSearch;
    private javax.swing.JLabel lbKeywork;
    private javax.swing.JScrollPane scrAddtask;
    private javax.swing.JTable tblAddtask;
    private javax.swing.JTextField txtSearch;
}
