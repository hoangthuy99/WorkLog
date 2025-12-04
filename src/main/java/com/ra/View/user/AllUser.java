package com.ra.View.user;

import com.ra.Controller.UserController;
import com.ra.Model.Entity.Tasks;
import com.ra.Model.Entity.Users;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import com.ra.Sercurity.SessionLocal;
import com.ra.Utils.HibernateUtil;
import org.hibernate.Session;


/**
 *
 * @author Admin
 */
public class AllUser extends javax.swing.JPanel {

    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(AllUser.class.getName());

    // KHAI BÁO BIẾN CHO LOGIC XỬ LÝ
    private int currentPage = 1;
    private final int pageSize = 10;
    // Biến này lưu trữ ID thật của các user đang hiển thị trên bảng
    private List<Integer> userIds;
    public UserController userController;


    /**
     * Creates new form AllUser1
     */
    public AllUser() {
        userController = new UserController(); // Khởi tạo Controller
        initComponents();
        // ===== PHÂN QUYỀN =====
        String role = (String) SessionLocal.get("ROLE");
        System.out.println("ROLE in AllUser = " + role);

        if (role != null) {
            switch (role) {
                case "employee" -> {
                    btnAdduser.setVisible(false);
                    btnEdit.setVisible(false);
                    btnDelete.setVisible(false);
                }
                case "manager" -> {
                    btnDelete.setVisible(false); // manager không được xóa
                }
                case "admin" -> {
                    // full quyền → không cần làm gì
                }
            }
        }



        loadUserTable();
    }

    // Phương thức mới để áp dụng lại bố cục căn giữa và cân đối
    private void applyCenteredLayout() {

        // Thiết lập lại ActionListener
        btnSearch.addActionListener(this::btnSearchActionPerformed);
        btnAlluser.addActionListener(this::btnAlluserActionPerformed);
        btnAdduser.addActionListener(this::btnAdduserActionPerformed);
        btnEdit.addActionListener(this::btnEditActionPerformed);
        btnDelete.addActionListener(this::btnDeleteActionPerformed);
        txtSearch.addActionListener(this::txtSearchActionPerformed);

        // Bố cục căn giữa GroupLayout
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);

        int TABLE_WIDTH = 575; // Kích thước bảng cố định
        int BUTTON_WIDTH = 75;
        int BUTTON_GAP = 30; // Khoảng cách giữa Edit và Delete

