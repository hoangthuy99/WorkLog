package com.ra.View.report;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.EventObject;
import java.util.List;

/**
 * ReportSummaryPanel - A GUI module for viewing and exporting summarized attendance data.
 * Added: Checkbox for bulk selection, and "Mass Delete" button.
 */
public class ReportSummaryPanel extends JPanel {

    // UI Components
    private JPanel viewFilterPanel;
    private JTable dataTable;
    private JScrollPane tableScrollPane;
    private JPanel actionFooter;

    // Buttons
    private JToggleButton byDeptButton;
    private JToggleButton byProjectButton;
    private JToggleButton byDeptProjectButton;
    private JButton exportCsvButton;
    private JButton bulkDeleteButton; // NEW: Nút xóa hàng loạt
    private JButton backButton;
    private JButton logoutButton;

    // Table models for different view modes
    private DefaultTableModel deptTableModel;
    private DefaultTableModel projectTableModel;
    private DefaultTableModel deptProjectTableModel;

    // Constants for Column Indexes
    private final int CHECKBOX_COLUMN_INDEX = 0; // Cột đầu tiên
    private final int ACTION_COLUMN_INDEX = 6;   // Cột Thao tác (sau khi thêm Checkbox)

    public ReportSummaryPanel() {
        initializeUI();
        setupEventListeners();
    }

