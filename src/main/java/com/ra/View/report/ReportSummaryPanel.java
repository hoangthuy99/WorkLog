package com.ra.View.report;
import com.ra.Controller.ReportController;
import com.ra.DTO.report.ReportRowDTO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * ReportSummaryPanel - A GUI module for viewing and exporting summarized attendance data.
 * Added: Checkbox for bulk selection, and "Mass Delete" button.
 */
public class ReportSummaryPanel extends JPanel {
    private final ReportController reportController = new ReportController();
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
    private JButton bulkDeleteButton; // Nút xóa hàng loạt
    // private JButton backButton; // ĐÃ BỎ

    // Table models for different view modes
    private DefaultTableModel deptTableModel;
    private DefaultTableModel projectTableModel;
    private DefaultTableModel deptProjectTableModel;


    private String formatMinutes(int m) {
        return (m / 60) + ":" + String.format("%02d", m % 60);
    }
    private void loadDeptData() {
        deptTableModel.setRowCount(0);

        for (ReportRowDTO r : reportController.summaryByDept()) {
            deptTableModel.addRow(new Object[]{
                    false,
                    r.deptCode,
                    r.deptName,
                    r.taskCode,
                    r.taskName,
                    formatMinutes(r.workMinutes)
            });
        }
    }

    private void loadProjectData() {
        projectTableModel.setRowCount(0);

        for (ReportRowDTO r : reportController.summaryByProject()) {
            projectTableModel.addRow(new Object[]{
                    false,
                    r.projectCode,
                    r.projectName,
                    r.taskCode,
                    r.taskName,
                    formatMinutes(r.workMinutes)
            });
        }
    }

    private void loadDeptProjectData() {
        deptProjectTableModel.setRowCount(0);

        for (ReportRowDTO r : reportController.summaryDeptProject()) {
            deptProjectTableModel.addRow(new Object[]{
                    false,
                    r.deptCode,
                    r.deptName,
                    r.projectCode,
                    r.projectName,
                    formatMinutes(r.workMinutes)
            });
        }
    }



    // Constants for Column Indexes
    private final int CHECKBOX_COLUMN_INDEX = 0; // Cột đầu tiên

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

        // Áp dụng renderer và editor cho cột Checkbox
        applyTableCustomComponents(deptTableModel.getColumnCount());

        tableScrollPane = new JScrollPane(dataTable);
        tableScrollPane.setPreferredSize(new Dimension(780, 400));

