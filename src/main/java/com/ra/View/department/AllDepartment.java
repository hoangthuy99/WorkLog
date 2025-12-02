package com.ra.View.department;

import com.ra.Controller.DepartmentController;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class AllDepartment extends JPanel {

    private final DepartmentController departmentController = new DepartmentController();

    public AllDepartment() {
        initComponents();
        loadTable(departmentController.findAll());
    }

    private void initComponents() {

        lbKeyword = new JLabel();
        btnSearch = new JButton();
        btnAddDepartment = new JButton();
        txtSearch = new JTextField();
        scrAdddepartment = new JScrollPane();
        tblAddDepartment = new JTable();
        btnEdit = new JButton();
        btnDelete = new JButton();

        lbKeyword.setText("キーワード");

        btnSearch.setText("検索");
        btnSearch.addActionListener(this::btnSearchActionPerformed);

        btnAddDepartment.setText("部署作成");
        btnAddDepartment.addActionListener(e -> openAddDepartment());

        tblAddDepartment.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "部署名", "プロジェクト名", "タスク名"}
        ));

        scrAdddepartment.setViewportView(tblAddDepartment);

        tblAddDepartment.getColumnModel().getColumn(0).setMinWidth(0);
        tblAddDepartment.getColumnModel().getColumn(0).setMaxWidth(0);

        btnEdit.setText("編集");
        btnEdit.addActionListener(e -> editDepartment());

        btnDelete.setText("削除");
        btnDelete.addActionListener(e -> deleteDepartment());

        // LAYOUT GIỮ NGUYÊN
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(40)
                                .addComponent(lbKeyword)
                                .addGap(18)
                                .addComponent(txtSearch, 100, 100, 100)
                                .addGap(18)
                                .addComponent(btnSearch)
                                .addGap(18)
                                .addComponent(btnAddDepartment)
                                .addContainerGap(40, Short.MAX_VALUE))
                        .addGroup(layout.createSequentialGroup()
                                .addGap(40)
                                .addComponent(scrAdddepartment, 480, 480, 480)
                                .addGap(30)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addComponent(btnEdit, 80, 80, 80)
                                        .addComponent(btnDelete, 80, 80, 80))
                                .addContainerGap(40, Short.MAX_VALUE))
        );

        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(30)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbKeyword)
                                        .addComponent(txtSearch)
                                        .addComponent(btnSearch)
                                        .addComponent(btnAddDepartment))
                                .addGap(20)
                                .addGroup(layout.createParallelGroup()
                                        .addComponent(scrAdddepartment, 300, 300, 300)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(btnEdit)
                                                .addGap(20)
                                                .addComponent(btnDelete)))
                                .addGap(30))
        );
    }

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {
        String keyword = txtSearch.getText().trim();
        loadTable(departmentController.search(keyword));
    }

    private void openAddDepartment() {
        AddDepartment form = new AddDepartment(null, true);
        form.showDialog();
        loadTable(departmentController.findAll());
    }

    private void editDepartment() {
        int row = tblAddDepartment.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "行を選択してください！");
            return;
        }

        int id = (int) tblAddDepartment.getValueAt(row, 0);

        AddDepartment form = new AddDepartment(null, true, id);
        form.showDialog();
        loadTable(departmentController.findAll());
    }

    private void deleteDepartment() {
        int row = tblAddDepartment.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "削除する行を選択してください！");
            return;
        }

        int id = (int) tblAddDepartment.getValueAt(row, 0);

        if (JOptionPane.showConfirmDialog(this, "削除しますか？",
                "確認", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {

            departmentController.delete(id);
            loadTable(departmentController.findAll());
        }
    }

    private void loadTable(List<Department> list) {
        DefaultTableModel model = (DefaultTableModel) tblAddDepartment.getModel();
        model.setRowCount(0);

        for (Department d : list) {

            String projectNames = (d.getProjects() == null) ? "" :
                    d.getProjects().stream().map(Project::getName)
                            .reduce("", (a, b) -> a + (a.isEmpty() ? "" : ", ") + b);

            String taskNames = (d.getTasks() == null) ? "" :
                    d.getTasks().stream().map(Tasks::getName)
                            .reduce("", (a, b) -> a + (a.isEmpty() ? "" : ", ") + b);

            model.addRow(new Object[]{
                    d.getId(),
                    d.getName(),
                    projectNames,
                    taskNames
            });
        }
    }

    /** MAIN GIỮ NGUYÊN */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            JFrame f = new JFrame("All Departments");
            f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            f.setContentPane(new AllDepartment());
            f.pack();
            f.setLocationRelativeTo(null);
            f.setVisible(true);
        });
    }


    private JButton btnAddDepartment;
    private JButton btnDelete;
    private JButton btnEdit;
    private JButton btnSearch;
    private JLabel lbKeyword;
    private JScrollPane scrAdddepartment;
    private JTable tblAddDepartment;
    private JTextField txtSearch;
}