    private void initializeUI() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(800, 600));

        Font japaneseFont = new Font("Meiryo", Font.PLAIN, 12);

        createViewFilterPanel(japaneseFont);
        createDataTable(japaneseFont);
        createActionFooter(japaneseFont);
    }

    private void createViewFilterPanel(Font font) {
        viewFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));

        byDeptButton = new JToggleButton("部署別");
        byProjectButton = new JToggleButton("プロジェクト別");
        byDeptProjectButton = new JToggleButton("部署別プロジェクト別");

        JToggleButton[] filterButtons = {byDeptButton, byProjectButton, byDeptProjectButton};
        for (JToggleButton button : filterButtons) {
            button.setFont(font);
            button.setPreferredSize(new Dimension(150, 30));
        }

        byDeptButton.setSelected(true);
        byDeptButton.setBackground(Color.GREEN);

        viewFilterPanel.add(byDeptButton);
        viewFilterPanel.add(byProjectButton);
        viewFilterPanel.add(byDeptProjectButton);

        add(viewFilterPanel, BorderLayout.NORTH);
    }

    private void createDataTable(Font font) {
        createTableModels();

        // Sử dụng deptModel mặc định
        dataTable = new JTable(deptTableModel);
        dataTable.setFont(font);
        dataTable.setRowHeight(30);
        dataTable.getTableHeader().setFont(font.deriveFont(Font.BOLD));

        // Áp dụng renderer và editor cho cột Checkbox và Action
        applyTableCustomComponents(deptTableModel.getColumnCount());

        tableScrollPane = new JScrollPane(dataTable);
        tableScrollPane.setPreferredSize(new Dimension(780, 400));

        add(tableScrollPane, BorderLayout.CENTER);
    }

    private void createTableModels() {
        // Cấu trúc mới: Thêm cột "選択" (Chọn) ở đầu
        String[] deptColumns = {"選択", "部署コード", "部署名", "作業コード", "作業名", "作業時間", "操作"};
        deptTableModel = new DefaultTableModel(deptColumns, 0) {
            // Đặt kiểu dữ liệu cho cột Checkbox (cột 0) là Boolean
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == CHECKBOX_COLUMN_INDEX) {
                    return Boolean.class;
                }
                return super.getColumnClass(columnIndex);
            }

            @Override
            public boolean isCellEditable(int row, int column) {
                // Checkbox (0) và Action (6) đều có thể chỉnh sửa
                return column == CHECKBOX_COLUMN_INDEX || column == ACTION_COLUMN_INDEX;
            }
        };

        // Dữ liệu mẫu (Cột đầu tiên là Boolean.FALSE cho Checkbox)
        deptTableModel.addRow(new Object[]{Boolean.FALSE, "D001", "開発部", "T001", "UI Design", "120h", null});
        deptTableModel.addRow(new Object[]{Boolean.FALSE, "D001", "開発部", "T002", "Backend Dev", "80h", null});
        deptTableModel.addRow(new Object[]{Boolean.FALSE, "D002", "営業部", "T003", "顧客対応", "60h", null});
        deptTableModel.addRow(new Object[]{Boolean.FALSE, "D002", "営業部", "T004", "資料作成", "40h", null});


        String[] projectColumns = {"選択", "プロジェクトコード", "プロジェクト名","作業コード","作業名", "作業時間", "操作"};
        projectTableModel = new DefaultTableModel(projectColumns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == CHECKBOX_COLUMN_INDEX) return Boolean.class;
                return super.getColumnClass(columnIndex);
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == CHECKBOX_COLUMN_INDEX || column == ACTION_COLUMN_INDEX;
            }
        };

        projectTableModel.addRow(new Object[]{Boolean.FALSE, "P001", "システム開発", "T005", "開発", "200h", null});
        projectTableModel.addRow(new Object[]{Boolean.FALSE, "P002", "顧客管理", "T006", "code", "100h", null});
        projectTableModel.addRow(new Object[]{Boolean.FALSE, "P003", "インフラ整備", "T007", "desin", "150h", null});

        String[] deptProjectColumns = {"選択", "部署コード", "部署名", "プロジェクトコード", "プロジェクト名", "作業時間", "操作"};
        deptProjectTableModel = new DefaultTableModel(deptProjectColumns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == CHECKBOX_COLUMN_INDEX) return Boolean.class;
                return super.getColumnClass(columnIndex);
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == CHECKBOX_COLUMN_INDEX || column == ACTION_COLUMN_INDEX;
            }
        };

        deptProjectTableModel.addRow(new Object[]{Boolean.FALSE, "D001", "開発部", "P001", "システム開発", "120h", null});
        deptProjectTableModel.addRow(new Object[]{Boolean.FALSE, "D001", "開発部", "P002", "顧客管理", "80h", null});
        deptProjectTableModel.addRow(new Object[]{Boolean.FALSE, "D002", "営業部", "P002", "顧客管理", "100h", null});
    }

    private void createActionFooter(Font font) {
        actionFooter = new JPanel(new BorderLayout());
        actionFooter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left side - Export and Bulk Delete button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        exportCsvButton = new JButton("CSV出力");
        bulkDeleteButton = new JButton("一括削除"); // Nút Xóa hàng loạt

        exportCsvButton.setFont(font);
        bulkDeleteButton.setFont(font);
        bulkDeleteButton.setBackground(new Color(255, 100, 100)); // Màu đỏ nhạt cho Xóa

        exportCsvButton.setPreferredSize(new Dimension(120, 30));
        bulkDeleteButton.setPreferredSize(new Dimension(120, 30));

        leftPanel.add(exportCsvButton);
        leftPanel.add(bulkDeleteButton);

        // Right side - System buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        backButton = new JButton("戻り");
        logoutButton = new JButton("ログアウト");

        JButton[] systemButtons = {backButton, logoutButton};
        for (JButton button : systemButtons) {
            button.setFont(font);
            button.setPreferredSize(new Dimension(100, 30));
            rightPanel.add(button);
        }

        actionFooter.add(leftPanel, BorderLayout.WEST);
        actionFooter.add(rightPanel, BorderLayout.EAST);

        add(actionFooter, BorderLayout.SOUTH);
    }

    private void setupEventListeners() {
        byDeptButton.addActionListener(new ViewFilterListener());
        byProjectButton.addActionListener(new ViewFilterListener());
        byDeptProjectButton.addActionListener(new ViewFilterListener());

        exportCsvButton.addActionListener(e -> exportToCsv());
        bulkDeleteButton.addActionListener(e -> handleBulkDeleteAction()); // NEW: Sự kiện Xóa hàng loạt
        backButton.addActionListener(e -> goBack());
        logoutButton.addActionListener(e -> logout());
    }

    /**
     * Applies the custom Renderer and Editor to the Checkbox and Action Columns.
     */
    private void applyTableCustomComponents(int totalColumns) {
        // Áp dụng cho cột Checkbox (cột 0)
        if (totalColumns > CHECKBOX_COLUMN_INDEX) {
            // Đặt tiêu đề cột 0 là một Checkbox để chọn/bỏ chọn tất cả
            JCheckBox selectAllBox = new JCheckBox();
            selectAllBox.addActionListener(new SelectAllListener(dataTable, CHECKBOX_COLUMN_INDEX));
            dataTable.getColumnModel().getColumn(CHECKBOX_COLUMN_INDEX).setHeaderRenderer(new CheckboxHeaderRenderer(selectAllBox));
            dataTable.getColumnModel().getColumn(CHECKBOX_COLUMN_INDEX).setPreferredWidth(50);
        }

        // Áp dụng cho cột Action (cột cuối cùng)
        if (totalColumns > ACTION_COLUMN_INDEX) {
            dataTable.getColumnModel().getColumn(ACTION_COLUMN_INDEX).setCellRenderer(new ButtonRenderer());
            dataTable.getColumnModel().getColumn(ACTION_COLUMN_INDEX).setCellEditor(new ButtonEditor(new JCheckBox()));
            dataTable.getColumnModel().getColumn(ACTION_COLUMN_INDEX).setPreferredWidth(150);
        }
    }

    /**
     * Inner class to handle view filter button clicks
     */
    private class ViewFilterListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JToggleButton source = (JToggleButton) e.getSource();

            byDeptButton.setBackground(null);
            byProjectButton.setBackground(null);
            byDeptProjectButton.setBackground(null);

            source.setBackground(Color.GREEN);

            DefaultTableModel newModel = null;
            if (source == byDeptButton) {
                newModel = deptTableModel;
            } else if (source == byProjectButton) {
                newModel = projectTableModel;
            } else if (source == byDeptProjectButton) {
                newModel = deptProjectTableModel;
            }

            if (newModel != null) {
                dataTable.setModel(newModel);
                // CRITICAL: Re-apply custom components every time the model changes
                applyTableCustomComponents(newModel.getColumnCount());
            }

            byDeptButton.setSelected(source == byDeptButton);
            byProjectButton.setSelected(source == byProjectButton);
            byDeptProjectButton.setSelected(source == byDeptProjectButton);
        }
    }

    // =========================================================================
    //  --- BULK DELETE LOGIC ---
    // =========================================================================

    /**
     * Handles the bulk delete action for all selected rows.
     */
    private void handleBulkDeleteAction() {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        List<Integer> selectedRows = new ArrayList<>();

        // 1. Collect selected rows (iterate backwards to handle index changes during deletion)
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            Boolean isSelected = (Boolean) model.getValueAt(i, CHECKBOX_COLUMN_INDEX);
            if (isSelected != null && isSelected.equals(Boolean.TRUE)) {
                selectedRows.add(i);
            }
        }

        if (selectedRows.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Vui lòng chọn ít nhất một hàng để xóa.",
                    "Lỗi Xóa",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Confirmation Dialog
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn XÓA (" + selectedRows.size() + ") hàng đã chọn không?",
                "Xác Nhận Xóa Hàng Loạt",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // 3. Perform Deletion
            for (int rowToDelete : selectedRows) {
                model.removeRow(rowToDelete);
            }
            JOptionPane.showMessageDialog(this,
                    "Đã xóa thành công " + selectedRows.size() + " hàng.",
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // =========================================================================
    //  --- SINGLE ACTION HANDLERS ---
    // =========================================================================

    private void handleEditAction(int row, String dataIdentifier) {
        JOptionPane.showMessageDialog(this,
                "Chức năng SỬA (編集) cho dòng: " + (row + 1) + "\nDữ liệu: " + dataIdentifier,
                "Sửa Dữ Liệu Báo Cáo",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleDeleteAction(int row, String dataIdentifier, DefaultTableModel model) {
        int confirm = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc chắn muốn XÓA (削除) dữ liệu này không?\n" + dataIdentifier,
                "Xác Nhận Xóa",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            model.removeRow(row);
            JOptionPane.showMessageDialog(this,
                    "Đã xóa thành công dòng: " + (row + 1),
                    "Thành công",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // =========================================================================
    //  --- CUSTOM TABLE CELL COMPONENTS (Renderer & Editor) ---
    // =========================================================================

    /**
     * Renderer for the buttons (Edit/Delete).
     */
    private class ButtonRenderer extends JPanel implements TableCellRenderer {
        private final JButton editButton;
        private final JButton deleteButton;

        public ButtonRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0));
            editButton = new JButton("編集");
            deleteButton = new JButton("削除");

            editButton.setFont(editButton.getFont().deriveFont(10f));
            deleteButton.setFont(deleteButton.getFont().deriveFont(10f));

            editButton.setBackground(new Color(255, 230, 180));
            deleteButton.setBackground(new Color(255, 200, 200));

            add(editButton);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }

    /**
     * Editor for the buttons (handles clicks).
     */
    private class ButtonEditor extends AbstractCellEditor implements TableCellEditor, ActionListener {
        private final JPanel panel;
        private final JButton editButton;
        private final JButton deleteButton;
        private JTable table;
        private int row;
        private Object editorValue;

        public ButtonEditor(JCheckBox checkBox) {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            editButton = new JButton("編集");
            deleteButton = new JButton("削除");

            editButton.setFont(editButton.getFont().deriveFont(10f));
            deleteButton.setFont(deleteButton.getFont().deriveFont(10f));

            editButton.setBackground(new Color(255, 230, 180));
            deleteButton.setBackground(new Color(255, 200, 200));

            editButton.addActionListener(this);
            deleteButton.addActionListener(this);

            panel.add(editButton);
            panel.add(deleteButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            this.table = table;
            this.row = row;
            this.editorValue = value;
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return editorValue;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            fireEditingStopped();

            String action = e.getActionCommand();
            DefaultTableModel model = (DefaultTableModel) table.getModel();

            // Lấy mã định danh từ cột đầu tiên (ví dụ: Mã Phòng Ban)
            Object identifier = model.getValueAt(row, 1);
            String dataIdentifier = model.getColumnName(1) + ": " + identifier;

            if (action.equals("編集")) {
                handleEditAction(row, dataIdentifier);
            } else if (action.equals("削除")) {
                handleDeleteAction(row, dataIdentifier, model);
            }
        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            return false;
        }
    }

    /**
     * Renderer for the Checkbox in the table header (Select All).
     */
    private class CheckboxHeaderRenderer extends JComponent implements TableCellRenderer {
        private final JCheckBox selectAllBox;

        public CheckboxHeaderRenderer(JCheckBox selectAllBox) {
            this.selectAllBox = selectAllBox;
            this.selectAllBox.setHorizontalAlignment(SwingConstants.CENTER);
            this.selectAllBox.setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            JComponent header = (JComponent) table.getTableHeader().getDefaultRenderer().getTableCellRendererComponent(table, value, isSelected, hasFocus, -1, column);
            this.selectAllBox.setBackground(header.getBackground());
            this.selectAllBox.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            this.selectAllBox.setText(value != null ? value.toString() : "");
            return this.selectAllBox;
        }
    }

    /**
     * Listener to handle Select All/Deselect All action on the header checkbox.
     */
    private class SelectAllListener implements ActionListener {
        private final JTable table;
        private final int checkboxColumn;

        public SelectAllListener(JTable table, int checkboxColumn) {
            this.table = table;
            this.checkboxColumn = checkboxColumn;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            JCheckBox headerBox = (JCheckBox) e.getSource();
            DefaultTableModel model = (DefaultTableModel) table.getModel();
            boolean checked = headerBox.isSelected();

            for (int i = 0; i < model.getRowCount(); i++) {
                model.setValueAt(checked, i, checkboxColumn);
            }
            // Fire event to update the view immediately
            table.getTableHeader().repaint();
        }
    }

    // =========================================================================
    //  --- PLACEHOLDER LOGIC ---
    // =========================================================================

    private void exportToCsv() {
        JOptionPane.showMessageDialog(this,
                "Chức năng Xuất CSV (CSV出力) cần được code để ghi dữ liệu bảng hiện tại vào file.",
                "Xuất CSV",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void goBack() {
        JOptionPane.showMessageDialog(this,
                "Chức năng Quay lại (戻り) cần được code để chuyển sang màn hình trước (ví dụ: dùng CardLayout).",
                "Quay lại",
                JOptionPane.INFORMATION_MESSAGE);
    }

    private void logout() {
        JOptionPane.showMessageDialog(this,
                "Chức năng Đăng xuất (ログアウト) cần được code để xóa phiên người dùng và chuyển về màn Login.",
                "Đăng xuất",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Main method for testing the UI
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            UIManager.put("Button.font", new Font("Meiryo", Font.PLAIN, 12));
            UIManager.put("Label.font", new Font("Meiryo", Font.PLAIN, 12));
            UIManager.put("Table.font", new Font("Meiryo", Font.PLAIN, 12));
            UIManager.put("TableHeader.font", new Font("Meiryo", Font.BOLD, 12));
        } catch (Exception e) {
            e.printStackTrace();
        }

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Attendance Report Summary - With Bulk Actions");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            ReportSummaryPanel reportPanel = new ReportSummaryPanel();
            frame.add(reportPanel);

            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }
}