package com.ra.View.UserManagenment;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import com.ra.Common.ErrorConstants;
import com.ra.Controller.UserController;
import com.ra.Model.Entity.Users;

public class CreateNewUser extends JPanel {
    private UserController userController;

    // Kích thước TextField mặc định (Đây là kích thước tối thiểu, sẽ được override)
    private final int FIELD_WIDTH_DEFAULT = 20;

    // Khai báo Font chung cho các trường nhập liệu và nhãn tiêu đề
    private final Font FIELD_FONT = ErrorConstants.MAIN_FONT; // Sử dụng Font từ ErrorConstants (ví dụ: size 14)
    private final Dimension FIELD_DIMENSION = new Dimension(200, 30); // Kích thước cố định cho chiều dài bằng nhau

    // --- KHAI BÁO NHÃN LỖI VÀ TRƯỜNG NHẬP LIỆU ---
    private JLabel staffIdError = new JLabel();
    private JLabel userIdError = new JLabel();
    private JLabel passwordError = new JLabel();
    private JLabel projectIdError = new JLabel();
    private JLabel roleError = new JLabel();

    // Khai báo các trường để có thể truy cập chúng trong các phương thức showError/clearError
    private JTextField staffIdField;
    private JTextField userIdField;
    JPasswordField passwordField;
    private JTextField projectIdField;
    private JComboBox<String> roleComboBox;

    // Màu lỗi
    private final Color ERROR_COLOR = ErrorConstants.ERROR_COLOR;


    public CreateNewUser() {
        // --- ĐÃ BỎ BỎ TIÊU ĐỀ title = new JLabel(...) ---

        setLayout(new BorderLayout(10, 10));
        setBackground(new Color(244, 246, 249));
        // Giảm padding trên cùng của JPanel chứa Form (border) để căn Form lên cao hơn
        setBorder(new EmptyBorder(10, 20, 20, 20));

        // Tiêu đề (Đã bị loại bỏ để chỉ còn tiêu đề chính của Admin Panel)
        // JLabel title = new JLabel("新ユーザー作成 (New User Registration)");
        // title.setFont(new Font(Font.DIALOG, Font.BOLD, 22));
        // title.setBorder(new EmptyBorder(0, 0, 20, 0));
        // add(title, BorderLayout.NORTH);

        // Form Panel (Sử dụng FlowLayout để căn chỉnh FormPanel vào giữa màn hình)
        JPanel centerWrapper = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centerWrapper.setOpaque(false);
        centerWrapper.add(createFormPanel());

        add(centerWrapper, BorderLayout.CENTER);

        // Khởi tạo các nhãn lỗi ở trạng thái ẩn
        initErrorLabels();
    }

    private void initErrorLabels() {
        JLabel[] errors = {staffIdError, userIdError, passwordError, projectIdError, roleError};
        for (JLabel label : errors) {
            label.setText(" ");
            label.setForeground(ERROR_COLOR);
            label.setFont(ErrorConstants.ERROR_FONT);
        }
    }

    private void applyErrorStyle(JComponent component) {
        component.setBorder(ErrorConstants.ERROR_BORDER);
    }

    private void applyDefaultStyle(JComponent component) {
        component.setBorder(ErrorConstants.DEFAULT_BORDER);
    }

    private void showError(JLabel errorLabel, String message, JComponent fieldComponent) {
        errorLabel.setText("※ " + message);
        errorLabel.setVisible(true);
        applyErrorStyle(fieldComponent);
    }

    private void clearError(JLabel errorLabel, JComponent fieldComponent) {
        errorLabel.setText(" ");
        errorLabel.setVisible(false);
        applyDefaultStyle(fieldComponent);
    }


    private JPanel createFormPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(Color.WHITE);
        // Giảm Padding lớn của Form Panel (Border)
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        GridBagConstraints gbc = new GridBagConstraints();

        // --- ĐIỀU CHỈNH KHOẢNG CÁCH (INSETS) GIỮA CÁC TRƯỜNG ---
        // Đặt insets mặc định mới: 4px trên, 15px trái/phải, 2px dưới
        final Insets defaultInsets = new Insets(4, 15, 2, 15);
        // Đặt insets cho nhãn lỗi: 0px trên, 15px trái/phải, 4px dưới
        final Insets errorInsets = new Insets(0, 15, 4, 15);

        gbc.insets = defaultInsets;
        gbc.anchor = GridBagConstraints.WEST;

        // --- KHAI BÁO VÀ THIẾT LẬP CÁC TRƯỜNG NHẬP LIỆU ---
        staffIdField = new JTextField();
        JTextField staffNameField = new JTextField();
        JTextField deptField = new JTextField();
        userIdField = new JTextField();
        passwordField = new JPasswordField();
        projectIdField = new JTextField();

        JComponent[] inputFields = {staffIdField, staffNameField, deptField, userIdField,
                passwordField, projectIdField};

        for (JComponent field : inputFields) {
            field.setPreferredSize(FIELD_DIMENSION);
            field.setFont(FIELD_FONT);
            field.setBorder(ErrorConstants.DEFAULT_BORDER);
        }

        staffNameField.setEditable(false);
        staffNameField.setBackground(new Color(240, 240, 240));
        deptField.setEditable(false);
        deptField.setBackground(new Color(240, 240, 240));

        String[] roles = {"Supervisor", "Employee"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setPreferredSize(FIELD_DIMENSION);
        roleComboBox.setFont(FIELD_FONT);
        roleComboBox.setBorder(ErrorConstants.DEFAULT_BORDER);


        int row = 0;

        // ===================================================================
        // DÒNG 1: 社員ID, 社員名, 部署
        // ===================================================================

        gbc.insets = defaultInsets;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel("社員のID"), gbc);

