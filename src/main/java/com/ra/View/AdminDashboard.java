package com.ra.View;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import javax.swing.border.CompoundBorder;

// --- IMPORT CÁC MÀN HÌNH MODULE ---
// Module Quản lý User
import com.ra.View.UserManagenment.UserList;
import com.ra.View.UserManagenment.CreateNewUser;

// Module Quản lý Chấm công (Attendance)
import com.ra.View.AttendanceManagement.WorkRecordPanel;
import com.ra.View.AttendanceManagement.MonthlyWorkView;
import com.ra.View.AttendanceManagement.AttendanceReviewPanel;
import com.ra.View.AttendanceManagement.DailyRecordPanel;

// Module Báo cáo
import com.ra.View.report.ReportSummaryPanel;
// ----------------------------------------

/**
 * AdminDashboard - Màn hình chính của hệ thống quản lý chấm công
 *
 * Chức năng chính:
 * 1. Hiển thị menu điều hướng (Sidebar) với các module chức năng
 * 2. Quản lý chuyển đổi giữa các màn hình con (Content Area)
 * 3. Hiển thị thông tin thời gian thực và thông tin người dùng
 * 4. Cung cấp chức năng đăng xuất
 *
 * Kiến trúc:
 * - BorderLayout chính: WEST (Sidebar), CENTER (Content Area)
 * - CardLayout cho Content Area: chuyển đổi giữa các panel
 * - Timer: cập nhật thời gian real-time
 *
 * @author System Team
 * @version 2.0
 * @since 2025-01-01
 */
class AdminDashboard extends JFrame {

    // ========================================================================
    // KHAI BÁO COMPONENTS CHÍNH
    // ========================================================================

    /** Panel chứa menu sidebar */
    private JPanel menuPanel;

    /** Panel chứa nội dung chính (sử dụng CardLayout) */
    private JPanel contentPanel;

    /** CardLayout để chuyển đổi giữa các màn hình */
    private CardLayout cardLayout;

    /** Label hiển thị tiêu đề màn hình hiện tại */
    private JLabel headerTitle;

    /** Label hiển thị thời gian real-time */
    private JLabel dateTimeLabel;

    // ========================================================================
    // KHAI BÁO TẤT CẢ CÁC MÀN HÌNH CON (PANELS)
    // ========================================================================

    // --- MODULE QUẢN LÝ USER ---
    /** Màn hình danh sách User */
    private UserList userListPanel;

    /** Màn hình tạo User mới */
    private CreateNewUser createNewUserPanel;

    // --- MODULE QUẢN LÝ CHẤM CÔNG (ATTENDANCE) ---
    /** Màn hình đăng ký Work-Record (báo cáo công việc hàng ngày) */
    private WorkRecordPanel workRecordPanel;

    /** Màn hình xem công theo tháng */
    private MonthlyWorkView monthlyWorkView;

    /** Màn hình phê duyệt chấm công */
    private AttendanceReviewPanel attendanceReviewPanel;

    /** Màn hình xem chi tiết công theo ngày */
    private DailyRecordPanel dailyRecordPanel;

    // --- MODULE BÁO CÁO ---
    /** Màn hình tổng hợp báo cáo */
    private ReportSummaryPanel reportSummaryPanel;

    // ========================================================================
    // KHAI BÁO MÀU SẮC VÀ FONT CHỮ
    // ========================================================================

    // --- MÀU SẮC SIDEBAR ---
    /** Màu nền Sidebar (Xanh đậm) */
    private final Color COLOR_SIDEBAR_BG = new Color(44, 62, 80);

    /** Màu hover trên menu Sidebar */
    private final Color COLOR_SIDEBAR_HOVER = new Color(52, 73, 94);

    /** Màu nền submenu */
    private final Color COLOR_SUBMENU_BG = new Color(37, 52, 68);

    // --- MÀU CHỮ ---
    /** Màu chữ chính (Đen) */
    private final Color NEW_TEXT_COLOR = Color.BLACK;

    /** Màu accent (Xanh dương) */
    private final Color COLOR_ACCENT = new Color(52, 152, 219);

    // --- KHAI BÁO FONT ---
    /** Font cơ bản cho các label và button */
    private final Font MENU_FONT_BASE = new Font(Font.DIALOG, Font.PLAIN, 14);

