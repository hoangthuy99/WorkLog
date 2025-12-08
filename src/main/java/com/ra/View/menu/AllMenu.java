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

public class AllMenu extends JPanel {

    private static final Logger logger =
            Logger.getLogger(AllMenu.class.getName());

    private final PermissionController controller = new PermissionController();
    private List<Permission> dataList;
    private List<Integer> permissionIds;    // Lưu ID thật

    // KHAI BÁO NÚT EDIT VÀ DELETE THỰC TẾ
    private JButton btnEdit;
    private JButton btnDelete;

    /**
     * Creates new form AllUser1
     */
    public AllMenu() {
        // Gọi initComponents trước
        initComponents();

        // Khởi tạo các nút bị thiếu (Edit/Delete)
        btnEdit = new JButton("編集");
        btnDelete = new JButton("削除");

        // Áp dụng bố cục căn giữa và thêm các nút Edit/Delete vào bố cục
        applyCenteredLayout();
        loadTableData(controller.findAll());
    }

    // Phương thức mới để khởi tạo nút và áp dụng bố cục căn giữa
    private void applyCenteredLayout() {
        // --- 1. GẮN LẠI SỰ KIỆN CHO NÚT ---
        btnEdit.addActionListener(this::btnEditActionPerformed);
        btnDelete.addActionListener(this::btnDeleteActionPerformed);
        btnAddmenu.addActionListener(this::btnAddmenuActionPerformed);
        btnSearch.addActionListener(this::btnSearchActionPerformed);
        btnAllMenu.addActionListener(this::btnAllMenuActionPerformed);
        txtSearch.addActionListener(this::txtSearchActionPerformed);


        // --- 2. ÁP DỤNG LAYOUT CĂN GIỮA ---
        // Sử dụng GridBagLayout để căn giữa container chính
        this.setLayout(new GridBagLayout());

        // Tạo một JPanel phụ để chứa tất cả các thành phần UI cũ, sau đó căn giữa JPanel phụ này.
        JPanel contentPanel = new JPanel();
        // DÒNG NÀY ĐÃ ĐƯỢC SỬA: Đổi màu nền của contentPanel thành trắng (255, 255, 255)
        contentPanel.setBackground(new Color(255, 255, 255));

        // Áp dụng GroupLayout cho contentPanel
        GroupLayout layout = new GroupLayout(contentPanel);
        contentPanel.setLayout(layout);

        int TABLE_WIDTH = 622; // Kích thước bảng cố định (dựa trên các file ảnh)
        int SEARCH_WIDTH = 120;
        int BTN_SEARCH_WIDTH = 60;
        int BTN_ALL_WIDTH = 60;
        int BTN_ADD_WIDTH = 140;
        int BUTTON_WIDTH = 75;
        int BUTTON_GAP = 18;

        // Bố cục Ngang (Horizontal Group)
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                                        // Hàng 1: Tìm kiếm và Thêm mới
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, SEARCH_WIDTH, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                                .addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, BTN_SEARCH_WIDTH, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnAllMenu, GroupLayout.PREFERRED_SIZE, BTN_ALL_WIDTH, GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnAddmenu, GroupLayout.PREFERRED_SIZE, BTN_ADD_WIDTH, GroupLayout.PREFERRED_SIZE))

                                        // Hàng 2: Bảng
                                        .addComponent(scrollPaneTable, GroupLayout.PREFERRED_SIZE, TABLE_WIDTH, GroupLayout.PREFERRED_SIZE)

                                        // Hàng 3: Nút Edit/Delete (Căn phải so với Bảng)
                                        .addGroup(layout.createSequentialGroup()
                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnEdit, GroupLayout.PREFERRED_SIZE, BUTTON_WIDTH, GroupLayout.PREFERRED_SIZE)
                                                .addGap(BUTTON_GAP, BUTTON_GAP, BUTTON_GAP)
                                                .addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, BUTTON_WIDTH, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        // Bố cục Dọc (Vertical Group)
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                // Hàng 1: Tìm kiếm/Tạo mới
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSearch)
                                        .addComponent(btnAllMenu)
                                        .addComponent(btnAddmenu))
                                .addGap(18, 18, 18)
                                // Hàng 2: Bảng
                                .addComponent(scrollPaneTable, GroupLayout.PREFERRED_SIZE, 300, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                // Hàng 3: Hành động
                                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnEdit, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(50, Short.MAX_VALUE)) // Padding dưới
        );

        // 3. Đặt contentPanel vào giữa JPanel chính (this) bằng GridBagLayout
        this.removeAll(); // Đảm bảo JPanel chính trống
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER; // Căn giữa contentPanel
        gbc.fill = GridBagConstraints.NONE;

        this.add(contentPanel, gbc);
        this.revalidate();
        this.repaint();
    }

    // ========================= LOAD TABLE =========================
    private void loadTableData(List<Permission> list) {
        this.dataList = list;
        this.permissionIds = list.stream()
                .map(Permission::getId)
                .collect(Collectors.toList());

        // 🌟 SỬA ĐỔI: LOẠI BỎ "編集" và "削除" khỏi cột headers
        String[] cols = {"メニューコード", "メニュー名", "ロール"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (Permission p : list) {
            String roleNames = (p.getRoles() != null)
                    ? p.getRoles().stream().map(Roles::getName).collect(Collectors.joining(", "))
                    : "";

            // 🌟 SỬA ĐỔI: CHỈ THÊM 3 CỘT DỮ LIỆU
            model.addRow(new Object[]{
                    p.getCode(),
                    p.getName(),
                    roleNames
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

        txtSearch = new JTextField();
        btnSearch = new JButton();
        btnAllMenu = new JButton();
        btnAddmenu = new JButton();
        scrollPaneTable = new JScrollPane();
        tblAllmenu = new JTable();
        // KHÔNG CẦN KHỞI TẠO btnEdit và btnDelete ở đây nếu bạn khởi tạo chúng trong constructor

        setBackground(new Color(255, 255, 255));

        txtSearch.setText("jTextField1");
        txtSearch.addActionListener(this::txtSearchActionPerformed);

        btnSearch.setBackground(new Color(229, 229, 229));
        btnSearch.setText("検索");
        btnSearch.addActionListener(this::btnSearchActionPerformed);

        btnAllMenu.setText("すべて");
        btnAllMenu.addActionListener(this::btnAllMenuActionPerformed);

        btnAddmenu.setBackground(new Color(208, 241, 208));
        btnAddmenu.setText("新メニュー作成");

        tblAllmenu.setModel(new DefaultTableModel(
                new Object [][] {
                        {null, null, null}, // 🌟 SỬA ĐỔI: CHỈ CÒN 3 CỘT
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null}
                },
                new String [] {
                        "メニューコード", "メニュー名", "ロール" // 🌟 SỬA ĐỔI: CHỈ CÒN 3 CỘT
                }
        ) {
            Class[] types = new Class [] {
                    String.class, String.class, String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
        });
        tblAllmenu.setCellSelectionEnabled(true);
        tblAllmenu.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        tblAllmenu.setShowHorizontalLines(true);
        tblAllmenu.setShowVerticalLines(true);
        tblAllmenu.setSurrendersFocusOnKeystroke(true);
        scrollPaneTable.setViewportView(tblAllmenu);

        // Bỏ phần Layout tự động của IDE
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(null);
    }// </editor-fold>//GEN-END:initComponents

    // ===================== SEARCH ==========================
    private void txtSearchActionPerformed(ActionEvent evt) {//GEN-FIRST:event_txtSearchActionPerformed
        btnSearchActionPerformed(evt);
    }//GEN-LAST:event_txtSearchActionPerformed

    private void btnSearchActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        String key = txtSearch.getText().trim();
        loadTableData(controller.search(key));
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnAllMenuActionPerformed(ActionEvent evt) {//GEN-FIRST:event_btnAllMenuActionPerformed
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
            // frame.add(new AddMenu()); // Cần uncomment và sửa nếu có lớp AddMenu

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
                // frame.add(new EditMenuScreen(perm.get())); // Cần uncomment và sửa nếu có lớp EditMenuScreen

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
    private JButton btnAddmenu;
    private JButton btnAllMenu;
    private JButton btnSearch;
    private JScrollPane scrollPaneTable;
    private JTable tblAllmenu;
    private JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}