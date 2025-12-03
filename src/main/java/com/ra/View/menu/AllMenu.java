package com.ra.View.menu;

import com.ra.Controller.PermissionController;
import com.ra.Model.Entity.Permission;
import com.ra.Model.Entity.Roles;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.logging.Logger;

public class AllMenu extends javax.swing.JPanel {

    private static final java.util.logging.Logger logger =
            Logger.getLogger(AllMenu.class.getName());

    private final PermissionController controller = new PermissionController();
    private List<Permission> dataList;
    private List<Integer> permissionIds;    // Lưu ID thật

    /* * ĐÃ XÓA KHAI BÁO LẠI CÁC BIẾN COMPONENT KHÔNG CẦN THIẾT Ở ĐÂY.
     * Chúng sẽ được tự động khai báo và khởi tạo ở cuối file trong initComponents().
     * Nếu bạn cần thêm nút Edit/Delete, bạn phải tạo chúng trong initComponents()
     * và khai báo chúng ở cuối file.
     */

    // KHAI BÁO NÚT EDIT VÀ DELETE THỰC TẾ (Vì chúng thiếu trong initComponents() cũ)
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnDelete;

    /**
     * Creates new form AllUser1
     */
    public AllMenu() {
        // Gọi initComponents trước
        initComponents();

        // Khởi tạo các nút bị thiếu (Edit/Delete)
        // LƯU Ý: Đây là phương pháp thủ công nếu bạn không dùng IDE Designer để tạo chúng.
        btnEdit = new JButton("編集");
        btnDelete = new JButton("削除");

        // Áp dụng bố cục căn giữa và thêm các nút Edit/Delete vào bố cục
        applyCenteredLayout();
        loadTableData(controller.findAll());
    }

    // Phương thức mới để khởi tạo nút và áp dụng bố cục căn giữa
    private void applyCenteredLayout() {
        // --- 1. GẮN LẠI SỰ KIỆN CHO NÚT ---
        // LƯU Ý: Các biến btnEdit, btnDelete, btnAddmenu, etc. giờ phải
        // là các biến đã được khởi tạo.
        btnEdit.addActionListener(this::btnEditActionPerformed);
        btnDelete.addActionListener(this::btnDeleteActionPerformed);
        btnAddmenu.addActionListener(this::btnAddmenuActionPerformed);
        btnSearch.addActionListener(this::btnSearchActionPerformed);
        btnAllMenu.addActionListener(this::btnAllMenuActionPerformed);
        txtSearch.addActionListener(this::txtSearchActionPerformed);


        // --- 2. ÁP DỤNG LAYOUT CĂN GIỮA ---
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);

        int TABLE_WIDTH = 622; // Kích thước bảng cố định
        int SEARCH_WIDTH = 120;
        int BTN_SEARCH_WIDTH = 60;
        int BTN_ALL_WIDTH = 60; // Giả định
        int BTN_ADD_WIDTH = 140;
        int BUTTON_WIDTH = 75;
        int BUTTON_GAP = 18;

