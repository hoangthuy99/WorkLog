package com.ra.View.department;

import com.ra.Controller.DepartmentController;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.awt.*;

import com.ra.Model.Entity.Department;
import com.ra.Model.Entity.Project;
import com.ra.Model.Entity.Tasks;
import com.ra.View.department.AddDepartment.DepartmentListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

// IMPLEMENT LISTENER
public class AllDepartment extends JPanel implements DepartmentListener {

    private final DepartmentController departmentController = new DepartmentController();

    private static final Logger logger =
            Logger.getLogger(AllDepartment.class.getName());

    private JPanel contentPanel;

    public AllDepartment() {
        // Sử dụng GridBagLayout để căn giữa contentPanel
        setLayout(new GridBagLayout());

        initComponents();

        // TẠO VÀ CĂN GIỮA PANEL BẢNG
        contentPanel = createTableViewPanel();

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.weightx = 1.0; gbc.weighty = 1.0;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        // Thêm contentPanel vào JPanel chính (this)
        add(contentPanel, gbc);

        // GẮN SỰ KIỆN CHO CÁC NÚT KHÔNG TỰ SINH
        btnEdit.addActionListener(this::btnEditActionPerformed);
        btnDelete.addActionListener(this::btnDeleteActionPerformed);
        btnAddDepartment.addActionListener(this::btnAddDepartmentActionPerformed);

        // GỌI HÀM TẢI DỮ LIỆU KHI KHỞI TẠO
        loadTable(departmentController.findAll());
    }

    // ==========================================================
    // LOGIC POP-UP DIALOG CHO EDIT
    // ==========================================================

