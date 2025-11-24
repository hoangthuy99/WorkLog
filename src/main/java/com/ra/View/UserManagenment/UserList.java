package com.ra.View.UserManagenment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

public class UserList extends JPanel {

    private JTable userTable;
    private DefaultTableModel tableModel;

    // Định nghĩa Font và Màu sắc
    private final Font FONT_BASE = new Font(Font.DIALOG, Font.PLAIN, 14);
    private final Font FONT_HEADER = new Font(Font.DIALOG, Font.BOLD, 14);
    private final Color COLOR_BG_LIGHT = new Color(244, 246, 249);

    // --- MÀU MỚI THEO YÊU CẦU MỚI NHẤT ---
    // Nền nút Tạo mới: Xanh lá nhạt (#c3e6cb)
    private final Color COLOR_NEW_USER_BUTTON_BG = new Color(195, 230, 203);
    private final Color COLOR_NEW_USER_BUTTON_FG = Color.BLACK; // Chữ nút Tạo mới: Đen

    // Nền nút Sửa: Xanh dương nhạt (#b3cde3)
    private final Color COLOR_EDIT_BUTTON_BG = new Color(179, 205, 227);
    // Nền nút Xóa: Đỏ nhạt (#f7a7a3)
    private final Color COLOR_DELETE_BUTTON_BG = new Color(247, 167, 163);
    // -------------------------------------

    private final Color COLOR_PAGINATION_BORDER = Color.GRAY;


    public UserList() {
        setLayout(new BorderLayout(10, 10));
        setBackground(COLOR_BG_LIGHT);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // 1. Header và Thanh công cụ (North)
        JPanel topPanel = createTopPanel();
        add(topPanel, BorderLayout.NORTH);

        // 2. Bảng dữ liệu (Center)
        JScrollPane tableScrollPane = createTablePanel();
        add(tableScrollPane, BorderLayout.CENTER);

        // 3. Phân trang (South)
        JPanel footerPanel = createFooterPanel();
        add(footerPanel, BorderLayout.SOUTH);
    }