        // Bố cục Ngang (Horizontal Group)
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                // Khoảng trống co giãn bên trái để căn giữa
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)

                                // Nhóm tất cả các thành phần trung tâm vào một ParalleGroup
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)

                                        // Hàng 1: Tìm kiếm và Thêm mới (Đảm bảo chiều rộng khớp với bảng)
                                        .addGroup(layout.createSequentialGroup()
                                                // Nhóm tìm kiếm bên trái
                                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, SEARCH_WIDTH, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED) // GAP1
                                                .addComponent(btnSearch, javax.swing.GroupLayout.PREFERRED_SIZE, BTN_SEARCH_WIDTH, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED) // GAP2
                                                .addComponent(btnAllMenu, javax.swing.GroupLayout.PREFERRED_SIZE, BTN_ALL_WIDTH, javax.swing.GroupLayout.PREFERRED_SIZE)

                                                // Khoảng trống co giãn: Tách nhóm tìm kiếm và nút Thêm mới
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)

                                                // Nút thêm mới bên phải
                                                .addComponent(btnAddmenu, javax.swing.GroupLayout.PREFERRED_SIZE, BTN_ADD_WIDTH, javax.swing.GroupLayout.PREFERRED_SIZE))

                                        // Hàng 2: Bảng (rộng cố định)
                                        .addComponent(scrollPaneTable, javax.swing.GroupLayout.PREFERRED_SIZE, TABLE_WIDTH, javax.swing.GroupLayout.PREFERRED_SIZE)

                                        // Hàng 3: Nút Edit/Delete (Căn phải so với Hàng 2/Bảng)
                                        .addGroup(layout.createSequentialGroup()
                                                // Khoảng trống co giãn: Đẩy nút Edit/Delete sang phải
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, BUTTON_WIDTH, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(BUTTON_GAP, BUTTON_GAP, BUTTON_GAP)
                                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, BUTTON_WIDTH, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                // Khoảng trống co giãn bên phải để căn giữa
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        // Bố cục Dọc (Vertical Group)
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                // Thêm khoảng trống padding trên (ví dụ: 50px)
                                .addGap(50, 50, 50)

                                // Hàng 1: Tìm kiếm/Tạo mới
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSearch)
                                        .addComponent(btnAllMenu)
                                        .addComponent(btnAddmenu))
                                .addGap(18, 18, 18)
                                // Hàng 2: Bảng
                                .addComponent(scrollPaneTable, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE) // Chiều cao cố định
                                .addGap(18, 18, 18)
                                // Hàng 3: Hành động
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        // Sử dụng các biến btnEdit và btnDelete đã được khởi tạo
                                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                // Khoảng trống co giãn ở dưới
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }

    // ========================= LOAD TABLE =========================
    private void loadTableData(List<Permission> list) {
        this.dataList = list;
        this.permissionIds = list.stream()
                .map(Permission::getId)
                .collect(Collectors.toList());

        String[] cols = {"メニューコード", "メニュー名", "ロール", "編集", "削除"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (Permission p : list) {
            String roleNames = (p.getRoles() != null)
                    ? p.getRoles().stream().map(Roles::getName).collect(Collectors.joining(", "))
                    : "";

            model.addRow(new Object[]{
                    p.getCode(),
                    p.getName(),
                    roleNames,
                    // Giữ null ở đây vì bạn không dùng Renderer cho nút
                    null,
                    null
            });
        }
        tblAllmenu.setModel(model);
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
        btnAllMenu = new javax.swing.JButton();
        btnAddmenu = new javax.swing.JButton();
        scrollPaneTable = new javax.swing.JScrollPane();
        tblAllmenu = new javax.swing.JTable();
        // KHÔNG CẦN KHỞI TẠO btnEdit và btnDelete ở đây nếu bạn khởi tạo chúng trong constructor

        setBackground(new java.awt.Color(255, 255, 255));

        txtSearch.setText("jTextField1");
        txtSearch.addActionListener(this::txtSearchActionPerformed);

        btnSearch.setBackground(new java.awt.Color(229, 229, 229));
        btnSearch.setText("検索");
        btnSearch.addActionListener(this::btnSearchActionPerformed);

        btnAllMenu.setText("すべて");
        btnAllMenu.addActionListener(this::btnAllMenuActionPerformed);

        btnAddmenu.setBackground(new java.awt.Color(208, 241, 208));
        btnAddmenu.setText("新メニュー作成");

        tblAllmenu.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null},
                        {null, null, null, null, null}
                },
                new String [] {
                        "メニューコード", "メニュー名", "ロール", "編集", "削除"
                }
        ) {
            Class[] types = new Class [] {
                    java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblAllmenu.setCellSelectionEnabled(true);
        tblAllmenu.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        tblAllmenu.setShowHorizontalLines(true);
        tblAllmenu.setShowVerticalLines(true);
        tblAllmenu.setSurrendersFocusOnKeystroke(true);
        scrollPaneTable.setViewportView(tblAllmenu);

        // Bỏ phần Layout tự động của IDE vì bạn đã định nghĩa lại trong applyCenteredLayout()
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(null); // Sử dụng null layout tạm thời
    }// </editor-fold>//GEN-END:initComponents

    // ===================== SEARCH ==========================
    private void txtSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        btnSearchActionPerformed(evt);
    }//GEN-LAST:event_txtSearchActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        String key = txtSearch.getText().trim();
        loadTableData(controller.search(key));
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnAllMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAllMenuActionPerformed
        loadTableData(controller.findAll());
    }//GEN-LAST:event_btnAllMenuActionPerformed

    // ====================== ADD NEW MENU =======================
    private void btnAddmenuActionPerformed(ActionEvent e) {
        Window parentWindow = SwingUtilities.getWindowAncestor(this);
        JFrame parentFrame = (parentWindow instanceof JFrame) ? (JFrame) parentWindow : null;

        try {
            // Giả định AddMenu là JFrame/JDialog
            JFrame frame = new JFrame("新メニュー作成");
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            // frame.add(new AddMenu());

            frame.pack();
            // Sử dụng setLocationRelativeTo(null) để căn giữa màn hình nếu không có parentFrame
            // Hoặc căn giữa so với parentFrame nếu có
            frame.setLocationRelativeTo(parentFrame != null ? parentFrame : null);
            frame.setVisible(true);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "AddMenu Componentが見つからないか、エラーが発生しました: " + ex.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
            logger.log(java.util.logging.Level.SEVERE, "Lỗi khi mở AddMenu", ex);
        }
    }

    // ====================== EDIT ============================
    private void btnEditActionPerformed(ActionEvent evt) {

        int row = tblAllmenu.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "メニューを選択してください!");
            return;
        }

        Integer id = permissionIds.get(row);

        Optional<Permission> perm = controller.findById(id);

        if (perm.isPresent()) {
            Window parentWindow = SwingUtilities.getWindowAncestor(this);
            JFrame parentFrame = (parentWindow instanceof JFrame) ? (JFrame) parentWindow : null;

            try {
                // Giả định EditMenuScreen là JFrame/JDialog
                JFrame frame = new JFrame("メニュー編集");
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                // frame.add(new EditMenuScreen(perm.get()));

                frame.pack();
                // Sử dụng setLocationRelativeTo(null) để căn giữa màn hình nếu không có parentFrame
                frame.setLocationRelativeTo(parentFrame != null ? parentFrame : null);
                frame.setVisible(true);

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "EditMenu Componentが見つからないか、エラーが発生しました: " + ex.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
                logger.log(java.util.logging.Level.SEVERE, "Lỗi khi mở EditMenu", ex);
            }
        } else {
            JOptionPane.showMessageDialog(this, "メニューが存在しません！");
        }
    }

    // ======================= DELETE ===========================
    private void btnDeleteActionPerformed(ActionEvent evt) {

        int row = tblAllmenu.getSelectedRow();
        if (row < 0) {
            JOptionPane.showMessageDialog(this, "メニューを選択してください!");
            return;
        }

        Integer id = permissionIds.get(row);

        Optional<Permission> perm = controller.findById(id);

        if (!perm.isPresent()) {
            JOptionPane.showMessageDialog(this, "このメニューは存在しません！");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "このメニューを削除しますか？",
                "確認",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            controller.delete(id);
            loadTableData(controller.findAll());
            JOptionPane.showMessageDialog(this, "削除しました！");
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddmenu;
    private javax.swing.JButton btnAllMenu;
    private javax.swing.JButton btnSearch;
    private javax.swing.JScrollPane scrollPaneTable;
    private javax.swing.JTable tblAllmenu;
    private javax.swing.JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}