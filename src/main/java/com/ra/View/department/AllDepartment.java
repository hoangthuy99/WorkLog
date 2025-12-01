package com.ra.View.department;

import com.ra.Controller.DepartmentController;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class AllDepartment extends javax.swing.JDialog {

    private final DepartmentController departmentController = new DepartmentController();

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(AllDepartment.class.getName());

    public AllDepartment(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        loadTable(departmentController.findAll());
    }

    @SuppressWarnings("unchecked")
    private void initComponents() {

        lbKeyword = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        btnAddDepartment = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        scrAdddepartment = new javax.swing.JScrollPane();
        tblAddDepartment = new javax.swing.JTable();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        lbKeyword.setText("キーワード");

        btnSearch.setText("検索");
        btnSearch.addActionListener(this::btnSearchActionPerformed);

        btnAddDepartment.setText("部署作成");
        btnAddDepartment.addActionListener(this::btnAddDepartmentActionPerformed);

        tblAddDepartment.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                        "部署名", "プロジェクト名", "タスク名"
                }
        ));
        scrAdddepartment.setViewportView(tblAddDepartment);

        btnEdit.setText("編集");
        btnDelete.setText("削除");

        // Layout
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(40)
                                .addComponent(lbKeyword)
                                .addGap(18)
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18)
                                .addComponent(btnSearch)
                                .addGap(18)
                                .addComponent(btnAddDepartment)
                                .addContainerGap(40, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(40)
                                .addComponent(scrAdddepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(40, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(30)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbKeyword)
                                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSearch)
                                        .addComponent(btnAddDepartment))
                                .addGap(20)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(scrAdddepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnEdit)
                                                .addGap(20)
                                                .addComponent(btnDelete)))
                                .addContainerGap(30, Short.MAX_VALUE))
        );

        pack();
    }


    // =================== HANDLER =================== //

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {
        String keyword = txtSearch.getText().trim();
        List<Department> list = departmentController.search(keyword);
        loadTable(list);
    }

    private void btnAddDepartmentActionPerformed(java.awt.event.ActionEvent evt) {
        AddDepartment dialog = new AddDepartment(null, true);
        dialog.setVisible(true);

        loadTable(departmentController.findAll()); // reload
    }


    // =================== LOAD TABLE =================== //

    private void loadTable(List<Department> list) {

        String[] columns = {"部署名", "プロジェクト名", "タスク名"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Department d : list) {

            // Project names
            String projectNames = "";
            if (d.getProjects() != null) {
                projectNames = d.getProjects().stream()
                        .map(Project::getName)
                        .reduce("", (a, b) -> a + (a.isEmpty() ? "" : ", ") + b);
            }

            // Task names
            String taskNames = "";
            if (d.getTasks() != null) {
                taskNames = d.getTasks().stream()
                        .map(Tasks::getName)
                        .reduce("", (a, b) -> a + (a.isEmpty() ? "" : ", ") + b);
            }

            model.addRow(new Object[]{
                    d.getName(),
                    projectNames,
                    taskNames
            });
        }

        tblAddDepartment.setModel(model);
    }


    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            AllDepartment dialog = new AllDepartment(new javax.swing.JFrame(), true);
            dialog.setVisible(true);
        });
    }

    private javax.swing.JButton btnAddDepartment;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSearch;
    private javax.swing.JLabel lbKeyword;
    private javax.swing.JScrollPane scrAdddepartment;
    private javax.swing.JTable tblAddDepartment;
    private javax.swing.JTextField txtSearch;
}