    // --- 1. Tạo Thanh Công cụ và Tìm kiếm đã sửa đổi ---
    private JPanel createTopPanel() {
        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        topPanel.setOpaque(false);

        // Panel Tìm kiếm và Lọc (phía Tây)
        JPanel searchFilterPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 5));
        searchFilterPanel.setOpaque(false);

        // Trường nhập キーワード (Keyword)
        JTextField keywordField = new JTextField(15);
        keywordField.setFont(FONT_BASE);
        keywordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.LIGHT_GRAY),
                new EmptyBorder(5, 5, 5, 5)
        ));

        // Nút Tìm kiếm (検索)
        JButton btnSearch = new JButton("検索");
        btnSearch.setFont(FONT_BASE);
        btnSearch.setPreferredSize(new Dimension(80, 30));

        // Nút Tất cả (すべて)
        JButton btnAll = new JButton("すべて");
        btnAll.setFont(FONT_BASE);
        btnAll.setPreferredSize(new Dimension(80, 30));

        // Sửa nhãn thành キーワード
        searchFilterPanel.add(new JLabel("キーワード (Keyword):"));
        searchFilterPanel.add(keywordField);
        searchFilterPanel.add(btnSearch);
        searchFilterPanel.add(btnAll);

        // Panel chứa các nút chức năng (phía Đông)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        buttonPanel.setOpaque(false);

        // --- SỬA MÀU NÚT Tạo mới User (New) ---
        JButton btnNewUser = new JButton("新ユーザー作成 (New)");
        btnNewUser.setFont(FONT_BASE);
        btnNewUser.setBackground(COLOR_NEW_USER_BUTTON_BG); // Xanh lá nhạt
        btnNewUser.setForeground(COLOR_NEW_USER_BUTTON_FG); // Chữ Đen
        btnNewUser.setFocusPainted(false);
        btnNewUser.setOpaque(true);
        btnNewUser.setBorderPainted(false); // Bỏ viền mặc định

        buttonPanel.add(btnNewUser);


        topPanel.add(searchFilterPanel, BorderLayout.WEST);
        topPanel.add(buttonPanel, BorderLayout.EAST);

        topPanel.setBorder(new EmptyBorder(0, 0, 10, 0));

        // Gắn sự kiện (Ví dụ: Chuyển sang màn hình tạo mới khi nhấp)
        btnNewUser.addActionListener(e -> {
            JOptionPane.showMessageDialog(this, "Chuyển sang màn hình 'CreateNewUser' (Tạo User Mới).");
        });

        return topPanel;
    }

    // --- 2. Tạo Bảng Dữ liệu (Thêm Renderer/Editor cho các nút) ---
    private JScrollPane createTablePanel() {
        // Tên cột đã được điều chỉnh theo yêu cầu và mẫu
        Vector<String> columnNames = new Vector<>();
        columnNames.add("社員ID");
        columnNames.add("社員名");
        columnNames.add("部署");
        columnNames.add("プロジェクト");
        columnNames.add("ユーザーID");
        columnNames.add("パスワード");
        columnNames.add("編集"); // Sửa - Cột 6
        columnNames.add("削除"); // Xóa - Cột 7

        // Dữ liệu mẫu
        Vector<Vector<Object>> data = new Vector<>();
        data.add(createRow("S111", "Nguyen Van A", "開発", "P001", "NVAn", "******", "", ""));
        data.add(createRow("S112", "Tran Van B", "営業", "P002", "TVanB", "******", "", ""));
        data.add(createRow("S113", "Le Thi C", "人事", "P003", "LTC", "******", "", ""));

        tableModel = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 6 || column == 7;
            }
        };
        userTable = new JTable(tableModel);

        // Thiết lập giao diện bảng
        userTable.setRowHeight(35);
        userTable.setFont(FONT_BASE);
        userTable.getTableHeader().setFont(FONT_HEADER);
        userTable.getTableHeader().setBackground(new Color(236, 240, 241));
        userTable.getTableHeader().setReorderingAllowed(false);

        // --- CĂN GIỮA TIÊU ĐỀ CỘT ---
        DefaultTableCellRenderer headerRenderer = (DefaultTableCellRenderer) userTable.getTableHeader().getDefaultRenderer();
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        // -----------------------------

        // Căn chỉnh chiều rộng cột
        userTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        userTable.getColumnModel().getColumn(6).setPreferredWidth(60);
        userTable.getColumnModel().getColumn(7).setPreferredWidth(60);

        // --- CÀI ĐẶT RENDERER VÀ EDITOR CHO CỘT NÚT ---
        userTable.getColumn("編集").setCellRenderer(new ButtonRenderer("編集", COLOR_EDIT_BUTTON_BG));
        userTable.getColumn("編集").setCellEditor(new ButtonEditor(this, "編集", "EDIT", COLOR_EDIT_BUTTON_BG));

        userTable.getColumn("削除").setCellRenderer(new ButtonRenderer("削除", COLOR_DELETE_BUTTON_BG));
        userTable.getColumn("削除").setCellEditor(new ButtonEditor(this, "削除", "DELETE", COLOR_DELETE_BUTTON_BG));
        // -----------------------------------------------------------------

        JScrollPane scrollPane = new JScrollPane(userTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        return scrollPane;
    }

    // Phương thức tiện ích tạo hàng Vector
    private Vector<Object> createRow(String staffId, String staffName, String dept, String project, String userId, String password, String edit, String delete) {
        Vector<Object> row = new Vector<>();
        row.add(staffId);
        row.add(staffName);
        row.add(dept);
        row.add(project);
        row.add(userId);
        row.add(password);
        row.add(edit);
        row.add(delete);
        return row;
    }


    // --- 3. Tạo Panel Phân trang (Footer) ---
    private JPanel createFooterPanel() {
        // Dùng FlowLayout để căn chỉnh các nút điều hướng sang phải
        JPanel footerPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        footerPanel.setBackground(Color.WHITE);

        // Panel chứa các nút điều hướng
        JPanel navPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 5, 0));
        navPanel.setBackground(Color.WHITE);

        // Nút "<"
        JButton btnPrev = new JButton("<");
        btnPrev.setPreferredSize(new Dimension(30, 25));
        btnPrev.setFocusPainted(false);
        btnPrev.setBorder(new LineBorder(COLOR_PAGINATION_BORDER, 1));

        // Ô nhập/hiển thị trang hiện tại
        JTextField currentPageField = new JTextField("1", 3);
        currentPageField.setHorizontalAlignment(SwingConstants.CENTER);
        currentPageField.setFont(FONT_BASE);
        currentPageField.setPreferredSize(new Dimension(40, 25));
        currentPageField.setBorder(new LineBorder(COLOR_PAGINATION_BORDER, 1));

        // Nút ">"
        JButton btnNext = new JButton(">");
        btnNext.setPreferredSize(new Dimension(30, 25));
        btnNext.setFocusPainted(false);
        btnNext.setBorder(new LineBorder(COLOR_PAGINATION_BORDER, 1));


        // Thêm các nút vào navPanel
        navPanel.add(btnPrev);
        navPanel.add(currentPageField);
        navPanel.add(btnNext);
        navPanel.setBorder(new EmptyBorder(0, 0, 0, 15));

        // Thêm navPanel vào footerPanel
        footerPanel.add(navPanel);

        // Thiết lập viền trên màu xám nhẹ
        footerPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(1, 0, 0, 0, Color.LIGHT_GRAY),
                new EmptyBorder(5, 10, 5, 10)
        ));

        return footerPanel;
    }
}