        gbc.gridy = row + 1; gbc.weightx = 1.0;
        panel.add(staffIdField, gbc);

        // NHÃN LỖI (ERROR LABEL)
        gbc.insets = errorInsets;
        gbc.gridy = row + 2; gbc.weightx = 0;
        panel.add(staffIdError, gbc);

        // Mũi tên
        gbc.insets = defaultInsets;
        gbc.gridx = 1; gbc.gridy = row + 1; gbc.weightx = 0;
        panel.add(new JLabel("→"), gbc);

        // 社員名 (Staff Name)
        gbc.gridx = 2; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel("社員名"), gbc);

        gbc.gridy = row + 1; gbc.weightx = 1.0;
        panel.add(staffNameField, gbc);

        // 部署 (Department)
        gbc.gridx = 3; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel("部署"), gbc);

        gbc.gridy = row + 1; gbc.weightx = 1.0;
        panel.add(deptField, gbc);

        row += 3;
        // Loại bỏ khoảng trống lớn giữa các nhóm trường
        // gbc.gridy = row++;
        // panel.add(Box.createVerticalStrut(10), gbc);

        // ===================================================================
        // DÒNG 2: User ID, Project ID
        // ===================================================================

        gbc.insets = defaultInsets;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel("User ID"), gbc);

        gbc.gridy = row + 1; gbc.weightx = 1.0;
        panel.add(userIdField, gbc);

        // NHÃN LỖI
        gbc.insets = errorInsets;
        gbc.gridy = row + 2; gbc.weightx = 0;
        panel.add(userIdError, gbc);

        // Project ID
        gbc.insets = defaultInsets;
        gbc.gridx = 2; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel("Project ID"), gbc);

        gbc.gridy = row + 1; gbc.weightx = 1.0;
        panel.add(projectIdField, gbc);

        // NHÃN LỖI
        gbc.insets = errorInsets;
        gbc.gridy = row + 2; gbc.weightx = 0;
        panel.add(projectIdError, gbc);


        row += 3;
        // Loại bỏ khoảng trống lớn giữa các nhóm trường
        // gbc.gridy = row++;
        // panel.add(Box.createVerticalStrut(10), gbc);

        // ===================================================================
        // DÒNG 3: Password, Role
        // ===================================================================

        // Password
        gbc.insets = defaultInsets;
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel("Password"), gbc);

        gbc.gridy = row + 1; gbc.weightx = 1.0;
        panel.add(passwordField, gbc);

        // NHÃN LỖI
        gbc.insets = errorInsets;
        gbc.gridy = row + 2; gbc.weightx = 0;
        panel.add(passwordError, gbc);

        // Role (Dropdown)
        gbc.insets = defaultInsets;
        gbc.gridx = 2; gbc.gridy = row; gbc.weightx = 0;
        panel.add(new JLabel("ロール"), gbc);

        gbc.gridy = row + 1; gbc.weightx = 1.0;
        panel.add(roleComboBox, gbc);

        // NHÃN LỖI
        gbc.insets = errorInsets;
        gbc.gridy = row + 2; gbc.weightx = 0;
        panel.add(roleError, gbc);


        row += 3;
        // ===================================================================
        // NÚT CHỨC NĂNG (TẠO)
        // ===================================================================

        // gbc.gridy = row++;
        // panel.add(Box.createVerticalStrut(20), gbc);

        // Thiết lập màu nền xanh da trời nhạt (#aed6f1)
        JButton btnCreate = new JButton("作成");
        btnCreate.setPreferredSize(new Dimension(150, 40));
        btnCreate.setBackground(new Color(174, 214, 241));
        btnCreate.setForeground(Color.BLACK);
        btnCreate.setOpaque(true);
        btnCreate.setFocusPainted(false);
        btnCreate.setBorderPainted(false);

        // Đặt nút ở hàng mới, kéo dài qua tất cả các cột và căn giữa
        gbc.gridx = 0;
        gbc.gridwidth = 4; // Kéo dài 4 cột
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 15, 0, 15); // Thêm khoảng cách 20px ở trên nút

        gbc.gridy = row++;
        panel.add(btnCreate, gbc);
        gbc.gridwidth = 1; // Đặt lại gridwidth

        // Logic mô phỏng Backend/Add Listener được giữ nguyên

        staffIdField.addActionListener(e -> {
            //TODO: lấy hàm tạo từ controller từ UserController
            userController.createUser(new Users());
        });

        btnCreate.addActionListener(e -> {
            clearError(staffIdError, staffIdField);
            clearError(userIdError, userIdField);
            clearError(passwordError, passwordField);
            clearError(projectIdError, projectIdField);
            clearError(roleError, roleComboBox);


            boolean hasError = false;
            String requiredMessage = "必須";

            if (staffIdField.getText().trim().isEmpty()) {
                showError(staffIdError, requiredMessage, staffIdField);
                hasError = true;
            }
            if (userIdField.getText().trim().isEmpty()) {
                showError(userIdError, requiredMessage, userIdField);
                hasError = true;
            }
            if (new String(passwordField.getPassword()).isEmpty()) {
                showError(passwordError, requiredMessage, passwordField);
                hasError = true;
            }
            if (projectIdField.getText().trim().isEmpty()) {
                showError(projectIdError, requiredMessage, projectIdField);
                hasError = true;
            }

            if (!hasError) {
                JOptionPane.showMessageDialog(this, "Tạo User mới thành công và lưu vào DB/UserList!",
                        "Tạo user thành công", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, ErrorConstants.ERROR_MESSAGE_CREATE_USER_MISSING_FIELDS,
                        "Tạo user thất bại", JOptionPane.ERROR_MESSAGE);
            }
        });

        return panel;
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame("Create New User Test");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 500);
        frame.add(new CreateNewUser());
        frame.setVisible(true);
    }
}