    /** Font cho menu cha (bold) */
    private final Font MENU_PARENT_FONT = new Font(Font.DIALOG, Font.BOLD, 15);

    /** Font cho menu con */
    private final Font MENU_CHILD_FONT = new Font(Font.DIALOG, Font.PLAIN, 14);

    // ========================================================================
    // CONSTRUCTOR - KHỞI TẠO HỆ THỐNG
    // ========================================================================

    /**
     * Constructor khởi tạo AdminDashboard
     *
     * Thứ tự khởi tạo:
     * 1. Đồng bộ font toàn giao diện
     * 2. Thiết lập thông tin cửa sổ (Title, Size, Location)
     * 3. Khởi tạo Sidebar (Menu điều hướng)
     * 4. Khởi tạo Content Area (Màn hình chính)
     * 5. Khởi động Timer cập nhật thời gian
     */
    public AdminDashboard() {
        // Đồng bộ font toàn giao diện
        UIManager.put("Label.font", MENU_FONT_BASE);
        UIManager.put("Button.font", MENU_FONT_BASE);

        // Thiết lập thuộc tính JFrame
        setTitle("勤怠管理システム");  // Tiêu đề: Hệ thống quản lý chấm công
        setSize(1200, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);  // Canh giữa màn hình
        setLayout(new BorderLayout());

        // Khởi tạo các thành phần UI
        initSidebar();      // Menu điều hướng bên trái
        initContentArea();  // Khu vực nội dung chính

        // Khởi động Timer cập nhật thời gian
        startDateTimeUpdater();

        // Hiển thị cửa sổ
        setVisible(true);
    }

    // ========================================================================
    // TIMER CẬP NHẬT THỜI GIAN REAL-TIME
    // ========================================================================

    /**
     * Khởi động Timer để cập nhật thời gian mỗi giây
     *
     * Định dạng: yyyy/MM/dd (E) HH:mm:ss
     * Ví dụ: 2025/01/15 (水) 14:30:45
     */
    private void startDateTimeUpdater() {
        ActionListener updateTask = e -> {
            LocalDateTime now = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd (E) HH:mm:ss", Locale.JAPAN);
            String formattedDateTime = now.format(formatter);
            if (dateTimeLabel != null) {
                dateTimeLabel.setText(formattedDateTime);
            }
        };

        // Timer chạy mỗi 1000ms (1 giây)
        Timer timer = new Timer(1000, updateTask);
        timer.start();
    }

    // ========================================================================
    // KHỞI TẠO SIDEBAR (MENU ĐIỀU HƯỚNG)
    // ========================================================================

