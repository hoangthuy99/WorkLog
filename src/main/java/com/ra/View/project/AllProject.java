package com.ra.View.project;

import com.ra.Controller.ProjectController;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AllProject extends JPanel {

    private final ProjectController projectController = new ProjectController();

    private JPanel contentPanel;

    public AllProject() {
        initComponents();
        applyCenteredLayout();

        loadTable(projectController.findAll());
        setupEvent();
    }

    private void setupEvent() {

        btnSearch.addActionListener(e -> {
            String keyword = txtProject.getText().trim();
            loadTable(projectController.search(keyword));
        });

        btnAll.addActionListener(e -> loadTable(projectController.findAll()));

        btnCreateDepartment.addActionListener(e -> openProjectForm(null));

        btnEdit.addActionListener(e -> {
            int row = tblAllProject.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "編集するプロジェクトを選択してください。");
                return;
            }

            int projectId = (int) tblAllProject.getValueAt(row, 4); // cột 3 = hidden id
            openProjectForm(projectId);
        });

        btnDelete.addActionListener(e -> {
            int row = tblAllProject.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "削除するプロジェクトを選択してください。");
                return;
            }

            int projectId = (int) tblAllProject.getValueAt(row, 4);

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "このプロジェクトを削除しますか？\n",
                    "確認",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm != JOptionPane.YES_OPTION) return;

            try {
                boolean success = projectController.delete(projectId);

                if (success) {
                    JOptionPane.showMessageDialog(
                            this,
                            "プロジェクトを削除しました。",
                            "完了",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    loadTable(projectController.findAll());
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "削除に失敗しました。",
                            "エラー",
                            JOptionPane.ERROR_MESSAGE
                    );
                }

            } catch (Exception ex) {

                String msg = ex.getMessage();

                // Nếu lỗi do đang dùng trong WorkRecord
                if (msg != null && msg.contains("使用されている")) {
                    JOptionPane.showMessageDialog(
                            this,
                            "このプロジェクトは勤務記録で使用されているため、削除できません。",
                            "削除エラー",
                            JOptionPane.ERROR_MESSAGE
                    );
                } else {
                    JOptionPane.showMessageDialog(
                            this,
                            "削除中に予期しないエラーが発生しました。\n" + msg,
                            "エラー",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            }
        });

    }

    private void openProjectForm(Integer projectId) {
        JFrame frame = new JFrame(projectId == null ? "プロジェクト作成" : "プロジェクト編集");
        if (projectId == null) {
            frame.add(new AddProject());
        } else {
            frame.add(new AddProject(projectId));
        }
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                loadTable(projectController.findAll());
            }

            @Override
            public void windowClosing(java.awt.event.WindowEvent e) {
                loadTable(projectController.findAll());
            }
        });
    }
    private void loadTable(List<Project> list) {
        String[] columns = {"No.", "プロジェクト名", "部署名", "タスク名", "ID(hidden)"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);
        int no = 1;
        for (Project p : list) {

            // Department names
            String depNames = "";
            if (p.getDepartments() != null) {
                depNames = p.getDepartments()
                        .stream()
                        .map(Department::getName)
                        .reduce("", (a, b) -> a + (a.isEmpty() ? "" : ", ") + b);
            }
            // Task names
            String taskNames = "";
            if (p.getTasks() != null) {
                taskNames = p.getTasks()
                        .stream()
                        .map(Tasks::getName)
                        .reduce("", (a, b) -> a + (a.isEmpty() ? "" : ", ") + b);
            }

            model.addRow(new Object[]{
                    no++,
                    p.getName(),
                    depNames,
                    taskNames,
                    p.getId()
            });
        }

        tblAllProject.setModel(model);
        tblAllProject.getColumnModel().getColumn(4).setMinWidth(0);
        tblAllProject.getColumnModel().getColumn(4).setMaxWidth(0);
    }
    private void applyCenteredLayout() {
        contentPanel = new JPanel();
        contentPanel.setBackground(new Color(255, 255, 255));
        GroupLayout contentLayout = new GroupLayout(contentPanel);
        contentPanel.setLayout(contentLayout);
        this.removeAll();
        contentPanel.add(txtProject);
        contentPanel.add(btnSearch);
        contentPanel.add(btnAll);
        contentPanel.add(btnCreateDepartment);
        contentPanel.add(jScrollPane1);
        contentPanel.add(btnEdit);
        contentPanel.add(btnDelete);
        contentPanel.add(lbProjectName);

        contentLayout.setHorizontalGroup(
                contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(contentLayout.createSequentialGroup()
                                .addGap(32)
                                .addComponent(lbProjectName)
                                .addGap(10)
                                .addComponent(txtProject, 130, 130, 130)
                                .addGap(10)
                                .addComponent(btnSearch)
                                .addGap(10)
                                .addComponent(btnAll)
                                .addGap(20)
                                .addComponent(btnCreateDepartment)
                                .addContainerGap())
                        .addGroup(contentLayout.createSequentialGroup()
                                .addGap(20)
                                .addComponent(jScrollPane1, 560, 560, 560)
                                .addContainerGap())
                        .addGroup(GroupLayout.Alignment.TRAILING,
                                contentLayout.createSequentialGroup()
                                        .addContainerGap(300, Short.MAX_VALUE)
                                        .addComponent(btnEdit)
                                        .addGap(10)
                                        .addComponent(btnDelete)
                                        .addGap(30))
        );

        contentLayout.setVerticalGroup(
                contentLayout.createSequentialGroup()
                        .addGap(40)
                        .addGroup(contentLayout.createParallelGroup()
                                .addComponent(lbProjectName)
                                .addComponent(txtProject, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnSearch)
                                .addComponent(btnAll)
                                .addComponent(btnCreateDepartment))
                        .addGap(20)
                        .addComponent(jScrollPane1, 200, 200, 200)
                        .addGap(20)
                        .addGroup(contentLayout.createParallelGroup()
                                .addComponent(btnEdit)
                                .addComponent(btnDelete))
        );

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        this.add(contentPanel, gbc);
        this.revalidate();
        this.repaint();
    }

    private JTextField txtProject;
    private JButton btnSearch;
    private JButton btnAll;
    private JButton btnCreateDepartment;
    private JScrollPane jScrollPane1;
    private JTable tblAllProject = new JTable();
    private JButton btnEdit;
    private JButton btnDelete;
    private JLabel lbProjectName;

    @SuppressWarnings("unchecked")
    private void initComponents() {

        txtProject = new JTextField();
        btnSearch = new JButton("検索");
        btnAll = new JButton("全て");
        btnCreateDepartment = new JButton("プロジェクトの作成が完了しました");

        jScrollPane1 = new JScrollPane();
        tblAllProject = new JTable();
        jScrollPane1.setViewportView(tblAllProject);

        btnEdit = new JButton("編集");
        btnDelete = new JButton("削除");

        lbProjectName = new JLabel("キーワード");

        this.setLayout(null);
    }
}
