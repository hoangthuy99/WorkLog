package com.ra.View.AttendanceManagement;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * MonthlyWorkView - Màn hình xem công hàng tháng
 * Hiển thị thông tin chấm công theo tháng với các trạng thái màu sắc
 */
public class MonthlyWorkView extends JPanel {

    // Màu sắc theo specification
    private final Color COLOR_SUMMARY_BG = new Color(221, 244, 221); // Light Green #DDF4DD
    private final Color COLOR_LOGOUT_BG = new Color(255, 205, 210); // Light Red #FFCDD2

    // Màu cho cột Status
    private final Color COLOR_NEEDS_FIX = new Color(255, 249, 196); // Light Yellow #FFF9C4
    private final Color COLOR_APPROVED = new Color(200, 230, 201);  // Light Green #C8E6C9
    private final Color COLOR_UNAPPROVED = new Color(255, 205, 210); // Light Red #FFCDD2

    // Font
    private final Font MAIN_FONT = new Font("Dialog", Font.PLAIN, 12);
    private final Font BOLD_FONT = new Font("Dialog", Font.BOLD, 12);
    private final Font LARGE_FONT = new Font("Dialog", Font.BOLD, 24);
    private final Font TITLE_FONT = new Font("Dialog", Font.BOLD, 18);

    // Components
    private JTable workTable;
    private DefaultTableModel tableModel;

    public MonthlyWorkView() {
        initializeUI();
        setupTableInteraction();
    }

    /**
     * Khởi tạo giao diện người dùng
     */
    private void initializeUI() {
        setLayout(new BorderLayout(10, 10));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Thêm các panel chính
        //add(createH
        // eaderPanel(), BorderLayout.NORTH);
        add(createMainPanel(), BorderLayout.CENTER);
        add(createFooterPanel(), BorderLayout.SOUTH);
    }