    /**
     * Khởi tạo Sidebar chứa menu điều hướng
     *
     * Cấu trúc:
     * - NORTH: Tiêu đề "ADMIN PANEL"
     * - CENTER: ScrollPane chứa các menu item
     * - SOUTH: Nút Logout
     *
     * Menu bao gồm:
     * 1. ユーザー管理 (Quản lý User)
     *    - ユーザーリスト (Danh sách User)
     *    - 新ユーザー作成 (Tạo User mới)
     *
     * 2. 部署管理 (Quản lý Phòng Ban)
     *
     * 3. プロジェクト管理 (Quản lý Dự án)
     *
     * 4. タスク管理 (Quản lý Task)
     *
     * 5. 勤怠管理 (Quản lý Chấm công)
     *    - Work-Record 登録 (Đăng ký Work-Record)
     *    - 日付別 Attendance 閲覧 (Xem công theo tháng)
     *    - 日次詳細閲覧 (Xem chi tiết theo ngày)
     *    - Attendance 承認 (Phê duyệt chấm công)
     *
     * 6. レポート管理 (Quản lý Báo cáo)
     *    - レポート集計 (Tổng hợp báo cáo)
     *
     * 7. システム (Hệ thống)
     *    - General Settings
     *    - Audit Logs
     *
     * 8. メニュー (Menu)
     *    - メニューリスト
     *    - 新メニュー作成
     */
    private void initSidebar() {
        // Container chính cho Sidebar
        JPanel sidebarContainer = new JPanel(new BorderLayout());
        sidebarContainer.setPreferredSize(new Dimension(260, getHeight()));
        sidebarContainer.setBackground(COLOR_SIDEBAR_BG);

        // Panel chứa các menu item
        menuPanel = new JPanel();
        menuPanel.setLayout(new BoxLayout(menuPanel, BoxLayout.Y_AXIS));
        menuPanel.setBackground(Color.WHITE);

        // Tiêu đề "ADMIN PANEL"
        JLabel adminTitle = new JLabel("ADMIN PANEL");
        adminTitle.setFont(new Font(Font.DIALOG, Font.BOLD, 18));
        adminTitle.setForeground(Color.WHITE);
        adminTitle.setBorder(new EmptyBorder(15, 15, 15, 0));
        adminTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setBackground(COLOR_SIDEBAR_BG);
        titlePanel.add(adminTitle, BorderLayout.WEST);

        sidebarContainer.add(titlePanel, BorderLayout.NORTH);

        // ====================================================================
        // THÊM CÁC MENU ITEM
        // ====================================================================

        // 1. QUẢN LÝ USER
        addAccordionMenu("ユーザー管理", new String[]{
                "ユーザーリスト",      // Danh sách User
                "新ユーザー作成"       // Tạo User mới
        });

        // 2. QUẢN LÝ PHÒNG BAN (Chưa có màn hình)
        addAccordionMenu("部署管理", new String[]{
                "部署リスト",
                "新部署作成"
        });

        // 3. QUẢN LÝ DỰ ÁN (Chưa có màn hình)
        addAccordionMenu("プロジェクト管理", new String[]{
                "プロジェクトリスト",
                "新プロジェクト作成"
        });

        // 4. QUẢN LÝ TASK (Chưa có màn hình)
        addAccordionMenu("タスク管理", new String[]{
                "タスクリスト",
                "新タスク作成"
        });

        // 5. QUẢN LÝ CHẤM CÔNG (4 màn hình con)
        addAccordionMenu("勤怠管理", new String[]{
                "Work-Record 登録",            // Đăng ký Work-Record
                "日付別 Attendance 閲覧",      // Xem công theo tháng
                "日次詳細閲覧",                // Xem chi tiết theo ngày
                "Attendance 承認"              // Phê duyệt chấm công
        });

        // 6. QUẢN LÝ BÁO CÁO
        addAccordionMenu("レポート管理", new String[]{
                "レポート集計"         // Tổng hợp báo cáo
        });

        // 7. HỆ THỐNG (Chưa có màn hình)
       // addAccordionMenu("システム", new String[]{
       //         "General Settings",
        //       "Audit Logs"
        //});

        // 8. MENU (Chưa có màn hình)
        addAccordionMenu("メニュー", new String[]{
                "メニューリスト",
                "新メニュー作成"
        });

        // ====================================================================
        // SCROLLPANE CHO MENU
        // ====================================================================

        JScrollPane scrollPane = new JScrollPane(menuPanel);
        scrollPane.setBorder(null);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(12);

        // ====================================================================
        // NÚT LOGOUT
        // ====================================================================

        JButton btnLogout = new JButton("ログアウト");  // Đăng xuất
        btnLogout.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        btnLogout.setForeground(new Color(231, 76, 60));  // Màu đỏ
        btnLogout.setFocusPainted(false);
        btnLogout.setBorderPainted(false);
        btnLogout.setContentAreaFilled(false);
        btnLogout.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Sự kiện click nút Logout
        btnLogout.addActionListener(e -> {
            int result = JOptionPane.showConfirmDialog(this,
                    "ログアウトしますか？",  // Bạn có muốn đăng xuất?
                    "確認",                 // Xác nhận
                    JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                JOptionPane.showMessageDialog(this, "ログアウトしました!");
                // Đóng Dashboard và mở lại LoginScreen
                this.dispose();
                SwingUtilities.invokeLater(() -> new LoginScreen().setVisible(true));
            }
        });

        JPanel logoutPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoutPanel.setBackground(COLOR_SIDEBAR_BG);
        logoutPanel.add(btnLogout);
        logoutPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Thêm các thành phần vào Sidebar
        sidebarContainer.add(scrollPane, BorderLayout.CENTER);
        sidebarContainer.add(logoutPanel, BorderLayout.SOUTH);

        // Thêm Sidebar vào JFrame
        add(sidebarContainer, BorderLayout.WEST);
    }

    // ========================================================================
    // KHỞI TẠO CONTENT AREA (KHU VỰC NỘI DUNG CHÍNH)
    // ========================================================================

