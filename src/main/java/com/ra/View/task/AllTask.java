package com.ra.View.task;

import com.ra.Controller.TaskController;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.awt.*;
import java.util.Optional;
import java.util.logging.Logger;

public class AllTask extends JPanel {

    private final TaskController taskController = new TaskController();
    private static final Logger logger = Logger.getLogger(AllTask.class.getName());

    private JScrollPane scrAddtask;
    private JTable tblAddtask;
    private JButton btnSearch, btnAddTask, btnAll, btnEdit, btnDelete;
    private JLabel lbKeywork;
    private JTextField txtSearch;

    public AllTask() {
        initComponents();
        applyCenteredLayout();
        loadTable(taskController.findAll());
    }

    private void applyCenteredLayout() {

        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(Color.WHITE);

        GroupLayout layout = new GroupLayout(contentPanel);
        contentPanel.setLayout(layout);

        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);

        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                        .addGroup(
                                layout.createSequentialGroup()
                                        .addComponent(lbKeywork)
                                        .addComponent(txtSearch, 120, 120, 120)
                                        .addComponent(btnSearch)
                                        .addComponent(btnAll)
                                        .addGap(40)
                                        .addComponent(btnAddTask, 120, 120, 120)
                        )
                        .addComponent(scrAddtask, 550, 550, 550)
                        .addGroup(
                                layout.createSequentialGroup()
                                        .addGap(250)
                                        .addComponent(btnEdit, 80, 80, 80)
                                        .addComponent(btnDelete, 80, 80, 80)
                        )
        );

        layout.setVerticalGroup(
                layout.createSequentialGroup()
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(lbKeywork)
                                        .addComponent(txtSearch)
                                        .addComponent(btnSearch)
                                        .addComponent(btnAll)
                                        .addComponent(btnAddTask)
                        )
                        .addGap(20)
                        .addComponent(scrAddtask, 300, 300, 300)
                        .addGap(20)
                        .addGroup(
                                layout.createParallelGroup(GroupLayout.Alignment.CENTER)
                                        .addComponent(btnEdit)
                                        .addComponent(btnDelete)
                        )
        );

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.CENTER;

        this.add(contentPanel, gbc);
    }

    private void initComponents() {

        scrAddtask = new JScrollPane();
        tblAddtask = new JTable();
        btnSearch = new JButton("検索");
        btnAddTask = new JButton("タスク作成");
        lbKeywork = new JLabel("キーワード");
        txtSearch = new JTextField();
        btnAll = new JButton("全て");
        btnEdit = new JButton("編集");
        btnDelete = new JButton("削除");

        setBackground(Color.WHITE);

        tblAddtask.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{"No.","タスク名", "プロジェクト名", "部署名"}
        ));
        scrAddtask.setViewportView(tblAddtask);

        btnSearch.addActionListener(e -> loadTable(taskController.search(txtSearch.getText().trim())));
        btnAll.addActionListener(e -> loadTable(taskController.findAll()));

        txtSearch.addActionListener(e -> btnSearch.doClick());

        btnAddTask.addActionListener(e -> openAddForm());
        btnEdit.addActionListener(e -> openEditForm());
        btnDelete.addActionListener(e -> deleteTask());
    }

    private void openAddForm() {
        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);

        JDialog dialog = new JDialog(parent, "新タスク作成", true);
        dialog.setContentPane(new AddTask());
        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        loadTable(taskController.findAll());
    }

    private void openEditForm() {
        int row = tblAddtask.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "編集するタスクを選択してください。");
            return;
        }

        // LẤY ĐÚNG CỘT: "タスク名" là cột 1
        String taskName = tblAddtask.getValueAt(row, 1).toString();

        Optional<Tasks> optTask = taskController.findByName(taskName);

        // Optional không bao giờ null → dùng isEmpty()
        if (optTask.isEmpty()) {
            JOptionPane.showMessageDialog(this, "タスクが見つかりません。");
            return;
        }

        Tasks task = optTask.get(); // task cần edit

        JFrame parent = (JFrame) SwingUtilities.getWindowAncestor(this);
        JDialog dialog = new JDialog(parent, "タスク編集", true);

        // 👉 PHẢI TRUYỀN task VÀO CONSTRUCTOR EDIT
        dialog.setContentPane(new AddTask(task));

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);

        loadTable(taskController.findAll());
    }


    private void deleteTask() {
        int row = tblAddtask.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                    "削除するタスクを選択してください。",
                    "エラー",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String taskName = tblAddtask.getValueAt(row, 1).toString();
        Optional<Tasks> t = taskController.findByName(taskName);

        // Optional không bao giờ null → phải check isPresent()
        if (t.isEmpty()) {
            JOptionPane.showMessageDialog(this, "タスクが見つかりません。");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "このタスクを削除しますか？\n",
                "確認",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                taskController.delete(t.get().getId());
                loadTable(taskController.findAll());

                JOptionPane.showMessageDialog(this,
                        "タスクを削除しました。",
                        "完了",
                        JOptionPane.INFORMATION_MESSAGE);

            } catch (Exception ex) {
                String msg = ex.getMessage();

                if (msg != null && msg.contains("使用されている")) {
                    JOptionPane.showMessageDialog(this,
                            "このタスクは勤務記録で使用されているため、削除できません。",
                            "削除エラー",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this,
                            "削除中にエラーが発生しました。\n" + msg,
                            "エラー",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }




    private void loadTable(List<Tasks> list) {
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"No.", "タスク名", "プロジェクト名", "部署名"}, 0
        );

        int no = 1;

        for (Tasks t : list) {

            String projects = t.getProjects() == null ? "" :
                    String.join(", ", t.getProjects().stream().map(Project::getName).toList());

            String deps = t.getDepartments() == null ? "" :
                    String.join(", ", t.getDepartments().stream().map(Department::getName).toList());

            model.addRow(new Object[]{
                    no++,
                    t.getName(),
                    projects,
                    deps
            });
        }

        tblAddtask.setModel(model);
    }


}
