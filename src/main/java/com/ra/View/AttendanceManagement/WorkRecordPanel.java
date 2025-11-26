package com.ra.View.AttendanceManagement;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * WorkRecordPanel - Panel cho chức năng đăng ký Work-Record
 * Hiển thị giao diện CRUD cho báo cáo công việc hàng ngày
 */
public class WorkRecordPanel extends JPanel {

    // Màu sắc theo specification
    private final Color COLOR_SUMMARY_BG = new Color(221, 244, 221); // Light Green #DDF4DD
    private final Color COLOR_TABLE_HEADER_BG = new Color(230, 242, 255); // Light Blue #E6F2FF
    private final Color COLOR_CANCEL_BG = new Color(255, 218, 185); // Light Orange
    private final Color COLOR_SEND_BG = new Color(173, 216, 230); // Light Blue
    private final Color COLOR_ERROR = new Color(255, 0, 0); // Red for required fields

    // Font
    private final Font MAIN_FONT = new Font("Dialog", Font.PLAIN, 12);
    private final Font BOLD_FONT = new Font("Dialog", Font.BOLD, 12);
    private final Font ERROR_FONT = new Font("Dialog", Font.PLAIN, 10);

    // Components
    private JTable workTable;
    private DefaultTableModel tableModel;
    private JTextField dateField, startField, endField, breakField;
    private JComboBox<String> projectComboBox, taskComboBox;

    public WorkRecordPanel() {
        initializeUI();
    }

    /**
     * Khởi tạo giao diện người dùng
     */
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Thêm các panel chính
        add(createInputPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    /**
     * Tạo panel nhập liệu (Top Panel)
     */
    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(new CompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // ===== ROW 1: Thời gian =====
        int row = 0;

        // Date Field
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createLabelWithRequired("日付"), gbc);

        gbc.gridy = row + 1;
        dateField = new JTextField("2025/11/11");
        dateField.setFont(MAIN_FONT);
        dateField.setPreferredSize(new Dimension(120, 25));
        panel.add(dateField, gbc);

        // Start Time
        gbc.gridx = 1; gbc.gridy = row;
        panel.add(new JLabel("開始"), gbc);

        gbc.gridy = row + 1;
        startField = new JTextField("09:00");
        startField.setFont(MAIN_FONT);
        startField.setPreferredSize(new Dimension(80, 25));
        panel.add(startField, gbc);

        // End Time
        gbc.gridx = 2; gbc.gridy = row;
        panel.add(new JLabel("終了"), gbc);

        gbc.gridy = row + 1;
        endField = new JTextField("14:00");
        endField.setFont(MAIN_FONT);
        endField.setPreferredSize(new Dimension(80, 25));
        panel.add(endField, gbc);

        // Break Time
        gbc.gridx = 3; gbc.gridy = row;
        panel.add(new JLabel("休憩"), gbc);

        gbc.gridy = row + 1;
        breakField = new JTextField("00:00");
        breakField.setFont(MAIN_FONT);
        breakField.setPreferredSize(new Dimension(80, 25));
        panel.add(breakField, gbc);

        // ===== ROW 2: Dự án và Nội dung công việc =====
        row += 2;

        // Project ComboBox
        gbc.gridx = 0; gbc.gridy = row;
        panel.add(createLabelWithRequired("プロジェクト"), gbc);

        gbc.gridy = row + 1;
        projectComboBox = createEditableComboBox();
        projectComboBox.setPreferredSize(new Dimension(200, 25));
        panel.add(projectComboBox, gbc);

        // Task Content ComboBox
        gbc.gridx = 1; gbc.gridy = row; gbc.gridwidth = 2;
        panel.add(createLabelWithRequired("作業内容"), gbc);

        gbc.gridy = row + 1;
        taskComboBox = createEditableComboBox();
        taskComboBox.setPreferredSize(new Dimension(250, 25));
        panel.add(taskComboBox, gbc);

        // ===== ROW 3: Nút Thêm =====
        row += 2;
        gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 4;
        gbc.anchor = GridBagConstraints.WEST;

        JButton addButton = new JButton("追加");
        addButton.setFont(BOLD_FONT);
        addButton.setPreferredSize(new Dimension(80, 30));
        addButton.setBackground(new Color(20, 250, 10));
        addButton.addActionListener(new AddButtonListener());
        panel.add(addButton, gbc);

        return panel;
    }

    /**
     * Tạo label với chú thích bắt buộc màu đỏ
     */
    private JPanel createLabelWithRequired(String text) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        JLabel mainLabel = new JLabel(text);
        mainLabel.setFont(MAIN_FONT);