    /**
     * Khởi tạo khu vực nội dung chính (Content Area)
     *
     * Cấu trúc:
     * - NORTH: Header (Lời chào + Thời gian + Tiêu đề màn hình)
     * - CENTER: CardLayout chứa các màn hình con
     *
     * CardLayout chứa các màn hình:
     * 1. "UserList" - Danh sách User
     * 2. "NewUser" - Tạo User mới
     * 3. "WorkRecord" - Đăng ký Work-Record
     * 4. "MonthlyView" - Xem công theo tháng
     * 5. "DailyRecord" - Xem chi tiết theo ngày
     * 6. "AttendanceReview" - Phê duyệt chấm công
     * 7. "ReportSummaryPanel" - Tổng hợp báo cáo
     * 8. "Default" - Màn hình demo
     */
    private void initContentArea() {
        // Panel chính bên phải (Content Area)
        JPanel rightPanel = new JPanel(new BorderLayout());
        rightPanel.setBackground(new Color(244, 246, 249));

        // ====================================================================
        // HEADER (NORTH)
        // ====================================================================

        // Container chứa toàn bộ header
        JPanel contentHeaderContainer = new JPanel();
        contentHeaderContainer.setLayout(new BoxLayout(contentHeaderContainer, BoxLayout.Y_AXIS));
        contentHeaderContainer.setBackground(Color.WHITE);

        // Top Header: Lời chào + Thời gian
        JPanel topHeaderPanel = createTopHeaderPanel();
        contentHeaderContainer.add(topHeaderPanel);

        // Main Header: Tiêu đề màn hình hiện tại
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(Color.WHITE);
        headerPanel.setBorder(new EmptyBorder(15, 20, 15, 20));

        headerTitle = new JLabel("ユーザー一覧(All User)");  // Tiêu đề mặc định
        headerTitle.setFont(new Font(Font.DIALOG, Font.BOLD, 26));
        headerTitle.setForeground(new Color(40, 55, 75));

        headerPanel.add(headerTitle, BorderLayout.WEST);
        contentHeaderContainer.add(headerPanel);

        rightPanel.add(contentHeaderContainer, BorderLayout.NORTH);

        // ====================================================================
        // CONTENT PANEL (CENTER) - SỬ DỤNG CARDLAYOUT
        // ====================================================================

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(new Color(244, 246, 249));
        contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // KHỞI TẠO VÀ THÊM TẤT CẢ CÁC MÀN HÌNH VÀO CARDLAYOUT
        try {
            // --- MODULE QUẢN LÝ USER ---
            userListPanel = new UserList();
            createNewUserPanel = new CreateNewUser();

            // --- MODULE QUẢN LÝ CHẤM CÔNG ---
            workRecordPanel = new WorkRecordPanel();
            monthlyWorkView = new MonthlyWorkView();
            attendanceReviewPanel = new AttendanceReviewPanel();
            dailyRecordPanel = new DailyRecordPanel();


            // --- MODULE BÁO CÁO ---
            reportSummaryPanel = new ReportSummaryPanel();

            // THÊM VÀO CARDLAYOUT VỚI KEY TƯƠNG ỨNG
            contentPanel.add(userListPanel, "UserList");
            contentPanel.add(createNewUserPanel, "NewUser");
            contentPanel.add(workRecordPanel, "WorkRecord");
            contentPanel.add(monthlyWorkView, "MonthlyView");
            contentPanel.add(attendanceReviewPanel, "AttendanceReview");
            contentPanel.add(dailyRecordPanel, "DailyRecord");
            contentPanel.add(reportSummaryPanel, "ReportSummaryPanel");

        } catch (Exception e) {
            // Xử lý lỗi khi khởi tạo các Panel
            System.err.println("Lỗi khi khởi tạo Panels: " + e.getMessage());
            e.printStackTrace();

            // Hiển thị màn hình lỗi
            JLabel errorLabel = new JLabel("LỖI: Không tìm thấy các class cần thiết - " + e.getMessage());
            errorLabel.setForeground(Color.RED);
            contentPanel.add(errorLabel, "Error");
            cardLayout.show(contentPanel, "Error");
        }

        // Thêm màn hình Demo (dùng khi menu chưa có màn hình tương ứng)
        contentPanel.add(createDemoPanel(), "Default");

        // Hiển thị màn hình UserList mặc định khi khởi động
        cardLayout.show(contentPanel, "UserList");

        rightPanel.add(contentPanel, BorderLayout.CENTER);
        add(rightPanel, BorderLayout.CENTER);
    }

