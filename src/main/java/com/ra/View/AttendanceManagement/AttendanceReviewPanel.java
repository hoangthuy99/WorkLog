package com.ra.View.AttendanceManagement;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class AttendanceReviewPanel extends JPanel {
    private JTable attendanceTable;
    private JButton approveButton;
    private JButton rejectButton;

    // Các component filter
    private DateTextField dateFilter; // Thay thế JComboBox bằng DateTextField
    private JComboBox<String> projectFilter;
    private JComboBox<String> deptFilter;
    private JComboBox<String> statusFilter;
    private JTextField empNameFilter;

    public AttendanceReviewPanel() {
        initializePanel();
    }

    private void initializePanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Add all components in order
        add(createFilterPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    /**
     * Creates the filter panel with interactive filter components
     * Uses FlowLayout for left-aligned components
     */
    private JPanel createFilterPanel() {
        JPanel filterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        filterPanel.setBackground(Color.WHITE);
        filterPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        // Create interactive filter components
        JPanel dateFilterPanel = createDateFilter();
        JPanel projectFilterPanel = createProjectFilter();
        JPanel deptFilterPanel = createDeptFilter();
        JPanel statusFilterPanel = createStatusFilter();

        // Add filter components to panel
        filterPanel.add(dateFilterPanel);
        filterPanel.add(projectFilterPanel);
        filterPanel.add(deptFilterPanel);
        filterPanel.add(statusFilterPanel);

        // Add search bar with icon/button
        filterPanel.add(createSearchBar());

        return filterPanel;
    }

    /**
     * Creates date filter with DateTextField
     */
    private JPanel createDateFilter() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("日付");
        label.setFont(new Font("Meiryo", Font.PLAIN, 12));
        label.setPreferredSize(new Dimension(40, 20));

        // Sử dụng DateTextField thay vì JComboBox
        dateFilter = new DateTextField("yyyy/MM/dd", new Date());
        dateFilter.setFont(new Font("Meiryo", Font.PLAIN, 12));
        dateFilter.setBackground(new Color(255, 205, 210)); // Light Red/Pink
        dateFilter.setPreferredSize(new Dimension(120, 30));
        dateFilter.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 5, 5, 5)
        ));

        // Thêm nút clear để xóa lựa chọn ngày
        JPanel dateInputPanel = new JPanel(new BorderLayout());
        dateInputPanel.setBackground(Color.WHITE);
        dateInputPanel.add(dateFilter, BorderLayout.CENTER);

        JButton clearDateButton = new JButton("×");
        clearDateButton.setFont(new Font("Meiryo", Font.PLAIN, 10));
        clearDateButton.setPreferredSize(new Dimension(20, 20));
        clearDateButton.setMargin(new Insets(0, 0, 0, 0));
        clearDateButton.addActionListener(e -> {
            dateFilter.setText("");
            applyFilters();
        });

        JPanel clearButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        clearButtonPanel.setBackground(Color.WHITE);
        clearButtonPanel.add(clearDateButton);
        dateInputPanel.add(clearButtonPanel, BorderLayout.EAST);

        panel.add(label, BorderLayout.NORTH);
        panel.add(dateInputPanel, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates project filter with dropdown
     */
    private JPanel createProjectFilter() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("プロジェクト");
        label.setFont(new Font("Meiryo", Font.PLAIN, 12));
        label.setPreferredSize(new Dimension(60, 20));

        // Project options
        String[] projectOptions = {
                "全て",
                "プロジェクトA",
                "プロジェクトB",
                "プロジェクトC",
                "プロジェクトD",
                "abc",
                "xyz"
        };

        projectFilter = new JComboBox<>(projectOptions);
        projectFilter.setFont(new Font("Meiryo", Font.PLAIN, 12));
        projectFilter.setBackground(Color.WHITE);
        projectFilter.setPreferredSize(new Dimension(120, 30));
        projectFilter.addActionListener(e -> applyFilters());

        panel.add(label, BorderLayout.NORTH);
        panel.add(projectFilter, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates department filter with dropdown
     */
    private JPanel createDeptFilter() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("部署");
        label.setFont(new Font("Meiryo", Font.PLAIN, 12));
        label.setPreferredSize(new Dimension(40, 20));

        // Department options
        String[] deptOptions = {
                "全て",
                "Code",
                "Dev",
                "Design",
                "Test",
                "開発",
                "営業",
                "人事"
        };

        deptFilter = new JComboBox<>(deptOptions);
        deptFilter.setFont(new Font("Meiryo", Font.PLAIN, 12));
        deptFilter.setBackground(Color.WHITE);
        deptFilter.setPreferredSize(new Dimension(120, 30));
        deptFilter.addActionListener(e -> applyFilters());

        panel.add(label, BorderLayout.NORTH);
        panel.add(deptFilter, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Creates status filter with dropdown
     */
    private JPanel createStatusFilter() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel("ステータス");
        label.setFont(new Font("Meiryo", Font.PLAIN, 12));
        label.setPreferredSize(new Dimension(50, 20));

        // Status options
        String[] statusOptions = {
                "全て",
                "確認要",
                "承認済",
                "却下済"
        };

        statusFilter = new JComboBox<>(statusOptions);
        statusFilter.setFont(new Font("Meiryo", Font.PLAIN, 12));
        statusFilter.setBackground(new Color(230, 242, 255)); // Light Blue
        statusFilter.setPreferredSize(new Dimension(120, 30));
        statusFilter.addActionListener(e -> applyFilters());

        panel.add(label, BorderLayout.NORTH);
        panel.add(statusFilter, BorderLayout.CENTER);

        return panel;
    }

    /**
     * Applies all filters to the table
     */
    private void applyFilters() {
        // Get filter values
        String selectedDate = dateFilter.getText().trim();
        String selectedProject = projectFilter.getSelectedItem().toString();
        String selectedDept = deptFilter.getSelectedItem().toString();
        String selectedStatus = statusFilter.getSelectedItem().toString();

        // In a real application, you would filter the table data here
        // For now, just show what filters are applied
        System.out.println("Applying filters:");
        System.out.println("Date: " + (selectedDate.isEmpty() ? "All" : selectedDate));
        System.out.println("Project: " + selectedProject);
        System.out.println("Dept: " + selectedDept);
        System.out.println("Status: " + selectedStatus);

        // You would typically filter your table model here
        // filterTableData(selectedDate, selectedProject, selectedDept, selectedStatus, empName);
    }

    /**
     * Creates the search bar with placeholder text
     */
    private JPanel createSearchBar() {
        JPanel searchPanel = new JPanel(new BorderLayout(5, 0));
        searchPanel.setBackground(Color.WHITE);

        // Search text field with placeholder
        JTextField searchField = new JTextField(15);
        searchField.setFont(new Font("Meiryo", Font.PLAIN, 12));
        searchField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));

        // Set placeholder text (simulated)
        searchField.setText("社員名");
        searchField.setForeground(Color.GRAY);

        // Search button with icon (using text as icon substitute)
        JButton searchButton = new JButton("検索");
        searchButton.setFont(new Font("Meiryo", Font.PLAIN, 12));
        searchButton.setBackground(Color.WHITE);
        searchButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        searchButton.setPreferredSize(new Dimension(40, 30));
        searchButton.addActionListener(e -> applyFilters());

        searchPanel.add(searchField, BorderLayout.CENTER);
        searchPanel.add(searchButton, BorderLayout.EAST);

        return searchPanel;
    }

    /**
     * Creates the main table panel with attendance data
     */
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBackground(Color.WHITE);

        // Create and configure the attendance table
        JScrollPane scrollPane = createAttendanceTable();
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    /**
     * Creates the attendance table with custom renderer for status column
     */
    private JScrollPane createAttendanceTable() {
        // Table column names
        String[] columnNames = {
                "Attendance ID", "社員 ID", "社員名", "部署", "勤務時間", "ステータス"
        };

        // Mock data as specified
        Object[][] data = {
                {"A111", "S111", "Nguyen Van A", "Code", "10", "確認要"},
                {"A121", "S123", "Nguyen Van B", "Test", "15", "確認要"},
                {"A122", "S124", "Tran Van C", "Dev", "8", "承認済"},
                {"A123", "S125", "Le Thi D", "Design", "12", "却下済"},
                {"A124", "S126", "Pham Van E", "Code", "9", "確認要"}
        };

        // Create non-editable table model
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Make all cells non-editable
            }

            @Override
            public Class<?> getColumnClass(int column) {
                return String.class; // All columns contain strings
            }
        };

        attendanceTable = new JTable(model);
        attendanceTable.setFont(new Font("Meiryo", Font.PLAIN, 12));
        attendanceTable.setRowHeight(35);
        attendanceTable.getTableHeader().setFont(new Font("Meiryo", Font.BOLD, 13));
        attendanceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        /**
         * CRITICAL: Apply custom renderer to the status column (column 5)
         * This renderer applies different background colors based on status value
         */
        attendanceTable.getColumnModel().getColumn(5).setCellRenderer(new StatusCellRenderer());

        JScrollPane scrollPane = new JScrollPane(attendanceTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        return scrollPane;
    }

    /**
     * Creates the footer panel with action buttons
     */
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));

        // Left side: Decision buttons
        JPanel decisionPanel = createDecisionButtonsPanel();
        footerPanel.add(decisionPanel, BorderLayout.WEST);

        // Right side: System buttons
        JPanel systemPanel = createSystemButtonsPanel();
        footerPanel.add(systemPanel, BorderLayout.EAST);

        return footerPanel;
    }

    /**
     * Creates the decision buttons panel (Approve/Reject)
     */
    private JPanel createDecisionButtonsPanel() {
        JPanel decisionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 0));
        decisionPanel.setBackground(Color.WHITE);

        // Reject button with Orange/Yellow background
        rejectButton = new JButton("却下");
        rejectButton.setFont(new Font("Meiryo", Font.BOLD, 14));
        rejectButton.setBackground(new Color(255, 183, 77)); // Orange/Yellow
        rejectButton.setForeground(Color.BLACK);
        rejectButton.setPreferredSize(new Dimension(100, 35));
        rejectButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Add action listener for reject button
        rejectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showRejectConfirmationDialog();
            }
        });

        // Approve button with Light Blue background
        approveButton = new JButton("承認");
        approveButton.setFont(new Font("Meiryo", Font.BOLD, 14));
        approveButton.setBackground(new Color(179, 229, 252)); // Light Blue
        approveButton.setForeground(Color.BLACK);
        approveButton.setPreferredSize(new Dimension(100, 35));
        approveButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));

        // Add action listener for approve button
        approveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showApproveConfirmationDialog();
            }
        });

        decisionPanel.add(rejectButton);
        decisionPanel.add(approveButton);

        return decisionPanel;
    }

    /**
     * Creates the system buttons panel (Back/Logout)
     */
    private JPanel createSystemButtonsPanel() {
        JPanel systemPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        systemPanel.setBackground(Color.WHITE);

        // Back button with white background
        JButton backButton = new JButton("戻り");
        backButton.setFont(new Font("Meiryo", Font.PLAIN, 14));
        backButton.setBackground(Color.WHITE);
        backButton.setPreferredSize(new Dimension(80, 35));
        backButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        backButton.addActionListener(e -> {
            System.out.println("Back button clicked - Navigation logic would go here");
        });

        // Logout button with light red background
        JButton logoutButton = new JButton("ログアウト");
        logoutButton.setFont(new Font("Meiryo", Font.PLAIN, 14));
        logoutButton.setBackground(new Color(255, 205, 210)); // Light Red
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1));
        logoutButton.addActionListener(e -> {
            System.out.println("Logout button clicked - Logout logic would go here");
        });

        systemPanel.add(backButton);
        systemPanel.add(logoutButton);

        return systemPanel;
    }

    /**
     * Shows confirmation dialog for approval action
     * Uses JOptionPane with custom buttons
     */
    private void showApproveConfirmationDialog() {
        int selectedRow = attendanceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "承認するレコードを選択してください。",
                    "選択エラー",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String employeeName = (String) attendanceTable.getValueAt(selectedRow, 2);

        // Create custom buttons
        Object[] options = {"承認", "後で"};

        int result = JOptionPane.showOptionDialog(
                this,
                "「" + employeeName + "」の勤怠を承認してもよろしいでしょうか？",
                "承認確認",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1] // Default to "後で"
        );

        /**
         * Handle dialog result:
         * 0 = "承認" (Approve) clicked
         * 1 = "後で" (Later) clicked or dialog closed
         */
        if (result == 0) {
            // Update status to approved
            attendanceTable.setValueAt("承認済", selectedRow, 5);
            JOptionPane.showMessageDialog(this,
                    "承認が完了しました。",
                    "成功",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Shows confirmation dialog for reject action
     * Uses JOptionPane with custom buttons
     */
    private void showRejectConfirmationDialog() {
        int selectedRow = attendanceTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "却下するレコードを選択してください。",
                    "選択エラー",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }

        String employeeName = (String) attendanceTable.getValueAt(selectedRow, 2);

        // Create custom buttons
        Object[] options = {"却下", "後で"};

        int result = JOptionPane.showOptionDialog(
                this,
                "「" + employeeName + "」の勤怠を却下してもよろしいでしょうか？",
                "却下確認",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[1] // Default to "後で"
        );

        /**
         * Handle dialog result:
         * 0 = "却下" (Reject) clicked
         * 1 = "後で" (Later) clicked or dialog closed
         */
        if (result == 0) {
            // Update status to rejected
            attendanceTable.setValueAt("却下済", selectedRow, 5);
            JOptionPane.showMessageDialog(this,
                    "却下が完了しました。",
                    "成功",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Custom cell renderer for the status column
     * CRUCIAL: Applies different background colors based on status value
     */
    private static class StatusCellRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            /**
             * COLOR CODING LOGIC:
             * - "確認要" (Needs Check) -> Light Yellow (#FFF9C4)
             * - "承認済" (Approved) -> Light Green (#C8E6C9)
             * - "却下済" (Rejected) -> Light Red (#FFCDD2)
             */
            if (value != null) {
                String status = value.toString();
                switch (status) {
                    case "確認要":
                        c.setBackground(new Color(255, 249, 196)); // Light Yellow
                        c.setForeground(Color.BLACK);
                        break;
                    case "承認済":
                        c.setBackground(new Color(200, 230, 201)); // Light Green
                        c.setForeground(Color.BLACK);
                        break;
                    case "却下済":
                        c.setBackground(new Color(255, 205, 210)); // Light Red
                        c.setForeground(Color.BLACK);
                        break;
                    default:
                        c.setBackground(Color.WHITE);
                        c.setForeground(Color.BLACK);
                }
            }

            // Center align the text and set font
            setHorizontalAlignment(CENTER);
            setFont(new Font("Meiryo", Font.PLAIN, 12));
            setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

            return c;
        }
    }

    /**
     * Main method for isolated testing
     * Run this to see the complete Admin Dashboard UI immediately
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame testFrame = new JFrame("Admin Dashboard - Attendance Review");
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.setSize(1000, 600);
            testFrame.setLocationRelativeTo(null);

            // Create and add our panel
            AttendanceReviewPanel reviewPanel = new AttendanceReviewPanel();
            testFrame.add(reviewPanel);

            testFrame.setVisible(true);
        });
    }
}