        JLabel requiredLabel = new JLabel("※必須");
        requiredLabel.setFont(ERROR_FONT);
        requiredLabel.setForeground(COLOR_ERROR);

        panel.add(mainLabel, BorderLayout.CENTER);
        panel.add(requiredLabel, BorderLayout.SOUTH);

        return panel;
    }

    /**
     * Tạo ComboBox có thể chỉnh sửa
     */
    private JComboBox<String> createEditableComboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setEditable(true);
        comboBox.setFont(MAIN_FONT);

        // Thêm một số dữ liệu mẫu
        comboBox.addItem("プロジェクトA");
        comboBox.addItem("プロジェクトB");
        comboBox.addItem("プロジェクトC");

        return comboBox;
    }

    /**
     * Tạo panel bảng dữ liệu (Middle Panel)
     */
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new CompoundBorder(
                new LineBorder(Color.LIGHT_GRAY, 1),
                new EmptyBorder(10, 10, 10, 10)
        ));

        // Summary Header
        panel.add(createSummaryHeader(), BorderLayout.NORTH);

        // Data Table
        panel.add(createDataTable(), BorderLayout.CENTER);

        return panel;
    }

    /**
     * Tạo header tổng hợp
     */
    private JPanel createSummaryHeader() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        panel.setBackground(COLOR_SUMMARY_BG);
        panel.setBorder(new EmptyBorder(8, 10, 8, 10));

        JLabel totalLabel = new JLabel("合計");
        totalLabel.setFont(BOLD_FONT);

        JLabel workTimeLabel = new JLabel("所定内勤務時間: 5時間");
        workTimeLabel.setFont(MAIN_FONT);

        JLabel breakTimeLabel = new JLabel("休憩: 0時間");
        breakTimeLabel.setFont(MAIN_FONT);

        JLabel overtimeLabel = new JLabel("残業: 0時間");
        overtimeLabel.setFont(MAIN_FONT);

        panel.add(totalLabel);
        panel.add(workTimeLabel);
        panel.add(breakTimeLabel);
        panel.add(overtimeLabel);

        return panel;
    }

    /**
     * Tạo bảng dữ liệu với các nút hành động
     */
    private JScrollPane createDataTable() {
        // Tạo model cho bảng
        String[] columnNames = {
                "順番", "プロジェクトID", "プロジェクト名", "作業内容",
                "開始", "終了", "休憩時間", "勤務時間", "操作"
        };

        // Dữ liệu mẫu
        Object[][] data = {
                {1, "P001", "プロジェクトA", "設計作業", "09:00", "12:00", "00:00", "3時間", ""},
                {2, "P002", "プロジェクトB", "コーディング", "13:00", "17:00", "01:00", "3時間", ""}
        };

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Chỉ cho phép chỉnh sửa cột "操作"
                return column == 8;
            }

            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return String.class;
            }
        };

        workTable = new JTable(tableModel);
        workTable.setRowHeight(35);
        workTable.setFont(MAIN_FONT);
        workTable.getTableHeader().setFont(BOLD_FONT);
        workTable.getTableHeader().setBackground(COLOR_TABLE_HEADER_BG);
        workTable.setSelectionBackground(new Color(220, 240, 255));

        // === CĂN GIỮA CHO TẤT CẢ CÁC CỘT ===
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Áp dụng căn giữa cho tất cả các cột từ 0 đến 8
        for (int i = 0; i < workTable.getColumnCount(); i++) {
            workTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Cũng căn giữa cho tiêu đề các cột
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) workTable.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Cài đặt renderer và editor cho cột "操作" (ghi đè lên centerRenderer)
        workTable.getColumnModel().getColumn(8).setCellRenderer(new ActionCellRenderer());
        workTable.getColumnModel().getColumn(8).setCellEditor(new ActionCellEditor());

        // Đặt chiều rộng cột
        workTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // 順番
        workTable.getColumnModel().getColumn(1).setPreferredWidth(80);  // プロジェクトID
        workTable.getColumnModel().getColumn(2).setPreferredWidth(100); // プロジェクト名
        workTable.getColumnModel().getColumn(3).setPreferredWidth(120); // 作業内容
        workTable.getColumnModel().getColumn(4).setPreferredWidth(60);  // 開始
        workTable.getColumnModel().getColumn(5).setPreferredWidth(60);  // 終了
        workTable.getColumnModel().getColumn(6).setPreferredWidth(70);  // 休憩時間
        workTable.getColumnModel().getColumn(7).setPreferredWidth(70);  // 勤務時間
        workTable.getColumnModel().getColumn(8).setPreferredWidth(160); // 操作

        JScrollPane scrollPane = new JScrollPane(workTable);
        scrollPane.setPreferredSize(new Dimension(800, 200));

        return scrollPane;
    }

    /**
     * Tạo panel footer với các nút hành động
     */
    private JPanel createFooterPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        panel.setBackground(Color.WHITE);
        panel.setBorder(new EmptyBorder(10, 0, 0, 0));

        // Nút Cancel
        JButton cancelButton = new JButton("キャンセル");
        cancelButton.setFont(BOLD_FONT);
        cancelButton.setPreferredSize(new Dimension(100, 35));
        cancelButton.setBackground(COLOR_CANCEL_BG);
        cancelButton.setFocusPainted(false);
        cancelButton.addActionListener(e -> clearForm());

        // Nút Send
        JButton sendButton = new JButton("送信");
        sendButton.setFont(BOLD_FONT);
        sendButton.setPreferredSize(new Dimension(100, 35));
        sendButton.setBackground(COLOR_SEND_BG);
        sendButton.setFocusPainted(false);
        sendButton.addActionListener(e -> submitData());

        panel.add(cancelButton);
        panel.add(sendButton);

        return panel;
    }

    /**
     * Xóa form nhập liệu
     */
    private void clearForm() {
        dateField.setText("2025/11/11");
        startField.setText("09:00");
        endField.setText("14:00");
        breakField.setText("00:00");
        projectComboBox.setSelectedIndex(0);
        taskComboBox.setSelectedIndex(0);
    }

    /**
     * Gửi dữ liệu
     */
    private void submitData() {
        if (tableModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this,
                    "データがありません。",
                    "警告",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        int result = JOptionPane.showConfirmDialog(this,
                "ワークレコードを送信しますか？",
                "確認",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                    "ワークレコードが正常に送信されました。",
                    "成功",
                    JOptionPane.INFORMATION_MESSAGE);
            // Ở đây có thể thêm logic gửi dữ liệu thực tế
        }
    }

    /**
     * Listener cho nút Thêm
     */
    private class AddButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // Validate dữ liệu
            if (projectComboBox.getSelectedItem() == null ||
                    projectComboBox.getSelectedItem().toString().trim().isEmpty()) {
                JOptionPane.showMessageDialog(WorkRecordPanel.this,
                        "プロジェクトを選択してください。",
                        "入力エラー",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (taskComboBox.getSelectedItem() == null ||
                    taskComboBox.getSelectedItem().toString().trim().isEmpty()) {
                JOptionPane.showMessageDialog(WorkRecordPanel.this,
                        "作業内容を入力してください。",
                        "入力エラー",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Thêm dữ liệu vào bảng
            int newRowNumber = tableModel.getRowCount() + 1;
            String project = projectComboBox.getSelectedItem().toString();
            String task = taskComboBox.getSelectedItem().toString();

            // Tính toán thời gian làm việc (đơn giản)
            String workTime = calculateWorkTime(startField.getText(), endField.getText(), breakField.getText());

            Object[] newRow = {
                    newRowNumber,
                    "P00" + newRowNumber,
                    project,
                    task,
                    startField.getText(),
                    endField.getText(),
                    breakField.getText(),
                    workTime,
                    ""
            };

            tableModel.addRow(newRow);

            JOptionPane.showMessageDialog(WorkRecordPanel.this,
                    "作業記録を追加しました。",
                    "成功",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Tính toán thời gian làm việc (đơn giản)
     */
    private String calculateWorkTime(String start, String end, String breakTime) {
        try {
            // Phân tích thời gian đơn giản
            int startHour = Integer.parseInt(start.split(":")[0]);
            int endHour = Integer.parseInt(end.split(":")[0]);
            int breakHour = Integer.parseInt(breakTime.split(":")[0]);

            int workHours = endHour - startHour - breakHour;
            return workHours + "時間";
        } catch (Exception e) {
            return "計算エラー";
        }
    }

    /**
     * Custom Cell Renderer cho cột Action
     */
    /**
     * Custom Cell Renderer cho cột Action (đã sửa để căn giữa)
     */
    private class ActionCellRenderer extends JPanel implements TableCellRenderer {
        private JButton editButton;
        private JButton deleteButton;

        public ActionCellRenderer() {
            setLayout(new FlowLayout(FlowLayout.CENTER, 5, 0)); // Đảm bảo căn giữa
            setOpaque(true);

            editButton = new JButton("編集");
            editButton.setFont(MAIN_FONT);
            editButton.setPreferredSize(new Dimension(75, 25));
            editButton.setBackground(new Color(179, 205, 227)); // Light blue
            editButton.setFocusPainted(false);
            editButton.setBorderPainted(false);

            deleteButton = new JButton("削除");
            deleteButton.setFont(MAIN_FONT);
            deleteButton.setPreferredSize(new Dimension(75, 25));
            deleteButton.setBackground(new Color(247, 167, 163)); // Light red
            deleteButton.setFocusPainted(false);
            deleteButton.setBorderPainted(false);

            add(editButton);
            add(deleteButton);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {
            // SỬA LỖI: Đảm bảo background hiển thị đúng
            if (isSelected) {
                setBackground(table.getSelectionBackground());
            } else {
                setBackground(table.getBackground());
            }
            return this;
        }
    }

    /**
     * Custom Cell Editor cho cột Action
     */
    private class ActionCellEditor extends AbstractCellEditor implements TableCellEditor {
        private JPanel panel;
        private JButton editButton;
        private JButton deleteButton;
        private int currentRow;

        public ActionCellEditor() {
            panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
            panel.setOpaque(true);

            editButton = new JButton("編集");
            editButton.setFont(MAIN_FONT);
            editButton.setPreferredSize(new Dimension(55, 25));
            editButton.setBackground(new Color(179, 205, 227));
            editButton.setFocusPainted(false);
            editButton.setBorderPainted(false);

            deleteButton = new JButton("削除");
            deleteButton.setFont(MAIN_FONT);
            deleteButton.setPreferredSize(new Dimension(55, 25));
            deleteButton.setBackground(new Color(247, 167, 163));
            deleteButton.setFocusPainted(false);
            deleteButton.setBorderPainted(false);

            // Sự kiện cho nút Edit
            editButton.addActionListener(e -> {
                if (currentRow >= 0 && currentRow < tableModel.getRowCount()) {
                    editRecord(currentRow);
                    fireEditingStopped();
                }
            });

            // Sự kiện cho nút Delete
            deleteButton.addActionListener(e -> {
                if (currentRow >= 0 && currentRow < tableModel.getRowCount()) {
                    deleteRecord(currentRow);
                    fireEditingStopped();
                }
            });

            panel.add(editButton);
            panel.add(deleteButton);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value,
                                                     boolean isSelected, int row, int column) {
            currentRow = row;
            panel.setBackground(table.getSelectionBackground());
            return panel;
        }

        @Override
        public Object getCellEditorValue() {
            return "";
        }

        /**
         * Chỉnh sửa bản ghi
         */
        private void editRecord(int row) {
            String projectName = tableModel.getValueAt(row, 2).toString();
            String taskContent = tableModel.getValueAt(row, 3).toString();

            // Hiển thị form chỉnh sửa
            JPanel editPanel = new JPanel(new GridLayout(4, 2, 5, 5));
            editPanel.add(new JLabel("プロジェクト名:"));
            JTextField projectField = new JTextField(projectName);
            editPanel.add(projectField);

            editPanel.add(new JLabel("作業内容:"));
            JTextField taskField = new JTextField(taskContent);
            editPanel.add(taskField);

            editPanel.add(new JLabel("開始時間:"));
            JTextField startField = new JTextField(tableModel.getValueAt(row, 4).toString());
            editPanel.add(startField);

            editPanel.add(new JLabel("終了時間:"));
            JTextField endField = new JTextField(tableModel.getValueAt(row, 5).toString());
            editPanel.add(endField);

            int result = JOptionPane.showConfirmDialog(WorkRecordPanel.this,
                    editPanel,
                    "レコード編集",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                // Cập nhật dữ liệu
                tableModel.setValueAt(projectField.getText(), row, 2);
                tableModel.setValueAt(taskField.getText(), row, 3);
                tableModel.setValueAt(startField.getText(), row, 4);
                tableModel.setValueAt(endField.getText(), row, 5);

                // Tính lại thời gian làm việc
                String workTime = calculateWorkTime(startField.getText(), endField.getText(),
                        tableModel.getValueAt(row, 6).toString());
                tableModel.setValueAt(workTime, row, 7);

                JOptionPane.showMessageDialog(WorkRecordPanel.this,
                        "レコードを更新しました。",
                        "成功",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }

        /**
         * Xóa bản ghi
         */
        private void deleteRecord(int row) {
            int result = JOptionPane.showConfirmDialog(WorkRecordPanel.this,
                    "この作業記録を削除しますか？",
                    "確認",
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                tableModel.removeRow(row);

                // Cập nhật số thứ tự
                for (int i = 0; i < tableModel.getRowCount(); i++) {
                    tableModel.setValueAt(i + 1, i, 0);
                }

                JOptionPane.showMessageDialog(WorkRecordPanel.this,
                        "作業記録を削除しました。",
                        "成功",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}