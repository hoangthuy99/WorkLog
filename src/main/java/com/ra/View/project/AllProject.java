package com.ra.View.project;

import com.ra.Controller.ProjectController;
import com.ra.Model.Entity.Project;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import java.util.logging.Logger;

public class AllProject extends javax.swing.JFrame {

    private static final Logger logger = Logger.getLogger(AllProject.class.getName());

    private final ProjectController projectController = new ProjectController();
    private DefaultTableModel model;

    public AllProject() {
        initComponents();
        setupTable();
        loadTable(projectController.findAll()); // load dự án ban đầu
    }

    /** Setup bảng */
    private void setupTable() {
        model = new DefaultTableModel(
                new Object[]{"STT", "ID", "プロジェクト名", "部署名", "タスク名"}, 0
        );
        tblAllDepartment.setModel(model);

        // Ẩn cột ID
        tblAllDepartment.getColumnModel().getColumn(1).setMinWidth(0);
        tblAllDepartment.getColumnModel().getColumn(1).setMaxWidth(0);

        // Căn giữa cột STT
        tblAllDepartment.getColumnModel().getColumn(0).setPreferredWidth(50);
    }


    /** Load dữ liệu lên bảng */
    private void loadTable(List<Project> list) {
        model.setRowCount(0);

        int stt = 1;

        for (Project p : list) {

            String deptNames = "";
            if (p.getDepartments() != null) {
                deptNames = p.getDepartments()
                        .stream()
                        .map(d -> d.getName())
                        .reduce("", (a, b) -> a + " " + b);
            }

            String taskNames = "";
            if (p.getTasks() != null) {
                taskNames = p.getTasks()
                        .stream()
                        .map(t -> t.getName())
                        .reduce("", (a, b) -> a + " " + b);
            }

            model.addRow(new Object[]{
                    stt++,                  // <---- SỐ THỨ TỰ
                    p.getId(),
                    p.getName(),
                    deptNames.trim(),
                    taskNames.trim()
            });
        }
    }



    @SuppressWarnings("unchecked")
    private void initComponents() {

        pnlAllProject = new javax.swing.JPanel();
        lbProjectName = new javax.swing.JLabel();
        txtProject = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnAll = new javax.swing.JButton();
        btnCreateProject = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAllDepartment = new javax.swing.JTable();
        jSpinner1 = new javax.swing.JSpinner();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        lbProjectName.setText("キーワード");

        // ---------- SEARCH ----------
        btnSearch.setText("検索");
        btnSearch.addActionListener(e -> {
            String keyword = txtProject.getText().trim();
            List<Project> result = projectController.search(keyword);
            loadTable(result);
        });

        // ---------- LOAD ALL ----------
        btnAll.setText("全て");
        btnAll.addActionListener(e -> loadTable(projectController.findAll()));

        // ---------- CREATE ----------
        btnCreateProject.setText("プロジェクト作成");
        btnCreateProject.addActionListener(e -> new AddProject().setVisible(true));

        tblAllDepartment.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                        "Title 1", "Title 2", "Title 3", "Title 4"
                }
        ));
        jScrollPane1.setViewportView(tblAllDepartment);

        // ---------- EDIT ----------
        btnEdit.setBackground(new java.awt.Color(204, 255, 255));
        btnEdit.setText("編集");
        btnEdit.addActionListener(e -> {

            int row = tblAllDepartment.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "行を選択してください");
                return;
            }

            int id = (int) model.getValueAt(row, 0);

            // mở AddProject ở chế độ EDIT
            AddProject editForm = new AddProject(id);
            editForm.setVisible(true);
        });

        // ---------- DELETE ----------
        btnDelete.setBackground(new java.awt.Color(255, 204, 204));
        btnDelete.setText("削除");
        btnDelete.addActionListener(e -> {

            int row = tblAllDepartment.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "行を選択してください");
                return;
            }

            int id = (int) model.getValueAt(row, 0);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "削除しますか？",
                    "確認",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                if (projectController.delete(id)) {
                    JOptionPane.showMessageDialog(this, "削除完了");
                    loadTable(projectController.findAll());
                } else {
                    JOptionPane.showMessageDialog(this, "削除失敗");
                }
            }
        });


        // ---------- GIỮ NGUYÊN LAYOUT CỦA BẠN ----------
        javax.swing.GroupLayout pnlAllProjectLayout = new javax.swing.GroupLayout(pnlAllProject);
        pnlAllProject.setLayout(pnlAllProjectLayout);
        pnlAllProjectLayout.setHorizontalGroup(
                pnlAllProjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAllProjectLayout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(lbProjectName)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtProject, javax.swing.GroupLayout.PREFERRED_SIZE, 134, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnAll, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                                .addComponent(btnCreateProject, javax.swing.GroupLayout.PREFERRED_SIZE, 135, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAllProjectLayout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30)
                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap())
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAllProjectLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(jScrollPane1)
                                .addContainerGap())
        );
        pnlAllProjectLayout.setVerticalGroup(
                pnlAllProjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlAllProjectLayout.createSequentialGroup()
                                .addGap(65, 65, 65)
                                .addGroup(pnlAllProjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbProjectName)
                                        .addComponent(txtProject, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSearch)
                                        .addComponent(btnAll)
                                        .addComponent(btnCreateProject))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 43, Short.MAX_VALUE)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 205, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(27, 27, 27)
                                .addGroup(pnlAllProjectLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jSpinner1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnEdit)
                                        .addComponent(btnDelete))
                                .addGap(15, 15, 15))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pnlAllProject, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(pnlAllProject, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }


    public static void main(String args[]) {
        EventQueue.invokeLater(() -> new AllProject().setVisible(true));
    }

    // Variables
    private javax.swing.JButton btnAll;
    private javax.swing.JButton btnCreateProject;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSearch;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSpinner jSpinner1;
    private javax.swing.JTable tblAllDepartment;
    private javax.swing.JTextField txtProject;
    private javax.swing.JPanel pnlAllProject;
    private javax.swing.JLabel lbProjectName;
}
