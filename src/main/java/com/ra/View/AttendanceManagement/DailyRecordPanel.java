package com.ra.View.AttendanceManagement;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
//màn này vẫn chưa được tích hợp với màn theo tháng. cần thêm logic
//

public class DailyRecordPanel extends JPanel {
    private JTable recordTable;
    private JLabel dateLabel;

    public DailyRecordPanel() {
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Add all components in order
        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }

    /**
     * Creates the top panel with statistics fields and date label
     * Uses GridBagLayout for precise control over component positioning
     */
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        // Statistics panel with GridBagLayout for neat arrangement
        JPanel statsPanel = new JPanel(new GridBagLayout());
        statsPanel.setBackground(Color.WHITE);
        statsPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 1: First three fields
        gbc.gridy = 0;

        gbc.gridx = 0;
        statsPanel.add(createLabeledField("出勤時間", "9:00"), gbc);

        gbc.gridx = 1;
        statsPanel.add(createLabeledField("退勤時間", "17:00"), gbc);

        gbc.gridx = 2;
        statsPanel.add(createLabeledField("休憩時間", "1:00"), gbc);

        // Row 2: Last two fields
        gbc.gridy = 1;

        gbc.gridx = 0;
        statsPanel.add(createLabeledField("所定内勤務時間", "4:00"), gbc);

        gbc.gridx = 1;
        statsPanel.add(createLabeledField("残業時間", "0:00"), gbc);

        topPanel.add(statsPanel, BorderLayout.NORTH);

        // Date label (bold, centered below statistics)
        dateLabel = new JLabel("2025年10月12日", JLabel.CENTER);
        dateLabel.setFont(new Font("Meiryo", Font.BOLD, 16));
        dateLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        topPanel.add(dateLabel, BorderLayout.CENTER);