        // ====================================================================
        // BỐ CỤC NGANG (HORIZONTAL GROUP)
        // ====================================================================
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                // Khoảng trống co giãn bên trái để căn giữa
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false) // Thêm false để giữ kích thước cố định

                                        // Hàng 1: Tìm kiếm và Thêm mới (Chiều rộng khớp với bảng)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                // Nhóm tìm kiếm bên trái
                                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnAlluser)

                                                // Khoảng trống co giãn
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)

                                                // Nút thêm mới bên phải
                                                .addComponent(btnAdduser, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE))

                                        // Hàng 2: Bảng (rộng cố định)
                                        .addComponent(scrollTable, javax.swing.GroupLayout.PREFERRED_SIZE, TABLE_WIDTH, javax.swing.GroupLayout.PREFERRED_SIZE)

                                        // Hàng 3: Nút Edit/Delete (căn phải so với bảng)
                                        .addGroup(layout.createSequentialGroup()
                                                // Khoảng trống co giãn để đẩy nút sang phải
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE) // Dùng Short.MAX_VALUE để đẩy
                                                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, BUTTON_WIDTH, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(BUTTON_GAP, BUTTON_GAP, BUTTON_GAP)
                                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, BUTTON_WIDTH, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                // Khoảng trống co giãn bên phải để căn giữa
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        // ====================================================================
        // BỐ CỤC DỌC (VERTICAL GROUP)
        // ====================================================================
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                // THAY THẾ padding cố định bằng KHOẢNG TRỐNG CO GIÃN
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)

                                // Hàng 1: Tìm kiếm/Tạo mới
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSearch)
                                        .addComponent(btnAlluser)
                                        .addComponent(btnAdduser))
                                .addGap(18, 18, 18)
                                // Hàng 2: Bảng
                                .addComponent(scrollTable, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE) // Chiều cao cố định
                                .addGap(18, 18, 18)
                                // Hàng 3: Hành động
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))

                                // Giữ khoảng trống co giãn ở dưới
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    // SỬA: Đổi từ private sang public để EditUser có thể gọi phương thức này để refresh bảng
    public void loadUserTable() {
        String keyword = txtSearch.getText().trim();
        List<Users> list = userController.findAll(keyword, currentPage, pageSize);

        userIds = new java.util.ArrayList<>();

        String[][] data = new String[list.size()][7];

        for (int i = 0; i < list.size(); i++) {
            Users u = list.get(i);
            // STT tăng dần
            data[i][0] = String.valueOf((currentPage - 1) * pageSize + i + 1);
            data[i][1] = u.getUserCode();
            data[i][2] = u.getFullName();
            data[i][3] = u.getUserName();
            data[i][4] = u.getDepartment() != null ? u.getDepartment().getName() : "";
            data[i][5] = u.getRole() != null ? u.getRole().getName() : "";
            data[i][6] = (u.getTasks() != null && !u.getTasks().isEmpty())
                    ? u.getTasks().stream().map(Tasks::getName).collect(Collectors.joining(", "))
                    : "";
            // Lưu ID thật
            userIds.add(u.getId());
        }

        tbAllUser.setModel(new javax.swing.table.DefaultTableModel(
                data,
                new String[]{"No.", "ユーザーコード", "社員名", "ユーザー名", "部署", "ロール","タスク"}
        ));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnAlluser = new javax.swing.JButton();
        btnAdduser = new javax.swing.JButton();
        scrollTable = new javax.swing.JScrollPane();
        tbAllUser = new javax.swing.JTable();
        btnEdit = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();

        setBackground(new java.awt.Color(255, 255, 255));

        txtSearch.setToolTipText("");
        txtSearch.addActionListener(this::txtSearchActionPerformed);

        btnSearch.setText("検索");
        btnSearch.addActionListener(this::btnSearchActionPerformed);

        btnAlluser.setText("すべて");
        btnAlluser.addActionListener(this::btnAlluserActionPerformed);

        btnAdduser.setBackground(new java.awt.Color(201, 221, 201));
        btnAdduser.setText("新ユーザー作成");
        btnAdduser.addActionListener(this::btnAdduserActionPerformed);

        tbAllUser.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "ユーザーコード", "社員名", "ユーザー名", "部署", "ロール", "タスク"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollTable.setViewportView(tbAllUser);

        btnEdit.setBackground(new java.awt.Color(0, 255, 204));
        btnEdit.setText("Edit");
        btnEdit.addActionListener(this::btnEditActionPerformed);

        btnDelete.setBackground(new java.awt.Color(255, 0, 51));
        btnDelete.setText("Delete");
        btnDelete.addActionListener(this::btnDeleteActionPerformed);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(37, 37, 37)
                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 132, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnAlluser)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnAdduser, javax.swing.GroupLayout.PREFERRED_SIZE, 124, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(42, 42, 42))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(scrollTable, javax.swing.GroupLayout.PREFERRED_SIZE, 575, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(28, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnEdit)
                .addGap(45, 45, 45)
                .addComponent(btnDelete)
                .addGap(35, 35, 35))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(btnAlluser)
                    .addComponent(btnAdduser))
                .addGap(18, 18, 18)
                .addComponent(scrollTable, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEdit)
                    .addComponent(btnDelete))
                .addContainerGap(55, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    private int currentPage = 1;
    private final int pageSize =Integer.MAX_VALUE;
    private long totalUsers = 0;
    private int totalPages = 0;

    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        btnSearchActionPerformed(evt);
    }//GEN-LAST:event_txtSearchActionPerformed
    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        currentPage = 1;
        loadUserTable();
    }//GEN-LAST:event_btnSearchActionPerformed
    private void btnAlluserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAlluserActionPerformed
        currentPage = 1;
        txtSearch.setText("");
        loadUserTable();
    }//GEN-LAST:event_btnAlluserActionPerformed
    private void btnAdduserActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdduserActionPerformed
        // TODO add your handling code here:
        AddUser addUserForm = new AddUser();
        addUserForm.setVisible(true);
        addUserForm.setLocationRelativeTo(null);
    }//GEN-LAST:event_btnAdduserActionPerformed
    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditActionPerformed
        int selectedRow = tbAllUser.getSelectedRow();
        Integer id = userIds.get(selectedRow);
       // Tìm user trong DB
        Optional<Users> user = userController.findById(id);
        if (user.isPresent()) {
            EditUser editForm = new EditUser(user.get());
            editForm.setVisible(true);
            editForm.setLocationRelativeTo(null);
        } else {
            JOptionPane.showMessageDialog(this, "ユーザーが存在しません！");
        }
    }//GEN-LAST:event_btnEditActionPerformed
    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        int row = tbAllUser.getSelectedRow();

        if (row < 0 || row >= userIds.size()) {
            JOptionPane.showMessageDialog(this, "ユーザーを選択してください！");
            return;
        }

        try {
            Integer id = userIds.get(row);

            Optional<Users> u = userController.findById(id);

            if (!u.isPresent()) {
                JOptionPane.showMessageDialog(this, "このユーザーは存在しません");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(
                    this,
                    "このユーザーを削除しますか？",
                    "確認",
                    JOptionPane.YES_NO_OPTION
            );

            if (confirm == JOptionPane.YES_OPTION) {
                userController.deleteUser(id);

                loadUserTable();

                JOptionPane.showMessageDialog(this, "削除しました"); // Đã xóa
            }

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Xảy ra lỗi khi xóa user: " + e.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
            logger.log(java.util.logging.Level.SEVERE, "Lỗi khi xóa user", e);
        }
    }//GEN-LAST:event_btnDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdduser;
    private javax.swing.JButton btnAlluser;
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnSearch;
    private javax.swing.JScrollPane scrollTable;
    private javax.swing.JTable tbAllUser;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}