// =================================================================
// LỚP HỖ TRỢ HIỂN THỊ NÚT TRONG JTABLE (RENDERER)
// =================================================================

class ButtonRenderer extends JButton implements TableCellRenderer {
    private String buttonText;

    public ButtonRenderer(String text, Color color) {
        setOpaque(true); // Rất quan trọng để hiển thị màu nền đầy đủ
        this.buttonText = text;
        setText(buttonText);
        setBackground(color); // Dùng màu nền truyền vào (Xanh dương nhạt / Đỏ nhạt)
        setForeground(Color.BLACK); // CHỮ ĐEN
        setFont(new Font(Font.DIALOG, Font.BOLD, 12));
        setFocusPainted(false);
        setBorder(new EmptyBorder(5, 5, 5, 5));
        // Bỏ viền BorderPainted(false) để hiển thị màu nền trọn vẹn
        setBorderPainted(false);
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus, int row, int column) {
        return this;
    }
}


// =================================================================
// LỚP HỖ TRỢ XỬ LÝ SỰ KIỆN KHI NHẤP VÀO NÚT TRONG JTABLE (EDITOR)
// =================================================================

class ButtonEditor extends DefaultCellEditor {
    private JButton button;
    private String label;
    private boolean isPushed;
    private JTable table;
    private JPanel parentPanel;

    private String actionType; // "EDIT" hoặc "DELETE"

    public ButtonEditor(JPanel parentPanel, String buttonText, String actionType, Color color) {
        super(new JCheckBox());
        this.parentPanel = parentPanel;
        this.actionType = actionType;
        this.button = new JButton(buttonText);
        this.button.setOpaque(true); // Rất quan trọng để hiển thị màu nền đầy đủ
        this.button.setFocusPainted(false);
        this.button.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
        this.button.setBorder(new EmptyBorder(5, 5, 5, 5));

        // Thiết lập màu sắc (Dùng màu truyền vào)
        this.button.setBackground(color);
        this.button.setForeground(Color.BLACK); // CHỮ ĐEN
        this.button.setBorderPainted(false); // Bỏ viền mặc định


        this.button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fireEditingStopped();
            }
        });
        this.label = buttonText;
    }

    @Override
    public Component getTableCellEditorComponent(JTable table, Object value,
                                                 boolean isSelected, int row, int column) {
        this.table = table;
        this.label = (value == null) ? "" : value.toString();
        isPushed = true;
        return button;
    }

    @Override
    public Object getCellEditorValue() {
        if (isPushed) {
            String staffId = (String) table.getModel().getValueAt(table.getSelectedRow(), 0);

            if (actionType.equals("EDIT")) {
                // Xử lý nút Sửa
                JOptionPane.showMessageDialog(parentPanel,
                        "Chuyển sang màn hình Sửa thông tin User ID: " + staffId +
                                "\n(Mô phỏng: giống màn hình Tạo User Mới)");

            } else if (actionType.equals("DELETE")) {
                // Xử lý nút Xóa
                int dialogResult = JOptionPane.showConfirmDialog(parentPanel,
                        "Bạn có chắc chắn muốn xóa User ID: " + staffId + "?",
                        "Xác nhận xóa",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE);

                if (dialogResult == JOptionPane.YES_OPTION) {
                    JOptionPane.showMessageDialog(parentPanel, "User ID: " + staffId + " đã được xóa (Mô phỏng).", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
                    // Ở đây sẽ thêm logic xóa hàng khỏi tableModel
                }
            }
        }
        isPushed = false;
        return label;
    }

    @Override
    public boolean stopCellEditing() {
        isPushed = false;
        return super.stopCellEditing();
    }
}