    // ========================================================================
    // TẠO TOP HEADER PANEL (LỜI CHÀO + THỜI GIAN)
    // ========================================================================

    /**
     * Tạo Top Header Panel chứa lời chào và thời gian
     *
     * @return JPanel chứa lời chào và thời gian
     */
    private JPanel createTopHeaderPanel() {
        JPanel topHeaderPanel = new JPanel(new BorderLayout());
        topHeaderPanel.setBackground(Color.WHITE);

        // Viền dưới và padding
        Border margin = new EmptyBorder(20, 20, 10, 20);
        Border bottomBorder = BorderFactory.createMatteBorder(0, 0, 1, 0, Color.LIGHT_GRAY);
        topHeaderPanel.setBorder(new CompoundBorder(bottomBorder, margin));

        // Lời chào (WEST)
        JLabel welcomeMessage = new JLabel("お疲れ様、名前さん!!!");  // Xin chào, Tên người dùng!
        welcomeMessage.setFont(new Font(Font.DIALOG, Font.BOLD, 24));
        welcomeMessage.setForeground(Color.BLACK);
        topHeaderPanel.add(welcomeMessage, BorderLayout.WEST);

        // Thời gian (EAST)
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd (E) HH:mm:ss", Locale.JAPAN);
        String formattedDateTime = now.format(formatter);

        dateTimeLabel = new JLabel(formattedDateTime);
        dateTimeLabel.setFont(MENU_FONT_BASE);
        dateTimeLabel.setForeground(Color.BLACK);

        // Box chứa label thời gian (có viền)
        JPanel timeBox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        timeBox.setBackground(Color.WHITE);
        timeBox.setBorder(new LineBorder(Color.BLACK, 1));
        timeBox.add(dateTimeLabel);

        topHeaderPanel.add(timeBox, BorderLayout.EAST);

        return topHeaderPanel;
    }

    // ========================================================================
    // TẠO DEMO PANEL (MÀN HÌNH MẶC ĐỊNH)
    // ========================================================================