    private void showAddEditDialog(Integer id) {
        try {
            // Lấy cửa sổ cha của AllDepartment (JFrame chứa nó)
            Frame owner = JOptionPane.getFrameForComponent(this);

            // Tạo JDialog (Pop-up Window)
            JDialog dialog = new JDialog(owner, (id == null ? "新部署作成" : "部署編集"), true);

            // Tạo Panel AddDepartment, truyền JDialog làm parentWindow, và truyền chính AllDepartment (this) làm Listener
            AddDepartment form = new AddDepartment(dialog, this, id);

            dialog.setContentPane(form);
            dialog.pack();
            dialog.setLocationRelativeTo(owner);

            // Bắt sự kiện đóng cửa sổ (ví dụ: nhấn nút X) để refresh bảng
            dialog.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    onDepartmentOperationComplete();
                }
            });

            dialog.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "ダイアログを開く際に致命的なエラーが発生しました: " + e.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
            logger.severe("Fatal error opening AddEditDialog: " + e.toString());
        }
    }

    // IMPLEMENTATION CỦA INTERFACE LISTENER
    @Override
    public void onDepartmentOperationComplete() {
        loadTable(departmentController.findAll());
    }

    // ==========================================================
    // PHƯƠNG THỨC TẠO PANEL VIEW
    // ==========================================================
    private JPanel createTableViewPanel() {
        JPanel viewPanel = new JPanel();
        viewPanel.setBackground(new Color(255, 255, 255));

        GroupLayout contentLayout = new GroupLayout(viewPanel);
        viewPanel.setLayout(contentLayout);

        viewPanel.add(lbKeyword);
        viewPanel.add(btnSearch);
        viewPanel.add(btnAddDepartment);
        viewPanel.add(txtSearch);
        viewPanel.add(scrAdddepartment);
        viewPanel.add(btnEdit);
        viewPanel.add(btnDelete);

        contentLayout.setHorizontalGroup(
                contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(contentLayout.createSequentialGroup()
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.TRAILING, false)
                                        .addGroup(GroupLayout.Alignment.LEADING, contentLayout.createSequentialGroup()
                                                .addGap(54, 54, 54)
                                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(scrAdddepartment, GroupLayout.Alignment.TRAILING, GroupLayout.PREFERRED_SIZE, 452, GroupLayout.PREFERRED_SIZE)
                                                        .addGroup(contentLayout.createSequentialGroup()
                                                                .addComponent(lbKeyword, GroupLayout.PREFERRED_SIZE, 69, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(btnSearch, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .addComponent(btnAddDepartment, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE))))
                                        .addGroup(contentLayout.createSequentialGroup()
                                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(btnEdit, GroupLayout.PREFERRED_SIZE, 75, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnDelete, GroupLayout.PREFERRED_SIZE, 74, GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)))
                                .addContainerGap(69, Short.MAX_VALUE))
        );
        contentLayout.setVerticalGroup(
                contentLayout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(contentLayout.createSequentialGroup()
                                .addGap(38, 38, 38)
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(lbKeyword)
                                        .addComponent(btnSearch)
                                        .addComponent(btnAddDepartment)
                                        .addComponent(txtSearch, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addComponent(scrAdddepartment, GroupLayout.PREFERRED_SIZE, 267, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(contentLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(btnEdit)
                                        .addComponent(btnDelete))
                                .addContainerGap(47, Short.MAX_VALUE))
        );
        return viewPanel;
    }


    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lbKeyword = new JLabel();
        btnSearch = new JButton();
        btnAddDepartment = new JButton();
        txtSearch = new JTextField();
        scrAdddepartment = new JScrollPane();
        tblAddDepartment = new JTable();
        btnEdit = new JButton();
        btnDelete = new JButton();

        setBackground(new Color(255, 255, 255));

        lbKeyword.setText("キーワード");
        btnSearch.setText("検索");
        btnSearch.addActionListener(this::btnSearchActionPerformed);

        btnAddDepartment.setText("新部署作成");

        tblAddDepartment.setModel(new DefaultTableModel(
                new Object [][] {},
                new String [] {
                        "ID", "部署名", "プロジェクト名", "タスク名"
                }
        ) {
            Class[] types = new Class [] {
                    Integer.class, Object.class, Object.class, String.class
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        });
        scrAdddepartment.setViewportView(tblAddDepartment);

        btnEdit.setText("編集");
        btnDelete.setText("削除");

        GroupLayout layout = new GroupLayout(this);
    }// </editor-fold>//GEN-END:initComponents

    // 5. CHUYỂN CÁC PHƯƠNG THỨC XỬ LÝ SỰ KIỆN (HANDLER)

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {
        String keyword = txtSearch.getText().trim();
        List<Department> list = departmentController.search(keyword);
        loadTable(list);
    }

    private void btnAddDepartmentActionPerformed(java.awt.event.ActionEvent evt) {
        showAddEditDialog(null);
    }

    private void btnEditActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = tblAddDepartment.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "編集する部署を選択してください。", "注意", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object idValue = tblAddDepartment.getModel().getValueAt(selectedRow, 0);
        int id = -1;

        try {
            if (idValue instanceof Integer) {
                id = (Integer) idValue;
            } else if (idValue instanceof String) {
                id = Integer.parseInt((String) idValue);
            } else {
                throw new ClassCastException("ID data type is unexpected: " + idValue.getClass().getName());
            }

            showAddEditDialog(id);

        } catch (ClassCastException | NumberFormatException e) {
            String errorMsg = "IDデータの形式エラー。IDが正しく設定されているか確認してください。";
            JOptionPane.showMessageDialog(this, errorMsg, "エラー", JOptionPane.ERROR_MESSAGE);
            logger.severe("Error getting ID from table cell: " + e.toString());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "編集ウィンドウを開く際にエラーが発生しました。", "エラー", JOptionPane.ERROR_MESSAGE);
            logger.severe("Error opening Edit dialog: " + e.toString());
        }
    }

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = tblAddDepartment.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "削除する部署を選択してください。", "注意", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Object idValue = tblAddDepartment.getModel().getValueAt(selectedRow, 0);
        int id;
        try {
            if (idValue instanceof Integer) {
                id = (Integer) idValue;
            } else if (idValue instanceof String) {
                id = Integer.parseInt((String) idValue);
            } else {
                throw new ClassCastException();
            }
        } catch (ClassCastException | NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "IDデータの形式エラー。", "エラー", JOptionPane.ERROR_MESSAGE);
            return;
        }


        int confirm = JOptionPane.showConfirmDialog(this,
                "この部署を削除してもよろしいですか？", "削除確認", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            try {
                departmentController.delete(id);
                JOptionPane.showMessageDialog(this, "部署が削除されました。");
                loadTable(departmentController.findAll());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "削除中にエラーが発生しました: " + e.getMessage(), "エラー", JOptionPane.ERROR_MESSAGE);
                logger.severe("Delete error: " + e.getMessage());
            }
        }
    }


    // 6. PHƯƠNG THỨC LOAD TABLE

    private void loadTable(List<Department> list) {

        String[] columns = {"ID", "部署名", "プロジェクト名", "タスク名"};
        DefaultTableModel model = new DefaultTableModel(columns, 0);

        for (Department d : list) {

            String projectNames = (d.getProjects() == null) ? "" :
                    d.getProjects().stream()
                            .map(Project::getName)
                            .reduce("", (a, b) -> a + (a.isEmpty() ? "" : ", ") + b);

            String taskNames = (d.getTasks() == null) ? "" :
                    d.getTasks().stream()
                            .map(Tasks::getName)
                            .reduce("", (a, b) -> a + (a.isEmpty() ? "" : ", ") + b);

            model.addRow(new Object[]{
                    d.getId(),
                    d.getName(),
                    projectNames,
                    taskNames
            });
        }

        tblAddDepartment.setModel(model);

        // Ẩn cột ID (cột 0)
        if (tblAddDepartment.getColumnModel().getColumnCount() > 0) {
            tblAddDepartment.getColumnModel().getColumn(0).setMinWidth(0);
            tblAddDepartment.getColumnModel().getColumn(0).setMaxWidth(0);
            tblAddDepartment.getColumnModel().getColumn(0).setPreferredWidth(0);
        }
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private JButton btnAddDepartment;
    private JButton btnDelete;
    private JButton btnEdit;
    private JButton btnSearch;
    private JLabel lbKeyword;
    private JScrollPane scrAdddepartment;
    private JTable tblAddDepartment;
    private JTextField txtSearch;
    // End of variables declaration//GEN-END:variables
}