    /**
     * Thiết lập tương tác cho bảng - cho phép double-click để xem chi tiết
     */
    private void setupTableInteraction() {
        workTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // Double-click
                    int row = workTable.rowAtPoint(e.getPoint());
                    if (row >= 0) {
                        String date = workTable.getValueAt(row, 0).toString();
                        String status = workTable.getValueAt(row, 7).toString();

                        // Hiển thị thông báo khi double-click (có thể thay bằng navigation sau này)
                        JOptionPane.showMessageDialog(MonthlyWorkView.this,
                                "日付 " + date + " の詳細を表示します\n" +
                                        "ステータス: " + status,
                                "日次詳細",
                                JOptionPane.INFORMATION_MESSAGE);
                    }
                }
            }
        });
    }

    /**
     * Tạo Header Panel (North)
     private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(0, 0, 20, 0));

        // Greeting - Lời chào
        JLabel greetingLabel = new JLabel("お疲れ様、名前さん!!!");
        greetingLabel.setFont(LARGE_FONT);
        greetingLabel.setForeground(Color.BLACK);

        // Date Display - Hiển thị ngày tháng
        JLabel dateLabel = new JLabel("2025/11/13 (水) 20:20:20");
        dateLabel.setFont(MAIN_FONT);
        dateLabel.setForeground(Color.BLACK);
        dateLabel.setBorder(new CompoundBorder(
                new LineBorder(Color.BLACK, 1),
                new EmptyBorder(5, 10, 5, 10)
        ));

        headerPanel.add(greetingLabel, BorderLayout.WEST);
        headerPanel.add(dateLabel, BorderLayout.EAST);

        return headerPanel;
    }
    */

    /**
     * Tạo Main Panel (Center)
     */
    private JPanel createMainPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);

        // Month Title - Tiêu đề tháng
        JLabel monthLabel = new JLabel("2025年10月", SwingConstants.CENTER);
        monthLabel.setFont(TITLE_FONT);
        monthLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        // Data Table Panel - Panel bảng dữ liệu
        JPanel tablePanel = createTablePanel();

        mainPanel.add(monthLabel, BorderLayout.NORTH);
        mainPanel.add(tablePanel, BorderLayout.CENTER);

        return mainPanel;
    }

    /**
     * Tạo Panel chứa bảng dữ liệu
     */
    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        // Summary Panel - Panel tổng hợp
        JPanel summaryPanel = createSummaryPanel();
        tablePanel.add(summaryPanel, BorderLayout.NORTH);

        // Data Table - Bảng dữ liệu
        JScrollPane tableScrollPane = createDataTable();
        tablePanel.add(tableScrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    /**
     * Tạo Summary Panel với background màu xanh lá nhạt
     */
    private JPanel createSummaryPanel() {
        JPanel summaryPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        summaryPanel.setBackground(COLOR_SUMMARY_BG);
        summaryPanel.setBorder(new EmptyBorder(10, 15, 10, 15));

        JLabel totalLabel = new JLabel("合計");
        totalLabel.setFont(new Font("Dialog", Font.BOLD, 14));

        JLabel workTimeLabel = new JLabel("所定内勤務時間：");
        workTimeLabel.setFont(MAIN_FONT);

        JLabel breakTimeLabel = new JLabel("休憩：");
        breakTimeLabel.setFont(MAIN_FONT);

        JLabel overtimeLabel = new JLabel("残業：");
        overtimeLabel.setFont(MAIN_FONT);

        summaryPanel.add(totalLabel);
        summaryPanel.add(workTimeLabel);
        summaryPanel.add(breakTimeLabel);
        summaryPanel.add(overtimeLabel);

        return summaryPanel;
    }

    /**
     * Tạo bảng dữ liệu với custom renderer cho cột Status
     */
    private JScrollPane createDataTable() {
        // Tạo model cho bảng
        String[] columnNames = {
                "日", "曜日", "出勤", "退勤", "休憩", "時間内", "残業", "状態"
        };

        // Dữ liệu mẫu theo hình ảnh
        Object[][] data = {
                {"12", "日", "9:00", "17:00", "1:00", "6:00", "a+b", "修正要"},
                {"13", "", "", "", "", "", "", "承認済み"},
                {"14", "", "", "", "", "", "", "未承認"},
                {"15", "", "", "", "", "", "", ""},
                {"16", "", "", "", "", "", "", ""}
        };

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Không cho phép chỉnh sửa trực tiếp trên bảng
                return false;
            }
        };

        workTable = new JTable(tableModel);
        workTable.setRowHeight(35);
        workTable.setFont(MAIN_FONT);
        workTable.getTableHeader().setFont(BOLD_FONT);
        workTable.getTableHeader().setBackground(new Color(240, 240, 240));
        workTable.setSelectionBackground(new Color(220, 240, 255));

        // === CUSTOM RENDERER CHO CỘT STATUS ===
        // Áp dụng custom renderer cho cột "状態" (cột 7)
        workTable.getColumnModel().getColumn(7).setCellRenderer(new StatusCellRenderer());

        // Căn giữa cho tất cả các cột
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < workTable.getColumnCount(); i++) {
            workTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Căn giữa tiêu đề cột
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) workTable.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // Đặt chiều rộng cột
        workTable.getColumnModel().getColumn(0).setPreferredWidth(50);  // 日
        workTable.getColumnModel().getColumn(1).setPreferredWidth(60);  // 曜日
        workTable.getColumnModel().getColumn(2).setPreferredWidth(70);  // 出勤
        workTable.getColumnModel().getColumn(3).setPreferredWidth(70);  // 退勤
        workTable.getColumnModel().getColumn(4).setPreferredWidth(70);  // 休憩
        workTable.getColumnModel().getColumn(5).setPreferredWidth(80);  // 時間内
        workTable.getColumnModel().getColumn(6).setPreferredWidth(80);  // 残業
        workTable.getColumnModel().getColumn(7).setPreferredWidth(90);  // 状態

        JScrollPane scrollPane = new JScrollPane(workTable);
        scrollPane.setPreferredSize(new Dimension(800, 300));

        return scrollPane;
    }

    /**
     * Tạo Footer Panel (South)
     */
    private JPanel createFooterPanel() {
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setBackground(Color.WHITE);
        footerPanel.setBorder(new EmptyBorder(20, 0, 0, 0));

        // Pagination - Phân trang (Left) - Sắp xếp theo chiều dọc
        JPanel paginationPanel = new JPanel();
        paginationPanel.setLayout(new BoxLayout(paginationPanel, BoxLayout.Y_AXIS));
        paginationPanel.setBackground(Color.WHITE);
        paginationPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Nút Previous Month
        JButton prevButton = new JButton("前月");
        setupPaginationButton(prevButton);
        prevButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Label số trang
        JLabel pageLabel = new JLabel("10");
        pageLabel.setFont(new Font("Dialog", Font.BOLD, 14));
        pageLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        pageLabel.setBorder(new EmptyBorder(5, 0, 5, 0));

        // Nút Next Month
        JButton nextButton = new JButton("後月");
        setupPaginationButton(nextButton);
        nextButton.setAlignmentX(Component.LEFT_ALIGNMENT);

        paginationPanel.add(prevButton);
        paginationPanel.add(pageLabel);
        paginationPanel.add(nextButton);

        // Action Buttons - Nút hành động (Right)
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionPanel.setBackground(Color.WHITE);

        JButton logoutButton = new JButton("ログアウト");

        // Thiết lập style cho nút hành động
        logoutButton.setFont(MAIN_FONT);
        logoutButton.setPreferredSize(new Dimension(100, 35));
        logoutButton.setBackground(COLOR_LOGOUT_BG);
        logoutButton.setFocusPainted(false);
        logoutButton.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));

        // Thêm sự kiện
        prevButton.addActionListener(e -> showPreviousMonth());
        nextButton.addActionListener(e -> showNextMonth());
        logoutButton.addActionListener(e -> logout());

        actionPanel.add(logoutButton);

        footerPanel.add(paginationPanel, BorderLayout.WEST);
        footerPanel.add(actionPanel, BorderLayout.EAST);

        return footerPanel;
    }

    /**
     * Thiết lập style cho nút phân trang
     */
    private void setupPaginationButton(JButton button) {
        button.setFont(MAIN_FONT);
        button.setPreferredSize(new Dimension(80, 30));
        button.setMaximumSize(new Dimension(80, 30));
        button.setBackground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
        button.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    /**
     * Custom Cell Renderer cho cột Status
     * Thay đổi màu nền dựa trên giá trị của ô
     */
    private class StatusCellRenderer extends DefaultTableCellRenderer {

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus, int row, int column) {

            // Gọi phương thức của lớp cha để thiết lập các thuộc tính cơ bản
            Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

            // Đặt căn giữa cho nội dung
            setHorizontalAlignment(SwingConstants.CENTER);

            // Đặt màu nền dựa trên giá trị status
            if (value != null) {
                String status = value.toString();
                switch (status) {
                    case "修正要":
                        setBackground(COLOR_NEEDS_FIX);
                        setForeground(Color.BLACK);
                        break;
                    case "承認済み":
                        setBackground(COLOR_APPROVED);
                        setForeground(Color.BLACK);
                        break;
                    case "未承認":
                        setBackground(COLOR_UNAPPROVED);
                        setForeground(Color.BLACK);
                        break;
                    default:
                        setBackground(Color.WHITE);
                        setForeground(Color.BLACK);
                        break;
                }
            } else {
                setBackground(Color.WHITE);
                setForeground(Color.BLACK);
            }

            // Nếu ô được chọn, giữ màu selection
            if (isSelected) {
                setBackground(getBackground().darker());
            }

            return this;
        }
    }

    /**
     * Xử lý sự kiện chuyển tháng trước
     */
    private void showPreviousMonth() {
        JOptionPane.showMessageDialog(this,
                "前月に移動します",
                "情報",
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Xử lý sự kiện chuyển tháng sau
     */
    private void showNextMonth() {
        JOptionPane.showMessageDialog(this,
                "後月に移動します",
                "情報",
                JOptionPane.INFORMATION_MESSAGE);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame testFrame = new JFrame("Monthly Work View - Test");
            testFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            testFrame.setSize(900, 600);
            testFrame.setLocationRelativeTo(null);

            MonthlyWorkView monthlyView = new MonthlyWorkView();
            testFrame.add(monthlyView);
            testFrame.setVisible(true);
        });
    }
    /**
     * Xử lý sự kiện đăng xuất
     */
    private void logout() {
        int result = JOptionPane.showConfirmDialog(this,
                "ログアウトしますか？",
                "確認",
                JOptionPane.YES_NO_OPTION);

        if (result == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this,
                    "ログアウトしました",
                    "情報",
                    JOptionPane.INFORMATION_MESSAGE);
        }
    }
}