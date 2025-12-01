package com.ra.View.menu;

import com.ra.Controller.PermissionController;
import com.ra.Model.Entity.Permission;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AllMenu extends javax.swing.JFrame {

    private static final java.util.logging.Logger logger =
            java.util.logging.Logger.getLogger(AllMenu.class.getName());

    private PermissionController controller = new PermissionController();
    private List<Permission> dataList;
    private List<Integer> permissionIds;    // Lưu ID thật

    public AllMenu() {
        initComponents();
        loadTableData(controller.findAll());
    }

    // ========================= LOAD TABLE =========================
    private void loadTableData(List<Permission> list) {

        this.dataList = list;

        this.permissionIds = list.stream()
                .map(Permission::getId)
                .collect(Collectors.toList());

        String[] cols = {"メニューコード", "メニュー名", "ロール"};
        DefaultTableModel model = new DefaultTableModel(cols, 0);

        for (Permission p : list) {

            String roleNames = "";
            if (p.getRoles() != null) {
                roleNames = p.getRoles()
                        .stream()
                        .map(r -> r.getName())
                        .collect(Collectors.joining(", "));
            }

            model.addRow(new Object[]{
                    p.getCode(),
                    p.getName(),
                    roleNames
            });
        }

        tblAllmenu.setModel(model);
    }

    // ============================ UI ==============================
    @SuppressWarnings("unchecked")
    private void initComponents() {

        pnlAllmenu = new javax.swing.JPanel();
        txtSearch = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        btnAllMenu = new javax.swing.JButton();
        btnAddmenu = new javax.swing.JButton();

        scrollPaneTable = new javax.swing.JScrollPane();
        tblAllmenu = new javax.swing.JTable();

        btnEdit = new JButton();
        btnDelete = new JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("メニュー一覧");

        pnlAllmenu.setBackground(new java.awt.Color(255, 255, 255));

        txtSearch.setText("");
        txtSearch.addActionListener(this::txtSearchActionPerformed);

        btnSearch.setBackground(new java.awt.Color(229, 229, 229));
        btnSearch.setText("検索");
        btnSearch.addActionListener(this::btnSearchActionPerformed);

        btnAllMenu.setText("すべて");
        btnAllMenu.addActionListener(this::btnAllMenuActionPerformed);

        btnAddmenu.setBackground(new java.awt.Color(208, 241, 208));
        btnAddmenu.setText("新メニュー作成");
        btnAddmenu.addActionListener(e -> {
            AddMenu add = new AddMenu();
            add.setVisible(true);
            add.setLocationRelativeTo(null);
        });

        tblAllmenu.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][]{},
                new String[]{
                        "メニューコード", "メニュー名", "ロール"
                }
        ));
        tblAllmenu.setRowHeight(25);
        scrollPaneTable.setViewportView(tblAllmenu);

        // =============== NÚT EDIT ===============
        btnEdit.setBackground(new java.awt.Color(0, 255, 204));
        btnEdit.setText("編集");
        btnEdit.addActionListener(this::btnEditActionPerformed);

        // =============== NÚT DELETE ===============
        btnDelete.setBackground(new java.awt.Color(255, 51, 51));
        btnDelete.setText("削除");
        btnDelete.addActionListener(this::btnDeleteActionPerformed);

        // ===================== LAYOUT =======================
        javax.swing.GroupLayout pnlAllmenuLayout = new javax.swing.GroupLayout(pnlAllmenu);
        pnlAllmenu.setLayout(pnlAllmenuLayout);

        pnlAllmenuLayout.setHorizontalGroup(
                pnlAllmenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlAllmenuLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(pnlAllmenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(scrollPaneTable, javax.swing.GroupLayout.PREFERRED_SIZE, 540, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(pnlAllmenuLayout.createSequentialGroup()
                                                .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnSearch)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(btnAllMenu)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                                                .addComponent(btnAddmenu, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlAllmenuLayout.createSequentialGroup()
                                                .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        ))
                                .addContainerGap(36, Short.MAX_VALUE))
        );

        pnlAllmenuLayout.setVerticalGroup(
                pnlAllmenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(pnlAllmenuLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addGroup(pnlAllmenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(txtSearch, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSearch)
                                        .addComponent(btnAllMenu)
                                        .addComponent(btnAddmenu))
                                .addGap(18, 18, 18)
                                .addComponent(scrollPaneTable, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(20, 20, 20)
                                .addGroup(pnlAllmenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnEdit, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnDelete, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addContainerGap(20, Short.MAX_VALUE))
        );

        getContentPane().add(pnlAllmenu, BorderLayout.CENTER);
        pack();
    }

    // ===================== SEARCH ==========================
    private void txtSearchActionPerformed(ActionEvent actionEvent) {
        btnSearchActionPerformed(actionEvent);
    }

    private void btnSearchActionPerformed(ActionEvent actionEvent) {
        String key = txtSearch.getText().trim();
        loadTableData(controller.search(key));
    }

    private void btnAllMenuActionPerformed(ActionEvent actionEvent) {
        loadTableData(controller.findAll());
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
            AddMenu editScreen = new AddMenu(perm.get());
            editScreen.setVisible(true);
            editScreen.setLocationRelativeTo(null);
            this.dispose();
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

    public static void main(String args[]) {
        EventQueue.invokeLater(() -> {
            AllMenu m = new AllMenu();
            m.setVisible(true);
            m.setLocationRelativeTo(null);
        });
    }

    // VARIABLES
    private javax.swing.JButton btnAddmenu;
    private javax.swing.JButton btnAllMenu;
    private javax.swing.JButton btnSearch;
    private javax.swing.JButton btnEdit;
    private javax.swing.JButton btnDelete;

    private javax.swing.JPanel pnlAllmenu;
    private javax.swing.JScrollPane scrollPaneTable;
    private javax.swing.JTable tblAllmenu;
    private javax.swing.JTextField txtSearch;
}
