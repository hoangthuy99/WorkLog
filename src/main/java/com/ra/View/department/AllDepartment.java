package com.ra.View.department;

import com.ra.Controller.DepartmentController;
import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class AllDepartment extends javax.swing.JPanel {

    // 1. CHUYỂN KHAI BÁO CONTROLLER VÀ LOGGER
    private final DepartmentController departmentController = new DepartmentController();

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(AllDepartment.class.getName());


    public AllDepartment() {
        initComponents();

        // 2. GẮN SỰ KIỆN CHO CÁC NÚT KHÔNG TỰ SINH (Edit, Delete)
        // Lưu ý: btnSearch và btnAddDepartment đã được tự động gắn trong initComponents() của File 2
        btnEdit.addActionListener(this::btnEditActionPerformed);
        btnDelete.addActionListener(this::btnDeleteActionPerformed);

        // 3. GỌI HÀM TẢI DỮ LIỆU KHI KHỞI TẠO
        loadTable(departmentController.findAll());
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbKeyword = new javax.swing.JLabel();
        btnSearch = new javax.swing.JButton();
        btnAddDepartment = new javax.swing.JButton();
        txtSearch = new javax.swing.JTextField();
        scrAdddepartment = new javax.swing.JScrollPane();
        tblAddDepartment = new javax.swing.JTable();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        lbKeyword.setText("キーワード");

        btnSearch.setText("検索");
        btnSearch.addActionListener(this::btnSearchActionPerformed);

        btnAddDepartment.setText("部署作成");
        btnAddDepartment.addActionListener(this::btnAddDepartmentActionPerformed);

        tblAddDepartment.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "部署名", "プロジェクト名", "タスク名"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Object.class, java.lang.Object.class, java.lang.String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        scrAdddepartment.setViewportView(tblAddDepartment);

        btnEdit.setText("編集");

        btnDelete.setText("削除");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 74, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(54, 54, 54)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(scrAdddepartment, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 452, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbKeyword, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnAddDepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(38, 38, 38)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbKeyword)
                    .addComponent(btnSearch)
                    .addComponent(btnAddDepartment)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(scrAdddepartment, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEdit)
                    .addComponent(btnDelete))
                .addContainerGap(47, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    // 4. CHUYỂN CÁC PHƯƠNG THỨC XỬ LÝ SỰ KIỆN (HANDLER)

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {
        String keyword = txtSearch.getText().trim();
        List<Department> list = departmentController.search(keyword);
        loadTable(list);
    }

    private void btnAddDepartmentActionPerformed(java.awt.event.ActionEvent evt) {
        // Vì đây là JPanel, nó không có 'parent' mặc định như JDialog.
        // Cần truyền Frame chứa nó vào khi tạo AddDepartment
        // LƯU Ý: Nếu bạn đã chuyển sang mô hình PanelSwitcher, bạn nên gọi PanelSwitcher.switchPanel(new AddDepartment1())

        JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        // Giả định có lớp AddDepartment (JDialog)
        // AddDepartment dialog = new AddDepartment(parentFrame, true);
        // dialog.setVisible(true);

        // loadTable(departmentController.findAll()); // reload
    }

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {
        // Logic chỉnh sửa: Lấy dòng được chọn và mở dialog chỉnh sửa
        int selectedRow = tblAddDepartment.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "編集する部署を選択してください。", "注意", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- BỔ SUNG LOGIC XỬ LÝ SỰ KIỆN CHO NÚT EDIT (CHƯA CÓ TRONG FILE GỐC) ---
        // Ví dụ: Lấy tên bộ phận từ hàng đã chọn (cần lấy ID trong thực tế)
        // String departmentName = tblAddDepartment.getValueAt(selectedRow, 0).toString();
        // Ví dụ: Mở dialog chỉnh sửa (nếu có lớp EditDepartment)
        // JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
        // EditDepartment editDialog = new EditDepartment(parentFrame, true, departmentId);
        // editDialog.setVisible(true);

        loadTable(departmentController.findAll()); // Tải lại bảng sau khi chỉnh sửa
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        // Logic xóa: Lấy dòng được chọn, xác nhận và xóa
        int selectedRow = tblAddDepartment.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "削除する部署を選択してください。", "注意", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // --- BỔ SUNG LOGIC XỬ LÝ SỰ KIỆN CHO NÚT DELETE (CHƯA CÓ TRONG FILE GỐC) ---
        // Lấy thông tin (cần ID trong thực tế, giả sử dùng Tên)
        // String departmentName = tblAddDepartment.getValueAt(selectedRow, 0).toString();

        int confirm = JOptionPane.showConfirmDialog(this,
                "この部署を削除してもよろしいですか？", "削除確認", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            // Thực hiện xóa (Cần có ID để xóa trong thực tế)
            // departmentController.delete(departmentId);
            JOptionPane.showMessageDialog(this, "部署が削除されました。");
            loadTable(departmentController.findAll()); // Tải lại bảng sau khi xóa
        }
    }


    // 5. CHUYỂN PHƯƠNG THỨC LOAD TABLE

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddDepartment;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSearch;
    private javax.swing.JLabel lbKeyword;
    private javax.swing.JScrollPane scrAdddepartment;
    private javax.swing.JTable tblAddDepartment;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables


    // ================== PHƯƠNG THỨC CHẠY THỬ (MAIN) ==================
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            // Đảm bảo import javax.swing.JFrame; nếu chưa có
            JFrame frame = new JFrame("All Department Test");
            AllDepartment panel = new AllDepartment();

            frame.setContentPane(panel);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}