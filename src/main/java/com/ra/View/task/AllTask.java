package com.ra.View.task;

import com.ra.Controller.TaskController;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.logging.Logger;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

public class AllTask extends JPanel {

    // 1. KHAI BÁO CONTROLLER VÀ LOGGER
    private final TaskController taskController = new TaskController();
    private static final Logger logger =
            Logger.getLogger(AllTask.class.getName());

    // Khai báo Content Panel để chứa nội dung cũ
    private JPanel contentPanel;


    /**
     * Creates new form AllTask1
     */
    public AllTask() {
        initComponents();
        // GỌI PHƯƠNG THỨC CĂN GIỮA
        applyCenteredLayout();

        // Tải dữ liệu ban đầu
        loadTable(taskController.findAll());

        // Gắn lại sự kiện cho các nút Edit và Delete (nếu bạn cần xử lý sự kiện cho chúng)
        // Lưu ý: Các sự kiện đã được gán trong initComponents, chỉ cần đảm bảo logic hoạt động
        // btnEdit.addActionListener(this::btnEditActionPerformed); // Đã có trong initComponents
        // btnDelete.addActionListener(this::btnDeleteActionPerformed); // Đã có trong constructor
    }

    // ==========================================================
    // PHƯƠNG THỨC THAY ĐỔI LAYOUT ĐỂ CĂN GIỮA
    // ==========================================================
    private void applyCenteredLayout() {
        // 1. Tạo JPanel mới để giữ bố cục cũ
        contentPanel = new JPanel();
        contentPanel.setBackground(new java.awt.Color(255, 255, 255)); // Giữ màu nền

        // 2. Tạo GroupLayout mới cho contentPanel
        GroupLayout contentLayout = new GroupLayout(contentPanel);
        contentPanel.setLayout(contentLayout);

        // Xóa tất cả components khỏi JPanel chính
        this.removeAll();

        // Thêm components vào contentPanel
        contentPanel.add(scrAddtask);
        contentPanel.add(btnSearch);
        contentPanel.add(btnAddTask);
        contentPanel.add(lbKeywork);
        contentPanel.add(txtSearch);
        contentPanel.add(btnAll);
        contentPanel.add(btnEdit);
        contentPanel.add(btnDelete);

        // TÁI TẠO LẠI BỐ CỤC CŨ TRÊN contentLayout
        contentLayout.setHorizontalGroup(
                contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 679, Short.MAX_VALUE)
                        .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(contentLayout.createSequentialGroup()
                                        .addGap(79, 79, 79)
                                        .addComponent(scrAddtask, GroupLayout.PREFERRED_SIZE, 521, GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(79, Short.MAX_VALUE)))
                        .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(contentLayout.createSequentialGroup()
                                        .addGap(78, 78, 78)
                                        .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                                .addGroup(contentLayout.createSequentialGroup()
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 313, GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(btnEdit, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                                        .addGap(59, 59, 59)
                                                        .addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE))
                                                .addGroup(contentLayout.createSequentialGroup()
                                                        .addComponent(lbKeywork, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 55, GroupLayout.PREFERRED_SIZE)
                                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                        .addComponent(btnAll, GroupLayout.PREFERRED_SIZE, 57, GroupLayout.PREFERRED_SIZE)
                                                        .addGap(83, 83, 83)
                                                        .addComponent(btnAddTask, GroupLayout.PREFERRED_SIZE, 128, GroupLayout.PREFERRED_SIZE)))
                                        .addGap(79, 79, 79)))
        );
        contentLayout.setVerticalGroup(
                contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGap(0, 473, Short.MAX_VALUE)
                        .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(contentLayout.createSequentialGroup()
                                        .addGap(103, 103, 103)
                                        .addComponent(scrAddtask, GroupLayout.PREFERRED_SIZE, 267, GroupLayout.PREFERRED_SIZE)
                                        .addContainerGap(103, Short.MAX_VALUE)))
                        .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                .addGroup(contentLayout.createSequentialGroup()
                                        .addGap(68, 68, 68)
                                        .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(btnAddTask)
                                                .addComponent(lbKeywork)
                                                .addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                                .addComponent(btnSearch)
                                                .addComponent(btnAll))
                                        .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 291, Short.MAX_VALUE)
                                        .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                                .addComponent(btnEdit)
                                                .addComponent(btnDelete))
                                        .addGap(68, 68, 68)))
        );

        // 3. Áp dụng GridBagLayout cho JPanel chính (this) và đặt contentPanel vào giữa
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0; // Cho phép không gian trống co giãn theo chiều ngang
        gbc.weighty = 1.0; // Cho phép không gian trống co giãn theo chiều dọc
        gbc.anchor = GridBagConstraints.CENTER; // Căn giữa contentPanel
        gbc.fill = GridBagConstraints.NONE; // Không kéo giãn contentPanel

        this.add(contentPanel, gbc);
        this.revalidate();
        this.repaint();
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        scrAddtask = new JScrollPane();
        tblAddtask = new JTable();
        btnSearch = new JButton();
        btnAddTask = new JButton();
        lbKeywork = new JLabel();
        txtSearch = new JTextField();
        btnAll = new JButton();
        btnEdit = new JButton();
        btnDelete = new JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        tblAddtask.setModel(new DefaultTableModel(
                new Object [][] {
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null}
                },
                new String [] {
                        "タスク名", "プロジェクト名", "部署名"
                }
        ) {
            Class[] types = new Class [] {
                    String.class, String.class, String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        scrAddtask.setViewportView(tblAddtask);

        btnSearch.setText("検索");
        btnSearch.addActionListener(this::btnSearchActionPerformed);

        btnAddTask.setText("タスク作成");
        btnAddTask.addActionListener(this::btnAddTaskActionPerformed);

        lbKeywork.setText("キーワード");

        txtSearch.addActionListener(this::txtSearchActionPerformed);

        btnAll.setText("全て");
        btnAll.addActionListener(this::btnAllActionPerformed);

        btnEdit.setText("編集");
        btnEdit.addActionListener(this::btnEditActionPerformed);

        btnDelete.setText("削除");
        btnDelete.addActionListener(this::btnDeleteActionPerformed);

        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        this.setLayout(null); // Thay thế bằng null layout tạm thời
    }// </editor-fold>//GEN-END:initComponents

    // 2. CHUYỂN CÁC PHƯƠNG THỨC XỬ LÝ SỰ KIỆN

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        String kw = txtSearch.getText().trim();
        loadTable(taskController.search(kw));
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnAddTaskActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddTaskActionPerformed
        // Vì đây là JPanel, nó cần JFrame/JDialog cha để hiển thị AddTask (là JDialog)

        // Lấy JFrame cha (hoặc null nếu không tìm thấy)
        java.awt.Frame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

        // Mở dialog AddTask
        // Lưu ý: Class AddTask phải tồn tại trong package com.ra.View.task
        // Giả định có lớp AddTask (JDialog)
        // AddTask dialog = new AddTask(parentFrame, true);
        // dialog.setVisible(true);

        loadTable(taskController.findAll()); // Tải lại bảng sau khi đóng dialog
    }//GEN-LAST:event_btnAddTaskActionPerformed

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        // Xử lý sự kiện Enter trong ô tìm kiếm
        btnSearchActionPerformed(evt);
    }//GEN-LAST:event_txtSearchActionPerformed

    private void btnAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllActionPerformed
        loadTable(taskController.findAll());
    }//GEN-LAST:event_btnAllActionPerformed

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        // TODO: Xử lý sự kiện Edit
        int selectedRow = tblAddtask.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "編集するタスクを選択してください。", "注意", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Thêm logic mở dialog EditTask tại đây
    }//GEN-LAST:event_btnEditActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        // TODO: Xử lý sự kiện Delete
        int selectedRow = tblAddtask.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "削除するタスクを選択してください。", "注意", JOptionPane.WARNING_MESSAGE);
            return;
        }
        // Thêm logic xóa task tại đây
    }

    // 3. CHUYỂN PHƯƠNG THỨC TẢI DỮ LIỆU
    private void loadTable(List<Tasks> list) {

        String[] cols = {"タスク名", "プロジェクト名", "部署名"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (Tasks t : list) {

            // Xử lý nhiều projects: gom tên project lại thành chuỗi
            String projectNames = "";
            if (t.getProjects() != null) {
                projectNames = t.getProjects()
                        .stream()
                        .map(Project::getName)
                        .reduce("", (a, b) -> a + (a.isEmpty() ? "" : ", ") + b);
            }

            // Xử lý nhiều departments: gom tên department lại thành chuỗi
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
            });
        }

        tblAddtask.setModel(model);
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnAddTask;
    private JButton btnAll;
    private JButton btnDelete;
    private JButton btnEdit;
    private JButton btnSearch;
    private JLabel lbKeywork;
    private JScrollPane scrAddtask;
    private JTable tblAddtask;
    private JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}