        add(tableScrollPane, BorderLayout.CENTER);
        loadDeptData();

    }

    private void createTableModels() {
        // Cột cuối là "作業時間"
        String[] deptColumns = {"選択", "部署コード", "部署名", "タスクコード", "タスク名", "作業時間"};
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
                // Chỉ còn Checkbox (0) là có thể chỉnh sửa
                return column == CHECKBOX_COLUMN_INDEX;
            }
        };

        String[] projectColumns = {"選択", "プロジェクトコード", "プロジェクト名","タスクコード","タスク名", "作業時間"};
        projectTableModel = new DefaultTableModel(projectColumns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == CHECKBOX_COLUMN_INDEX) return Boolean.class;
                return super.getColumnClass(columnIndex);
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == CHECKBOX_COLUMN_INDEX;
            }
        };

        String[] deptProjectColumns = {"選択", "部署コード", "部署名", "プロジェクトコード", "プロジェクト名", "作業時間"};
        deptProjectTableModel = new DefaultTableModel(deptProjectColumns, 0) {
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                if (columnIndex == CHECKBOX_COLUMN_INDEX) return Boolean.class;
                return super.getColumnClass(columnIndex);
            }
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == CHECKBOX_COLUMN_INDEX;
            }
        };
    }

    private void createActionFooter(Font font) {
        actionFooter = new JPanel(new BorderLayout());
        actionFooter.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Left side - Export and Bulk Delete button
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        exportCsvButton = new JButton("CSV出力");
        bulkDeleteButton = new JButton("削除");

        exportCsvButton.setFont(font);
        bulkDeleteButton.setFont(font);
        bulkDeleteButton.setBackground(new Color(255, 100, 100)); // Màu đỏ nhạt cho Xóa

        exportCsvButton.setPreferredSize(new Dimension(120, 30));
        bulkDeleteButton.setPreferredSize(new Dimension(120, 30));

        leftPanel.add(exportCsvButton);
        leftPanel.add(bulkDeleteButton);

        // actionFooter chỉ còn leftPanel được căn lề WEST

        actionFooter.add(leftPanel, BorderLayout.WEST);
        // Không thêm rightPanel vì nó trống

        add(actionFooter, BorderLayout.SOUTH);
    }

    private void setupEventListeners() {
        byDeptButton.addActionListener(new ViewFilterListener());
        byProjectButton.addActionListener(new ViewFilterListener());
        byDeptProjectButton.addActionListener(new ViewFilterListener());

        exportCsvButton.addActionListener(e -> exportToCsv());
        bulkDeleteButton.addActionListener(e -> handleBulkDeleteAction()); // Sự kiện Xóa hàng loạt
        // backButton.addActionListener(e -> goBack()); // ĐÃ BỎ
    }

    /**
     * Applies the custom Renderer and Editor to the Checkbox column.
     * Cột Action đã bị loại bỏ.
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
                loadDeptData();
            } else if (source == byProjectButton) {
                newModel = projectTableModel;
                loadProjectData();
            } else if (source == byDeptProjectButton) {
                newModel = deptProjectTableModel;
                loadDeptProjectData();
            }

            if (newModel != null) {
                dataTable.setModel(newModel);
                applyTableCustomComponents(newModel.getColumnCount());

                dataTable.getTableHeader().repaint();
                tableScrollPane.revalidate();
                tableScrollPane.repaint();
            }

            byDeptButton.setSelected(source == byDeptButton);
            byProjectButton.setSelected(source == byProjectButton);
            byDeptProjectButton.setSelected(source == byDeptProjectButton);
        }
    }

    /**
     * Handles the bulk delete action for all selected rows.
     */
    private void handleBulkDeleteAction() {
        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();
        List<Integer> selectedRows = new ArrayList<>();

        // 1. Collect selected rows (iterate backwards to handle index changes during deletion)
        for (int i = model.getRowCount() - 1; i >= 0; i--) {
            // Đảm bảo cột 0 là Checkbox
            if (model.getColumnCount() > CHECKBOX_COLUMN_INDEX && model.getColumnClass(CHECKBOX_COLUMN_INDEX) == Boolean.class) {
                Boolean isSelected = (Boolean) model.getValueAt(i, CHECKBOX_COLUMN_INDEX);
                if (isSelected != null && isSelected.equals(Boolean.TRUE)) {
                    selectedRows.add(i);
                }
            }
        }

        if (selectedRows.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "削除する行を選択してください。",
                    "削除エラー",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. Confirmation Dialog
        int confirm = JOptionPane.showConfirmDialog(this,
                "選択された " + selectedRows.size() + " 行を本当に削除してもよろしいですか？",
                "一括削除確認",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            // 3. Perform Deletion
            // Xóa từ hàng cuối cùng lên để tránh lỗi index
            selectedRows.stream().sorted((a, b) -> b.compareTo(a)).forEach(model::removeRow);

            JOptionPane.showMessageDialog(this,
                    selectedRows.size() + " 行を削除しました。",
                    "完了",
                    JOptionPane.INFORMATION_MESSAGE);
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
    private void exportToCsv() {

        DefaultTableModel model = (DefaultTableModel) dataTable.getModel();

        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("CSV保存");

        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;

        try (PrintWriter pw = new PrintWriter(chooser.getSelectedFile() + ".csv", "UTF-8")) {

            // Headers (bỏ cột checkbox)
            for (int c = 1; c < model.getColumnCount(); c++) {
                pw.print(model.getColumnName(c));
                if (c < model.getColumnCount() - 1) pw.print(",");
            }
            pw.println();

            // Rows
            for (int r = 0; r < model.getRowCount(); r++) {
                for (int c = 1; c < model.getColumnCount(); c++) {
                    pw.print(model.getValueAt(r, c));
                    if (c < model.getColumnCount() - 1) pw.print(",");
                }
                pw.println();
            }

            JOptionPane.showMessageDialog(this, "CSV出力完了！");

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "CSV出力失敗！");
        }
    }

}