    /**
     * Tạo Demo Panel - hiển thị khi chọn menu chưa có màn hình
     *
     * @return JPanel chứa thông báo demo
     */
    private JPanel createDemoPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(Color.WHITE);
        p.setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel l = new JLabel("デモコンテンツエリア - Chọn một mục từ menu để bắt đầu");
        l.setFont(new Font(Font.DIALOG, Font.PLAIN, 18));
        l.setForeground(Color.GRAY);
        p.add(l);
        return p;
    }

    // ========================================================================
    // THÊM MENU ACCORDION (CHA + CON)
    // ========================================================================

    /**
     * Thêm menu dạng Accordion (có thể mở rộng/thu gọn)
     *
     * @param title Tiêu đề menu cha
     * @param subItems Mảng các menu con (null nếu không có con)
     *
     * Logic:
     * - Nếu subItems = null: tạo menu đơn giản
     * - Nếu subItems != null: tạo menu cha + các menu con
     */
    private void addAccordionMenu(String title, String[] subItems) {
        // Trường hợp menu KHÔNG CÓ menu con
        if (subItems == null || subItems.length == 0) {
            JButton singleBtn = createFlatButton(title, true);
            singleBtn.addActionListener(e -> {
                headerTitle.setText(title);
                cardLayout.show(contentPanel, "Default");
            });
            menuPanel.add(singleBtn);
            return;
        }

        // Trường hợp menu CÓ menu con
        JButton parentBtn = createFlatButton(title, true);

        // Panel chứa các menu con
        JPanel subPanel = new JPanel();
        subPanel.setLayout(new BoxLayout(subPanel, BoxLayout.Y_AXIS));
        subPanel.setBackground(Color.LIGHT_GRAY);
        subPanel.setVisible(false);  // Ẩn mặc định

        // Thêm từng menu con
        for (String item : subItems) {
            JButton subBtn = new JButton(item);
            subBtn.setAlignmentX(Component.LEFT_ALIGNMENT);
            subBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 36));
            subBtn.setBackground(Color.LIGHT_GRAY);
            subBtn.setForeground(NEW_TEXT_COLOR);
            subBtn.setFont(MENU_CHILD_FONT);
            subBtn.setBorder(new EmptyBorder(8, 25, 8, 10));
            subBtn.setHorizontalAlignment(SwingConstants.LEFT);
            subBtn.setFocusPainted(false);
            subBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));

            // Sự kiện click menu con
            subBtn.addActionListener(e -> {
                String clickedItem = ((JButton)e.getSource()).getText();
                headerTitle.setText(clickedItem);

                // ============================================================
                // LOGIC CHUYỂN MÀN HÌNH DỰA VÀO TÊN MENU
                // ============================================================

                String cardName;

                // MODULE QUẢN LÝ USER
                if ("ユーザーリスト".equals(clickedItem)) {
                    cardName = "UserList";
                } else if ("新ユーザー作成".equals(clickedItem)) {
                    cardName = "NewUser";
                }

                // MODULE QUẢN LÝ CHẤM CÔNG
                else if ("Work-Record 登録".equals(clickedItem)) {
                    cardName = "WorkRecord";
                } else if ("日付別 Attendance 閲覧".equals(clickedItem)) {
                    cardName = "MonthlyView";
                } else if ("日次詳細閲覧".equals(clickedItem)) {
                    cardName = "DailyRecord";
                } else if ("Attendance 承認".equals(clickedItem)) {
                    cardName = "AttendanceReview";
                }

                // MODULE BÁO CÁO
                else if ("レポート集計".equals(clickedItem)) {
                    cardName = "ReportSummaryPanel";
                }

                // Mặc định (menu chưa có màn hình)
                else {
                    cardName = "Default";
                }

                // Log để debug
                System.out.println("Chuyển đến màn hình: " + cardName + " - " + clickedItem);

                // Chuyển màn hình
                cardLayout.show(contentPanel, cardName);

                // Cập nhật lại giao diện
                contentPanel.revalidate();
                contentPanel.repaint();
            });

            // Hiệu ứng hover cho menu con
            subBtn.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent evt) {
                    subBtn.setForeground(Color.BLUE);
                    subBtn.setBackground(new Color(200, 200, 200));
                }
                public void mouseExited(MouseEvent evt) {
                    subBtn.setForeground(NEW_TEXT_COLOR);
                    subBtn.setBackground(Color.LIGHT_GRAY);
                }
            });

            subPanel.add(subBtn);
        }

        // Sự kiện click menu cha (Mở/Đóng menu con)
        parentBtn.addActionListener(e -> {
            boolean isVisible = subPanel.isVisible();
            subPanel.setVisible(!isVisible);
            parentBtn.setText(title);
            menuPanel.revalidate();
        });

        // Thêm menu cha và menu con vào menuPanel
        menuPanel.add(parentBtn);
        menuPanel.add(subPanel);
    }

    // ========================================================================
    // TẠO NÚT MENU FLAT (KHÔNG VIỀN NỔI)
    // ========================================================================

    /**
     * Tạo nút menu với style flat (không viền nổi)
     *
     * @param text Nội dung text của nút
     * @param isParent true nếu là menu cha, false nếu là menu con
     * @return JButton đã được style
     */
    private JButton createFlatButton(String text, boolean isParent) {
        JButton btn = new JButton(text);
        btn.setAlignmentX(Component.LEFT_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setBackground(Color.WHITE);
        btn.setForeground(NEW_TEXT_COLOR);
        btn.setFocusPainted(false);
        btn.setBorder(new EmptyBorder(12, 15, 12, 30));
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setFont(isParent ? MENU_PARENT_FONT : MENU_CHILD_FONT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Hiệu ứng hover
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent evt) {
                btn.setBackground(new Color(240, 240, 240));
            }
            public void mouseExited(MouseEvent evt) {
                btn.setBackground(Color.WHITE);
            }
        });

        return btn;
    }

    // ========================================================================
    // MAIN METHOD - ĐIỂM KHỞI ĐỘNG HỆ THỐNG
    // ========================================================================

    /**
     * Main method để chạy AdminDashboard độc lập (dùng cho testing)
     *
     * Trong production, AdminDashboard sẽ được mở từ LoginScreen
     * sau khi xác thực thành công
     *
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        try {
            // Sử dụng System Look and Feel để giao diện giống OS
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Bỏ qua nếu không thể thiết lập System L&F
            e.printStackTrace();
        }

        // Khởi chạy trên Event Dispatch Thread
        SwingUtilities.invokeLater(() -> {
            new AdminDashboard().setVisible(true);
        });
    }
}