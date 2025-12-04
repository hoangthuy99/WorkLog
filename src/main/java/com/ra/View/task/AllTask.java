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
        btnEdit.addActionListener(this::btnEditActionPerformed);
        btnDelete.addActionListener(this::btnDeleteActionPerformed);


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

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {

        int row = tblAddtask.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "タスクを選択してください！");
            return;
        }

        int id = Integer.parseInt(tblAddtask.getValueAt(row, 0).toString());

        taskController.findById(id).ifPresentOrElse(task -> {
            AddTask dialog = new AddTask(null, true, task);
            dialog.setVisible(true);

            loadTable(taskController.findAll());

        }, () -> JOptionPane.showMessageDialog(this, "Task not found!"));
    }


    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {

        int row = tblAddtask.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "タスクを選択してください！");
            return;
        }

        int id = Integer.parseInt(tblAddtask.getValueAt(row, 0).toString());

        if (JOptionPane.showConfirmDialog(this, "削除しますか？") == JOptionPane.YES_OPTION) {

            if (taskController.delete(id)) {
                JOptionPane.showMessageDialog(this, "削除完了！");
                loadTable(taskController.findAll());
            } else {
                JOptionPane.showMessageDialog(this, "削除失敗！");
            }
        }
    }


    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {
        String keyword = txtSearch.getText().trim();

        if (keyword.isEmpty()) {
            loadTable(taskController.findAll());
            return;
        }

        List<Tasks> results = taskController.search(keyword);

        if (results.isEmpty()) {
            JOptionPane.showMessageDialog(this, "検索結果がありません！");
        }

        loadTable(results);
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

        String[] cols = {"ID", "タスク名", "プロジェクト名", "部署名"};
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        for (Tasks t : list) {

            String projectNames = t.getProjects() == null ? "" :
                    t.getProjects().stream().map(Project::getName)
                            .reduce("", (a, b) -> a + (a.isEmpty() ? "" : ", ") + b);

            String departmentNames = t.getDepartments() == null ? "" :
                    t.getDepartments().stream().map(Department::getName)
                            .reduce("", (a, b) -> a + (a.isEmpty() ? "" : ", ") + b);

            model.addRow(new Object[]{
                    t.getId(),             // ⭐ Cột 0 BẮT BUỘC là ID
                    t.getName(),
                    projectNames,
                    departmentNames
            });
        }

        tblAddtask.setModel(model);

        // Ẩn cột ID
        tblAddtask.getColumnModel().getColumn(0).setMinWidth(0);
        tblAddtask.getColumnModel().getColumn(0).setMaxWidth(0);
        tblAddtask.getColumnModel().getColumn(0).setWidth(0);
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
