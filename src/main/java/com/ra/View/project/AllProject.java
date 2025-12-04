/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package com.ra.View.project;

import javax.swing.*;
// Thêm các thư viện cần thiết cho Layout
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

/**
 *
 * @author Admin
 */
public class AllProject extends JPanel {

    // Khai báo Content Panel để chứa nội dung cũ
    private JPanel contentPanel;

    /**
     * Creates new form AllProject1
     */
    public AllProject() {
        initComponents();
        // GỌI PHƯƠNG THỨC CĂN GIỮA
        applyCenteredLayout();
        // Cần thêm logic tải dữ liệu và gán sự kiện cho các nút ở đây nếu đây là code thật.
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
        contentPanel.add(txtProject);
        contentPanel.add(btnSearch);
        contentPanel.add(btnAll);
        contentPanel.add(btnCreateDepartment);
        contentPanel.add(jScrollPane1);
        contentPanel.add(jSpinner1);
        contentPanel.add(btnEdit);
        contentPanel.add(btnDelete);
        contentPanel.add(lbProjectName);

        // TÁI TẠO LẠI BỐ CỤC CŨ TRÊN contentLayout
        contentLayout.setHorizontalGroup(
                contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(GroupLayout.Alignment.TRAILING, contentLayout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jSpinner1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnEdit, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 84, GroupLayout.PREFERRED_SIZE)
                                .addGap(30, 30, 30))
                        .addGroup(GroupLayout.Alignment.TRAILING, contentLayout.createSequentialGroup()
                                .addGap(32, 32, 32)
                                .addComponent(lbProjectName)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtProject, GroupLayout.PREFERRED_SIZE, 134, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnAll, GroupLayout.PREFERRED_SIZE, 56, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 68, Short.MAX_VALUE)
                                .addComponent(btnCreateDepartment, GroupLayout.PREFERRED_SIZE, 135, GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29))
                        .addGroup(contentLayout.createSequentialGroup()
                                .addGap(17, 17, 17)
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 567, GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        contentLayout.setVerticalGroup(
                contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(contentLayout.createSequentialGroup()
                                .addGap(65, 65, 65)
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbProjectName)
                                        .addComponent(txtProject, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnSearch)
                                        .addComponent(btnAll)
                                        .addComponent(btnCreateDepartment))
                                .addGap(18, 18, 18)
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 204, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 33, Short.MAX_VALUE)
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jSpinner1, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
                                        .addComponent(btnEdit)
                                        .addComponent(btnDelete))
                                .addGap(15, 15, 15))
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

        txtProject = new JTextField();
        btnSearch = new JButton();
        btnAll = new JButton();
        btnCreateDepartment = new JButton();
        jScrollPane1 = new JScrollPane();
        tblAllDepartment = new JTable();
        jSpinner1 = new JSpinner();
        btnEdit = new JButton();
        btnDelete = new JButton();
        lbProjectName = new JLabel();

        setBackground(new java.awt.Color(255, 255, 255));

        txtProject.setText("キーワード入力");
        txtProject.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtProjectActionPerformed(evt);
            }
        });

        btnSearch.setText("検索");

        btnAll.setText("全て");

        btnCreateDepartment.setText("プロジェクト作成");

        tblAllDepartment.setModel(new javax.swing.table.DefaultTableModel(
                new Object [][] {
                        {null, null, null},
                        {null, null, null},
                        {null, null, null},
                        {null, null, null}
                },
                new String [] {
                        "プロジェクト名", "部署名", "タスク名"
                }
        ));
        jScrollPane1.setViewportView(tblAllDepartment);

        btnEdit.setBackground(new java.awt.Color(204, 255, 255));
        btnEdit.setText("編集");

        btnDelete.setBackground(new java.awt.Color(255, 204, 204));
        btnDelete.setText("削除");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        lbProjectName.setText("キーワード");

        // Giữ layout tự động tạo (GroupLayout) nhưng nó sẽ bị ghi đè bởi applyCenteredLayout().
        GroupLayout layout = new GroupLayout(this);
        this.setLayout(layout);
        // Loại bỏ các khai báo GroupLayout tự động để tránh xung đột với GridBagLayout
        // và thay bằng null layout tạm thời
        this.setLayout(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txtProjectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtProjectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtProjectActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnDeleteActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnAll;
    private JButton btnCreateDepartment;
    private JButton btnDelete;
    private JButton btnEdit;
    private JButton btnSearch;
    private JScrollPane jScrollPane1;
    private JSpinner jSpinner1;
    private JLabel lbProjectName;
    private JTable tblAllDepartment;
    private JTextField txtProject;
    // End of variables declaration//GEN-END:variables
}