        return topPanel;
    }

    /**
     * Creates a labeled text field component
     */
    private JPanel createLabeledField(String label, String value) {
        JPanel fieldPanel = new JPanel(new BorderLayout());
        fieldPanel.setBackground(Color.WHITE);

        // Label
        JLabel titleLabel = new JLabel(label);
        titleLabel.setFont(new Font("Meiryo", Font.PLAIN, 12));
        titleLabel.setHorizontalAlignment(JLabel.CENTER);

        // Value field
        JTextField valueField = new JTextField(value);
        valueField.setFont(new Font("Meiryo", Font.PLAIN, 14));
        valueField.setPreferredSize(new Dimension(80, 25));
        valueField.setEditable(false);
        valueField.setHorizontalAlignment(JTextField.CENTER);
        valueField.setBackground(Color.WHITE);

        fieldPanel.add(titleLabel, BorderLayout.NORTH);
        fieldPanel.add(valueField, BorderLayout.CENTER);

        return fieldPanel;
    }

    /**
     * Creates the center panel with the table container
     * Includes rounded border and header strip
     */
    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);

        // Create the decorative table container
        JPanel tableContainer = createTableContainer();
        centerPanel.add(tableContainer, BorderLayout.CENTER);

        return centerPanel;
    }

    /**
     * Creates the table container with rounded border and header strip
     */
    private JPanel createTableContainer() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(Color.WHITE);

        // Create rounded border
        Border roundedBorder = BorderFactory.createCompoundBorder(
                new RoundedBorder(15, Color.BLACK),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        );
        container.setBorder(roundedBorder);

        // Header strip with "全てレコード"
        JLabel headerLabel = new JLabel("全てレコード", JLabel.CENTER);
        headerLabel.setFont(new Font("Meiryo", Font.BOLD, 16));
        headerLabel.setOpaque(true);
        headerLabel.setBackground(new Color(230, 242, 255)); // #E6F2FF - Light Blue
        headerLabel.setPreferredSize(new Dimension(0, 40));
        container.add(headerLabel, BorderLayout.NORTH);

        // Create and add the record table
        container.add(createRecordTable(), BorderLayout.CENTER);

        return container;
    }

    /**
     * Creates the record table with custom renderer for status column
     */
    private JScrollPane createRecordTable() {
        // Table column names exactly as specified
        String[] columnNames = {
                "順番", "プロジェクトID", "プロジェクト名", "作業内容",
                "開始", "終了", "休憩時間", "勤務時間", "状態"
        };

        // Mock data matching the image exactly
        Object[][] data = {
                {"1", "111", "abc", "a", "9:00", "14:00", "1:00", "4:00", "修正要"},
                {"2", "112", "xyz", "b", "15:00", "17:00", "0:00", "2:00", "修正要"}
        };

        // Create non-editable table model
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }
        };

        recordTable = new JTable(model);
        recordTable.setFont(new Font("Meiryo", Font.PLAIN, 12));
        recordTable.setRowHeight(30);
        recordTable.getTableHeader().setFont(new Font("Meiryo", Font.BOLD, 12));

        /**
         * CRITICAL: Apply custom renderer to the status column (column 8)
         * This renderer checks if the cell value is "修正要" and applies light yellow background
         */
        recordTable.getColumnModel().getColumn(8).setCellRenderer(new StatusCellRenderer());

        JScrollPane scrollPane = new JScrollPane(recordTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());

        return scrollPane;
    }

    /**
     * Creates the bottom panel with pagination and action buttons
     */
    private JPanel createBottomPanel() {
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

        // Left side: Day navigation (pagination controls)
        JPanel navPanel = createNavigationPanel();
        bottomPanel.add(navPanel, BorderLayout.WEST);

        // Right side: Action buttons
        JPanel buttonPanel = createButtonPanel();
        bottomPanel.add(buttonPanel, BorderLayout.EAST);

        return bottomPanel;
    }

    /**
     * Creates the navigation panel with day controls
     */
    private JPanel createNavigationPanel() {
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        navPanel.setBackground(Color.WHITE);

        JButton prevDayBtn = new JButton("< 前日");
        JLabel dayLabel = new JLabel("12");
        JButton nextDayBtn = new JButton("後日 >");

        // Style navigation buttons
        for (JButton btn : new JButton[]{prevDayBtn, nextDayBtn}) {
            btn.setFont(new Font("Meiryo", Font.PLAIN, 14));
            btn.setBackground(Color.WHITE);
        }

        // Style day label
        dayLabel.setFont(new Font("Meiryo", Font.BOLD, 14));
        dayLabel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        navPanel.add(prevDayBtn);
        navPanel.add(dayLabel);
        navPanel.add(nextDayBtn);

        return navPanel;
    }

    /**
     * Creates the button panel with Back and Logout buttons
     */
    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonPanel.setBackground(Color.WHITE);

        // Back button with white background
        JButton backBtn = new JButton("戻り");
        backBtn.setFont(new Font("Meiryo", Font.PLAIN, 14));
        backBtn.setBackground(Color.WHITE);
        backBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Back button clicked - Navigation logic would go here");
            }
        });

        // Logout button with light red background (#FFCDD2)
        JButton logoutBtn = new JButton("ログアウト");
        logoutBtn.setFont(new Font("Meiryo", Font.PLAIN, 14));
        logoutBtn.setBackground(new Color(255, 205, 210));
        logoutBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Logout button clicked - Logout logic would go here");
            }
        });

        buttonPanel.add(backBtn);
        buttonPanel.add(logoutBtn);

        return buttonPanel;
    }

    /**
     * Custom border class for rounded corners
     */
    private static class RoundedBorder extends LineBorder {
        private int radius;

        public RoundedBorder(int radius, Color color) {
            super(color, 1);
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setColor(getLineColor());
            g2d.drawRoundRect(x, y, width - 1, height - 1, radius, radius);
            g2d.dispose();
        }
    }

    /**
     * Custom cell renderer for the status column
     * This is CRUCIAL for the color coding logic
     */
    private static class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            // Get the default component
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            /**
             * COLOR LOGIC: If the cell value is "修正要", apply light yellow background
             * Otherwise, use default background
             */
            if (value != null && value.equals("修正要")) {
                c.setBackground(new Color(255, 249, 196)); // #FFF9C4 - Light Yellow
                c.setForeground(Color.BLACK);
            } else {
                c.setBackground(Color.WHITE);
                c.setForeground(Color.BLACK);
            }

            // Center align the text and set font
            setHorizontalAlignment(CENTER);
            setFont(new Font("Meiryo", Font.PLAIN, 12));

            return c;
        }
    }

    // Public methods for external control
    public void setDate(String date) {
        dateLabel.setText(date);
    }

    public void setDayNumber(String day) {
        // This would update the day label in navigation if needed
    }
    public void loadDailyData(String date, Object[][] records) {
        setDate(date);
        // Logic cập nhật bảng với dữ liệu hàng ngày
        updateTableData(records);
    }

    private void updateTableData(Object[][] data) {
        DefaultTableModel model = (DefaultTableModel) recordTable.getModel();
        model.setRowCount(0); // Xóa dữ liệu cũ
        for (Object[] row : data) {
            model.addRow(row);
        }
    }
    /**
     * Main method for isolated testing
     * Run this to see the UI immediately
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame testFrame = new JFrame("Daily Record Panel - Test");
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.setSize(1000, 600);
            testFrame.setLocationRelativeTo(null);

            // Create and add our panel
            DailyRecordPanel dailyPanel = new DailyRecordPanel();
            testFrame.add(dailyPanel);

            testFrame.setVisible(true);
        });
    }
}